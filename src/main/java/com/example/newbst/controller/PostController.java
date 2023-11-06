package com.example.newbst.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.newbst.Mapper.PostMapper;
import com.example.newbst.dto.DetailPost;
import com.example.newbst.dto.Posts;
import com.example.newbst.dto.Result;
import com.example.newbst.pojo.Message;
import com.example.newbst.pojo.Post;
import com.example.newbst.service.MessageService;
import com.example.newbst.service.PostService;
import com.example.newbst.utils.HtmlFilter;
import com.example.newbst.utils.ImageUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.newbst.utils.RedisConstants.POST;

@RestController
@RequestMapping("/post")
@Slf4j
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ImageUtil imageUtil;

    TreeMap<Integer, Posts> posts;

    /**
     * 上传文章
     * @param post
     * @return
     */
    @PostMapping("uplaod")
    public Result UploadEssay(@RequestBody Post post) {
        if ("" == post.getType() || null == post.getType() || post.getType().length() > 4) {
            return Result.fail("请添加文章标签");
        }
//        post.setBody(Jsoup.clean(post.getBody(), Whitelist.basic()));
        post.setBody(HtmlFilter.create().clean(post.getBody()));
        imageUtil.CleanPic(post.getBody(), post.getTag());
        postService.save(post);
        return Result.ok();
    }

    /**
     * 查看帖子详情
     * post表 + message表
     * @param pid postId
     * @return
     */
    @GetMapping("/detail/{id}")
    public Result CheckDetail(@PathVariable("id") Integer pid) {
        Post post = postService.queryById(pid);
        List<Message> list = messageService.list(new LambdaQueryWrapper<Message>().eq(Message::getPostId, pid));
        DetailPost detailPost = new DetailPost(post, list);
        return Result.ok(detailPost);
    }

    /**
     * 查看当前分类下的热门帖子
     * @param type
     * @return
     */
    @GetMapping("/view/{type}/")
    public Result getView(@PathVariable("type") String type,
                          @RequestParam(value = "page", defaultValue = "1") int page,
                          @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        if (null == type || "" == type || type.length() > 4) {
            return Result.fail("请设置有效分类");
        }
        List<Post> records = getPost(page, pageSize, type);
        getMainImg(records);
        return Result.ok(records);
    }



    /**
     * 热门帖子
     * @return
     */
    @GetMapping("/hottopic/")
    public Result getView(@RequestParam(value = "page", defaultValue = "1") int page,
                          @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        List<Post> post = getPost(page, pageSize, "");
        // 提取主要图片
        getMainImg(post);
        return Result.ok(post);
    }

    @GetMapping("startedit")
    public Result StartEdit() {
        return Result.ok((int)(Math.random()*1000000));
    }

    /**
     * 更新帖子
     * @param post
     * @return
     */
    @PatchMapping("/update")
    public Result UpdatePost(@RequestBody Post post) {
        Post ori = postService.getById(post.getId());
        if (ori == null) {
            return Result.fail("未找到要修改的文章");
        }
        imageUtil.CleanPic(post.getBody(), post.getTag());
        String postJson = stringRedisTemplate.opsForValue().getAndDelete(POST + post.getId());
        Post bef = JSONUtil.toBean(postJson, Post.class);
        // 将修改累计的浏览量写入
        post.setViews(bef.getViews());
        postService.updateById(post);
        return Result.ok();
    }

    /**
     * 删除帖子
     * @param pid
     * @return
     */
    @DeleteMapping("/delpost/{id}")
    public Result DelPost(@PathVariable("id") String pid) {
        try {
            Post post = postService.getById(pid);
            // 删除关联的图片
            imageUtil.CleanPic(post.getBody(), "");
            // 删除缓存
            stringRedisTemplate.delete(POST + post.getId());
            postService.removeById(pid);
            return Result.ok();
        } catch (DataIntegrityViolationException e) {
            return Result.fail("您输入的文章id无效");
        }
    }

    /**
     * 关键词搜索
     * @return
     */
    @GetMapping("search")
    public Result Search(@RequestBody Map<String, String> map) {
        String keyWord = map.get("keyword");
        if (null == keyWord) {
            return Result.fail("搜索内容不能为空");
        }
        if (keyWord.isBlank()) {
            return Result.fail("搜索内容不能为特殊字符");
        }
        posts = new TreeMap<Integer, Posts>(Posts.HighWeight);
        if (keyWord.length() < 10) {
            // 分词
            List<Term> segment = HanLP.segment(keyWord);
            List<String> collect = segment.stream().map(Term::toString)
                    // 过滤符号，过滤term的/词性的部分
                    .filter(s -> !s.contains("/w")).map(s -> s.split("/")[0]).collect(Collectors.toList());
            SearchInPost(collect);
        }
        if (keyWord.length() > 30) {
            // 提取关键字
            SearchInPost(HanLP.extractKeyword(keyWord, 5));

        }
        return Result.ok(posts);
    }

    public void SearchInPost(List<String> segment) {
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        for (String key : segment) {
            queryWrapper.like(Post::getTitle, key);
            List<Post> post = postMapper.selectList(queryWrapper);
            for(Post p : post) {
                if (!posts.isEmpty() && posts.containsKey(p.getId())) {
                    Posts posts1 = posts.get(p.getId());
                    posts1.setWeight(posts1.getWeight() + 1);
                } else {
                    posts.put(p.getId(), new Posts(p));
                }
            }
        }
    }

    private void getMainImg(List<Post> posts) {
        for (Post p : posts) {
            String[] src = new String[3];
            Document parse = Jsoup.parse(p.getBody());
            Elements isContainImg = parse.select("img").empty();
            if (isContainImg.size() == 0) {
                continue;
            }
            for (int i = 0; i < isContainImg.size() || i < 3; i ++ ) {
                src[i] = isContainImg.get(i).attr("src");
            }
            p.setImg(src.toString());
        }
    }


    /**
     * 得到 views 降序的post记录，具备处理分类的能力
     * @param page
     * @param pageSize
     * @param type
     * @return
     */
    private List<Post> getPost( int page, int pageSize, String type) {
        Page<Post> postPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Post::getViews);
        if (!type.isEmpty()) {
            queryWrapper.eq(Post::getType, type);
        }
        List<Post> records = postService.page(postPage, queryWrapper).getRecords();
        return records;
    }

}

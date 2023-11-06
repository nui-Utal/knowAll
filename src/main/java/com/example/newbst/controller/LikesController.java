package com.example.newbst.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.newbst.Mapper.PostMapper;
import com.example.newbst.dto.Result;
import com.example.newbst.pojo.Likes;
import com.example.newbst.service.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * created by Xu on 2023/8/20 15:52.
 */
@RestController
public class LikesController {

    @Autowired
    private LikesService likesService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private PostMapper postMapper;


    /**
     * 喜欢文章。修改likes表并同步post数据
     * @param map
     * @return
     */
    @PostMapping("/post/like")
    public Result LikePost(@RequestBody Map<String, Integer> map) {
        Integer pid = map.get("pid");
        String username = (String) httpServletRequest.getAttribute("username");
        Likes likes = new Likes(username, pid);
        postMapper.addLike(pid);
        likesService.save(likes);
        return Result.ok();
    }

    /**
     * 取消对文章的喜欢。修改likes表并同步post数据
     * @param postId
     * @return
     */
    @DeleteMapping("/post/{pid}/dislike")
    public Result DisLike(@PathVariable("pid") Integer postId) {
        String username = (String) httpServletRequest.getAttribute("username");
        Likes likes = new Likes(username, postId);
        postMapper.disLike(postId);
        likesService.removeById(likes);
        return Result.ok();
    }

    /**
     * 查看我的喜欢
     * @return
     */
    @GetMapping("mylikes")
    public Result getMyLike() {
        Object username = httpServletRequest.getAttribute("username");
        LambdaQueryWrapper<Likes> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Likes::getCreateTime);
        queryWrapper.eq(Likes::getUsername, username);
        List<Likes> list = likesService.list();
        return Result.ok(list);
    }

}

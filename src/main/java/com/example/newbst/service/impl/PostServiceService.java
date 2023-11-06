package com.example.newbst.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.newbst.Mapper.PostMapper;
import com.example.newbst.pojo.Post;
import com.example.newbst.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.example.newbst.utils.RedisConstants.*;

@Service
public class PostServiceService extends ServiceImpl<PostMapper, Post> implements PostService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Post queryById(Integer id) {
        // 查询缓存
        String postJson = stringRedisTemplate.opsForValue().get(POST + id);
        if (StrUtil.isNotBlank(postJson)) {
            Post post = JSONUtil.toBean(postJson, Post.class);
            // 更新 view 并存入redis
            post.setViews(post.getViews() + 1);
            stringRedisTemplate.opsForValue().set(POST_VIEWS + post.getId(), post.getViews().toString());
            // 跟新 post 的 ttl
            stringRedisTemplate.opsForValue().set(POST + id, JSONUtil.toJsonStr(post), POST_TTL, TimeUnit.DAYS);
            return post;
        }
        if (postJson != null) {
            return null;
        }
        Post post = this.getById(id);
        if (null == post) {
            // 存入空值
            stringRedisTemplate.opsForValue().set(POST + id, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
        }
        stringRedisTemplate.opsForValue().set(POST + id, JSONUtil.toJsonStr(post), POST_TTL, TimeUnit.DAYS);
        stringRedisTemplate.opsForValue().set(POST_VIEWS + post.getId(), post.getViews().toString());
        return post;
    }


}

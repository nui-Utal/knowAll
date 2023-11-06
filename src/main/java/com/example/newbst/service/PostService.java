package com.example.newbst.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.newbst.pojo.Post;

public interface PostService extends IService<Post> {

    public Post queryById(Integer id);
}

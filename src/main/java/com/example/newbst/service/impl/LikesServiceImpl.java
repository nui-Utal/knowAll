package com.example.newbst.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.newbst.Mapper.LikesMapper;
import com.example.newbst.pojo.Likes;
import com.example.newbst.service.LikesService;
import org.springframework.stereotype.Service;

/**
 * created by Xu on 2023/8/20 15:54.
 */
@Service
public class LikesServiceImpl extends ServiceImpl<LikesMapper, Likes> implements LikesService {
}

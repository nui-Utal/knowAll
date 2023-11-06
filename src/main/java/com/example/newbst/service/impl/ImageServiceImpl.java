package com.example.newbst.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.newbst.Mapper.ImageMapper;
import com.example.newbst.pojo.Image;
import com.example.newbst.service.ImageService;
import org.springframework.stereotype.Service;

/**
 * created by Xu on 2023/8/24 17:50.
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {
}

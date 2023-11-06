package com.example.newbst.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.newbst.Mapper.UserMapper;
import com.example.newbst.pojo.User;
import com.example.newbst.service.UserImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserImpl {
    @Autowired
   private UserMapper userMapper;

}

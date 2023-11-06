package com.example.newbst.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.newbst.Mapper.MessageMapper;
import com.example.newbst.pojo.Message;
import com.example.newbst.service.MessageService;
import org.springframework.stereotype.Service;

/**
 * created by Xu on 2023/8/20 17:15.
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
}

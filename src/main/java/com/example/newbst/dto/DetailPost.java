package com.example.newbst.dto;

import com.example.newbst.pojo.Message;
import com.example.newbst.pojo.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * created by Xu on 2023/8/20 19:13.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailPost {
    // 帖子本身
    private Post post;
    // 相关留言
    private List<Message> messages;
}

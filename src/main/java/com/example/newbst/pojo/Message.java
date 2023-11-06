package com.example.newbst.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * created by Xu on 2023/8/19 16:08.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private Integer id;

    private Integer postId;

    private String username;

    private String content;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public Message(Integer postId, String content) {
        this.postId = postId;
        this.content = content;
    }
}

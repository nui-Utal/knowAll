package com.example.newbst.pojo;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * created by Xu on 2023/8/19 16:07.
 */
@Data
@NoArgsConstructor
public class Likes {
    private Integer id;

    private Integer postId;

    private String username;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public Likes(String username, Integer postId) {
        this.username = username;
        this.postId = postId;
    }
}

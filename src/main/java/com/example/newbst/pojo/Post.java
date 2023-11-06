package com.example.newbst.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Post {
    //帖子id

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 标题
    private String title;

    private String intro;

    private String body;

    private String type;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String admin;

    private Integer views;

    private Integer likes;

    private String keyWord;

    @TableField(exist = false)
    private String img;

    @TableField(exist = false)
    private String tag;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

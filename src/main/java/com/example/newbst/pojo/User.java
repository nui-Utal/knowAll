package com.example.newbst.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;


@Data
public class User {
    private Integer id;

//    微信登录微信id
    private String wxid;

//    用户类别，是否为管理员可以登录后台
    private Integer type;

    // qq号
    private String qqNum;

    // 微信号
    private String wxNum;

//    逻辑删除
    private Integer available;

//    微信姓名
    private  String wxname;


}

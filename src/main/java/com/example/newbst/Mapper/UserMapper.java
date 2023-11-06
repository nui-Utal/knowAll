package com.example.newbst.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.example.newbst.pojo.User;
import org.apache.ibatis.annotations.*;
import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM user")
    List<User> findAll();

    @Select("SELECT * FROM user wxid = #{wxid}")
    User findopenid(String wxid);

    @Insert("INSERT INTO user (wxid) VALUES (#{wxid})")
    Boolean insertuser(User user);

    @Insert("INSERT INTO user (wx_num,qq_num) VALUES (#{wxNum},#{qqNum}) where wxid = #{wxid}")
    Boolean insertmessage(String wxNum,String qqNum,String wxid);
}

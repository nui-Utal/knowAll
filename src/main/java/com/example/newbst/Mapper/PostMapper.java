package com.example.newbst.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.newbst.pojo.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    @Update("UPDATE post SET likes = likes + 1 where id = #{id}")
    int addLike(Integer id);

    @Update("UPDATE post SET likes = likes - 1 where id = #{id}")
    int disLike(Integer id);

    @Update("UPDATE post SET views = #{views} where id = #{id}")
    int updateView(String id, String views);

}

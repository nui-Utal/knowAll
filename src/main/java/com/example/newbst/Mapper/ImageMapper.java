package com.example.newbst.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.newbst.pojo.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * created by Xu on 2023/8/24 17:49.
 */
@Mapper
public interface ImageMapper extends BaseMapper<Image> {
    @Update("UPDATE image SET usages = usages + 1 WHERE id = #{id}")
    void plusUsage(@Param("id") Integer id);

    @Update("UPDATE image SET usages = usages - 1 WHERE name = #{name}")
    void subtractUsage(@Param("name") String name);
}

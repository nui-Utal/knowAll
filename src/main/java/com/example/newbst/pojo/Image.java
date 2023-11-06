package com.example.newbst.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by Xu on 2023/8/24 17:47.
 */
@Data
@NoArgsConstructor
public class Image {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String hash;

    private String name;

    // 图片被引用次数
    private Integer usages;

    public Image(String name, String hash) {
        this.name = name;
        this.hash = hash;
    }
}

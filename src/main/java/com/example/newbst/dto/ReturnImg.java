package com.example.newbst.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by Xu on 2023/8/22 17:43.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnImg {
    Integer errno;
    ReturnImgUrl data;
}

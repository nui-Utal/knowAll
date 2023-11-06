package com.example.newbst.dto;

import com.example.newbst.pojo.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;


/**
 * created by Xu on 2023/8/21 16:14.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Posts {
    public static Comparator<? super Integer> HighWeight;
    private int weight = 0;

    private Post post;

    public Posts(Post post) {
        this.weight ++;
        this.post = post;
    }

    public static class HighWeight implements Comparator<Posts> {
        @Override
        public int compare(Posts obj1, Posts obj2) {
            // 根据特定的比较逻辑进行比较
            return obj1.getWeight() - obj2.getWeight();
        }
    }

}

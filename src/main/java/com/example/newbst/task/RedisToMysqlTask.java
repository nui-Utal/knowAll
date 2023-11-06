package com.example.newbst.task;

import com.example.newbst.Mapper.PostMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;

import static com.example.newbst.utils.RedisConstants.POST_VIEWS;

/**
 * created by Xu on 2023/8/25 15:12.
 */
@Slf4j
@Component
public class RedisToMysqlTask {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PostMapper postMapper;

    @Scheduled(fixedDelay = 5 * 60000) // 每 60 秒执行一次
    public void syncViewsFromRedisToMysql() {
        // 从 Redis 中获取浏览量数据并同步到 MySQL
        Set<String> keys = stringRedisTemplate.keys(POST_VIEWS + "*");
        // 得到键名称的字符串 eg.["POST_VIEWS:1", "POST_VIEWS:2", "POST_VIEWS:3"]
        for (String id : keys) {
            String views = stringRedisTemplate.opsForValue().get(id);
            String[] split = id.split(":");
            postMapper.updateView(split[split.length - 1], views);
        }
        log.info("正在将redis数据写入mysql");
    }

    @Scheduled(fixedDelay = 3600000)    // deleteRedisKey per hour
    public void syncViewsFromRedis2Mysql() {
        // 从 Redis 中获取浏览量数据并同步到 MySQL
        Set<String> keys = stringRedisTemplate.keys(POST_VIEWS + "*");
        for (String key : keys) {
            String views = stringRedisTemplate.opsForValue().get(key);
            stringRedisTemplate.delete(key);
            String[] split = key.split(":");
            postMapper.updateView(split[split.length - 1], views);
        }
    }
}

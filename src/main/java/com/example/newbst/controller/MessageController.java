package com.example.newbst.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.newbst.dto.Result;
import com.example.newbst.pojo.Message;
import com.example.newbst.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * created by Xu on 2023/8/20 17:17.
 */
@RestController
@RequestMapping("/post")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 发送留言
     * @param message
     * @return
     */
    @PostMapping("message")
    public Result SendMes(@RequestBody Message message) {
        String username = (String) httpServletRequest.getAttribute("username");
        message.setUsername(username);
        messageService.save(message);
        return Result.ok();
    }

    /**
     * 删除留言
     * @param mid
     * @return
     */
    @DeleteMapping("delmes/{mid}")
    public Result DelMes(@PathVariable("mid") Integer mid) {
        messageService.removeById(mid);
        return Result.ok();
    }

//    @GetMapping("message/{pid}")
//    public Result GetMessage(@PathVariable("pid") Integer pid) {
//        // 使用lambda一定要指定类型
//        List<Message> list = messageService.list(new LambdaQueryWrapper<Message>().eq(Message::getPostId, pid));
//        return Result.ok(list);
//    }
}

package com.example.newbst.controller;


import com.example.newbst.Mapper.UserMapper;
import com.example.newbst.dto.Result;
import com.example.newbst.pojo.User;
import com.example.newbst.utils.ApiResponse;
import com.example.newbst.utils.JwtToken;
import com.example.newbst.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    private static final String appId = "";

    private static final String appSecret = "";

    @PostMapping("/logout")
    public Result logout() {
        try {
            // Simulate some logic to fetch data
            System.out.println("输出");
//            校验密码
            ResponseData response = ResponseData.success("登出成功", null);
            return Result.ok(response);

        } catch (Exception e) {
            return Result.fail("系统错误");
        }
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        try {
            // Simulate some logic to fetch data
            String  wxid=user.getWxid();

//            校验密码
            Boolean istrue = userMapper.insertuser(user);

            if (istrue) {

                User user1=userMapper.findopenid(wxid);
                String token= JwtToken.generateToken(user1);
                if (token == null||token.equals("")) {
                    return Result.fail("系统错误");
                }
                ApiResponse response = new ApiResponse();
                response.setData(user1);
                response.setToken(token);
                ResponseData response1 = ResponseData.success("登录成功", response);
                return Result.ok(response1);
            }else {
                return Result.fail("登陆失败");
            }

        } catch (Exception e) {
            System.out.println(e);
            return Result.fail("系统错误");
        }
    }

    @PostMapping("/message")
    public Result message(@RequestBody User user) {
        try {
            // Simulate some logic to fetch data
            String  wxid=user.getWxid();
            String  wxNum=user.getWxNum();
            String  qqNum=user.getQqNum();

//            校验密码
            Boolean istrue = userMapper.insertuser(user);

            if (istrue) {

                Boolean ok=userMapper.insertmessage(wxNum,qqNum,wxid);

                if (!ok) {
                    return Result.fail("系统错误");
                }
                ApiResponse response = new ApiResponse();
                ResponseData response1 = ResponseData.success("信息添加成功", response);
                return Result.ok(response1);
            }else {
                return Result.fail("登陆失败");
            }

        } catch (Exception e) {
            System.out.println(e);
            return Result.fail("系统错误");
        }
    }



}

package com.example.newbst.config;


import com.example.newbst.utils.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private TokenInterceptor tokenInterceptor;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(tokenInterceptor)
//                .addPathPatterns("/user/logout");  // 设置需要拦截的路径
//
//        registry.addInterceptor(tokenInterceptor)
//                .addPathPatterns(
//                        "/user/getlist"
//                        ,"/post/**"
//                        ,"/mylikes"
//                        );  // 设置需要拦截的路径
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 这里的classpath是resources作为当前目录
        registry.addResourceHandler("/post/uploadpic/**")
                .addResourceLocations("classpath:/static/");
        // 通过classpath定位到resource文件夹
//        registry.addResourceHandler("/font/**").addResourceLocations("classpath:/font/");
    }
}

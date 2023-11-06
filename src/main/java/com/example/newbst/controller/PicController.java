package com.example.newbst.controller;

import com.example.newbst.Mapper.ImageMapper;
import com.example.newbst.dto.ReturnImg;
import com.example.newbst.dto.ReturnImgUrl;
import com.example.newbst.pojo.Image;
import com.example.newbst.service.ImageService;
import com.example.newbst.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;

import static com.example.newbst.utils.RedisConstants.POST_IMG;


/**
 * created by Xu on 2023/8/20 10:30.
 */
@RestController
@RequestMapping("post")
public class PicController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ImageUtil imageUtil;

    private static final String[] suffixWhiteList = {"png","jpeg","jpg","gif"};

    public static ClassLoader classLoader = PicController.class.getClassLoader();

    public static String savePath = classLoader.getResource("static").getPath();

    // 这里要做的就只是正常地保存图片，再返回对应的请求路径
    @PostMapping("/uploadpic")
    public ReturnImg UploadPic(@RequestParam("file") MultipartFile file, @RequestParam("tag") Integer tag)
            throws IOException, NoSuchAlgorithmException {
        if (file.isEmpty()) {
            return new ReturnImg(0, new ReturnImgUrl());
        }
        long timestamp = System.currentTimeMillis();
        String base36Code = Long.toString(timestamp, 36);
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename()).toLowerCase();
        if (!Arrays.stream(suffixWhiteList).anyMatch(x -> x.equalsIgnoreCase(fileExtension))) {
            return new ReturnImg(0, new ReturnImgUrl());
        }
        String fileName = base36Code + "." + fileExtension;
        File saveFile = new File(savePath, fileName);
        // 得到当前图片的hash值
        String hash = imageUtil.calculateHash(file);
        List<Image> list = imageService.list();
        boolean isSavedSimilarImg = false;
        for(Image i : list) {
            // ==》判断图片是否相似，相似则将获取原图片名称返回，但删除原图片，保存新图片
            if (imageUtil.isImageSimilarity(hash, i.getHash())) {
                // 在保存新图片之前删除原图片
                new File(savePath, i.getName()).delete();
                // 添加图片被引用次数
                imageMapper.plusUsage(i.getId());
                fileName = i.getName();
                isSavedSimilarImg = true;
                break;
            }
        }
        if (!isSavedSimilarImg) {
            imageService.save(new Image(fileName, hash));
        }
        // 保存图片到本地
        try (FileOutputStream fos = new FileOutputStream(saveFile)) {
            fos.write(file.getBytes());
        } catch (Exception e) {
            return new ReturnImg(0, new ReturnImgUrl());
        }

        // 保存图片后将图片名存入key对应的set中
        stringRedisTemplate.opsForSet().add(POST_IMG + tag, fileName);

        // 直接打印localhost是设备/ip
        InetAddress localHost = InetAddress.getLocalHost();
        return new ReturnImg(0, new ReturnImgUrl(localHost.getHostAddress() + "/post/uploadpic/" + fileName));
    }


 }

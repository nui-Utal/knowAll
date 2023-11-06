package com.example.newbst.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.newbst.Mapper.ImageMapper;
import com.example.newbst.controller.PicController;
import com.example.newbst.pojo.Image;
import com.example.newbst.service.ImageService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.newbst.utils.RedisConstants.POST_IMG;

/**
 * created by Xu on 2023/8/26 15:11.
 */
@Component
public class ImageUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageMapper imageMapper;

    /**
     * 整理提交内容中的所有图片，清除不需要的图片
     * 删除已删除帖子中的所有图片
     * @param body
     */
    public void CleanPic(String body, String tag) {
        Document doc = Jsoup.parse(body);

        // 修改后确认的帖子，如果没有图片依旧需要查看redis进行无用图片的删除
        if (doc.select("img").size() == 0 && !tag.isEmpty()) {
            // 删除缓存中记录的图片
            deleteImg(tag, null);
        }

        // 获取所有img，并提取出图片
        List<String> imgs = doc.select("img").eachAttr("src").stream()
                .map(s -> s.split("/")).map(arr -> arr[arr.length - 1]).collect(Collectors.toList());
        // 删除被删除帖子的关联图片
        if ("" == tag) {
            for (String pic : imgs) {
                // 删除图片
                new File(PicController.savePath, pic).delete();
            }
            return;
        }
        // 删除冗余图片
        deleteImg(tag, imgs);
    }

    private void deleteImg(String tag, List<String> imgs) {
        Set<String> redisImg = stringRedisTemplate.opsForSet().members(POST_IMG + tag);
        if (redisImg.size() == 0) {
            return;
        }
        if (null != imgs) {
//            redisImg = redisImg.stream().filter(s -> imgs.contains(s));
            redisImg.removeIf(s -> !imgs.contains(s));
        }
        for (String img : redisImg){
            // 引用为0的时候直接移除
            boolean remove = imageService.remove(new LambdaQueryWrapper<Image>().eq(Image::getName, img).eq(Image::getUsages, 1));
            if (remove) {
                // usage == 1
                new File(PicController.savePath, img).delete();
            } else {
                imageMapper.subtractUsage(img);
            }
        }
        stringRedisTemplate.delete(POST_IMG + tag);
    }

    public boolean isImageSimilarity(String cur, String save) {
        if (compareHash(cur, save) > 0.95) {
            return true;
        }
        return false;
    }
    /**
     * 获取图像的哈希值
     *
     * @param file 图像
     * @return 图像的哈希值
     */
    public String calculateHash(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = file.getBytes();
        byte[] hash = md.digest(bytes);

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 比较两个哈希值的相似度
     *
     * @param hash1 哈希值1
     * @param hash2 哈希值2
     * @return 相似度（0.0~1.0）
     */
    public static float compareHash(String hash1, String hash2) {
        // 哈希值比较的逻辑
        int distance = calculateHammingDistance(hash1, hash2);
        return 1 - (float) distance / 128;
    }

    private static byte[] getPixels(BufferedImage image) {
        // 获取图像的像素数组
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixelArray = new int[width * height];
        image.getRGB(0, 0, width, height, pixelArray, 0, width);

        byte[] pixels = new byte[width * height];
        for (int i = 0; i < width * height; i++) {
            int pixel = pixelArray[i];
            int r = (pixel & 0xff0000) >> 16;
            int g = (pixel & 0xff00) >> 8;
            int b = pixel & 0xff;
            pixels[i] = (byte) ((r + g + b) / 3);
        }
        return pixels;
    }

    private static String convertToHexString(byte[] hashBytes) {
        // 将字节数组转换为十六进制字符串
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hashBytes) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static int calculateHammingDistance(String hash1, String hash2) {
        // 计算汉明距离
        if (hash1.length() != hash2.length()) {
            throw new IllegalArgumentException("Hash lengths do not match.");
        }
        int distance = 0;
        for (int i = 0; i < hash1.length(); i++) {
            if (hash1.charAt(i) != hash2.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }
}

package com.example.newbst.utils;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.net.URLConnection;

/**
 * created by Xu on 2023/8/21 11:01.
 */
@Slf4j
public class MapUtils {
    private static final String KEY = "0d4d0855365909fe9cd206d2eded48c2";

    /*public static Map getLonAndLat(String address, String key) {
        // 返回输入地址address的经纬度信息, 格式是 经度,纬度
        String queryUrl = "http://restapi.amap.com/v3/geocode/geo?key=" + key + "&address=" + address;
        // 高德接口返回的是JSON格式的字符串
        String queryResult = getResponse(queryUrl);
        Map<String, String> map = new HashMap<String, String>();
        JSONObject obj = JSONUtil.parseObj(queryResult);
        if (obj.get("status").toString().equals("1")) {
            JSONObject jobJSON = JSONObject.parseObject(obj.get("geocodes").toString().substring(1, obj.get("geocodes").toString().length() - 1));
            String location = jobJSON.get("location").toString();
            System.out.println("经纬度：" + location);
            String[] lonAndLat = location.split(",");
            if (lonAndLat != null && lonAndLat.length == 2) {
                map.put("lng", lonAndLat[0]);
                map.put("lat", lonAndLat[1]);
            }
            System.out.println(map);
            return map;
        } else {
            throw new RuntimeException("地址转换经纬度失败，错误码：" + obj.get("infocode"));
        }
    }*/

    private static String getResponse(String serverUrl) {
        // 用JAVA发起http请求，并返回json格式的结果
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(serverUrl);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static String getCityCodeByIp(String ipAddress) throws Exception {
        JSONObject json = null;
        String adCode = "";

            json = readJsonFromUrl("http://restapi.amap.com/v3/ip?ip=" + ipAddress + "&key=" + KEY + "");
            if ("0".equals(json.get("status"))) {      //调用异常时，抛出返回信息，以便分析（我会在错误层统一捕获并输出到error日志）
                throw new Exception("调用高德返回异常: " + json.toString());
            }

            adCode = json.getStr("adcode");          //获取ip定位的城市编码（高德返回json如下，需要其他也可自行获取）
        return adCode;
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {

        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = JSONUtil.parseObj(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}

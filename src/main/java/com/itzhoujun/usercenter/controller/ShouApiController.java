package com.itzhoujun.usercenter.controller;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ShouApiController {


    public static String get_time() {//设置时间
        Date d = new Date();//创建日期对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//创建格式化日期
        String s = sdf.format(d);//创建成字符串
        return s;
    }

    public static String result(String ISBN) {
        //接口地址
        String requestUrl = "https://oapi.shb.ltd/service/auth/get_access_token";
        //params用于存储请求数据的参数
        Map params = new HashMap();
        // pure java
        //int time = (int) (System.currentTimeMillis() / 1000);
        //请求数据
        String result = "";
        String  param = "";
        String verCode = "cdb38c08e6d0d97608f7d6b5d0b0081fc86146b361f2fb8f"+"_"+"1584925258";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(verCode.getBytes());//update处理
            byte b[] = md.digest();
            int i;//定义整型
            //声明StringBuffer对象
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];//将首个元素赋值给i
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");//前面补0
                buf.append(Integer.toHexString(i));//转换成16进制编码
            }
            result = buf.toString();//转换成字符串
            System.out.println(result);//输出32位16进制字符串
            params.put("appKey", "c");
            params.put("timestamp", 1584925258);
            params.put("verifyCode", result);
            JSONObject json = new JSONObject();
            json.put("appKey", "shbbhy925u8bpptfs7");
            json.put("timestamp", 1584925258);
            json.put("verifyCode", result);
            System.out.println(params);
            System.out.println(json);
            param= JSON.toJSONString(params);
            System.out.println(param);
            result = json.toString();
        }
        catch(Exception e){
            System.out.println("Wrong!");
        }
        //调用httpRequest方法，这个方法主要用于请求地址，并加上请求参数
        String l = httpRequest(requestUrl, result);
        String s = httpRequest(requestUrl, param);
        return l;
}

    private static String httpRequest(String requestUrl, String param) {
        //buffer用于接受返回的json数据

        StringBuffer buffer = new StringBuffer();
        try {
            //建立URL，把请求地址给补全，其中urlencode（）方法用于把params里的参数给取出来
            URL url = new URL(requestUrl+"?"+param);

            System.out.println(url);
            //打开http连接
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();//连接
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.connect();

            //获得输入
            InputStream inputStream = httpUrlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");//编码
            BufferedReader bufferedReader =  new BufferedReader(inputStreamReader);


            //将bufferReader的值给放到str里
            String str = null;
            while((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }


            //关闭bufferReader和输入流
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            // inputStream = null;

            //断开连接
            httpUrlConnection.disconnect();

        } catch(Exception ex) {
            ex.printStackTrace();
        }
        //返回字符串

        return buffer.toString();
    }


//    public static String urlencode() {
//
//        //将map里的参数变成像 showapi_appid=###&showapi_sign=###&的样子
//        StringBuilder sb = new StringBuilder();
//        for(Map.Entry i : data.entrySet()) {
//            try {
//                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
//            } catch (UnsupportedEncodingException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return sb.toString();
//    }
}







package com.hxx.sbweb.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class UploadUtil {
	/**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param url Service net address
     * @param params text content
     * @param files pictures
     * @return String result of Service response
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params, Map<String, File> files)
            throws IOException {
        String BOUNDARY = UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";


        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(10 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);


        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
            //sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            //sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }


        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        // 发送文件数据
        if (files != null){
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1.append("Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
                        + file.getValue().getName() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());


                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                try {
                	while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
				} catch (Exception e) {
//					e.getMessage();
				}finally {
					is.close();
				}
                outStream.write(LINEND.getBytes());
            }

        }
        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();

        String result="";
        try{
            // 得到响应码
            int res = conn.getResponseCode();
            InputStream in = conn.getInputStream();

            BufferedReader br=new BufferedReader(new InputStreamReader(in));
            //定义String类型用于储存单行数据
            String line=null;
            //创建StringBuffer对象用于存储所有数据
            StringBuffer sb1=new StringBuffer();
            if (res == 200) {
                while((line=br.readLine())!=null){
                    sb1.append(line);
                }
            }
            result= sb1.toString();
            in.close();
            br.close();
            outStream.close();
            conn.disconnect();
        }
        catch(Exception e){
            result=e.toString();
        }
        return result;
    }

    public static String makeSignature(String timestamp, String nonce, String token){
        //System.out.println("token:"+getToken()+"\ntimestamp:"+timestamp+"\nnonce:"+nonce);
        String[] arr = new String[]{token, timestamp, nonce};
        // 将 token, timestamp, nonce 三个参数进行字典排序
        //System.out.println("排序前的字符串数组："+arr.toString());
        Arrays.sort(arr);
        //System.out.println("排序后的字符串数组："+arr.toString());
        StringBuilder content = new StringBuilder();
        for(int i = 0; i < arr.length; i++){
            content.append(arr[i]);
        }
        // System.out.println("拼接后的字符串："+content.toString());
        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行 shal 加密
            // System.out.println("加密前转换成字节数组前的字符串："+content.toString());
            byte[] digest = md.digest(content.toString().getBytes());
            //System.out.println("加密后的字节数组转换成的字符串："+digest.toString());
            tmpStr = byteToStr(digest);
            //System.out.println("加密后的字符串"+tmpStr);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
        }
        content = null;
        // 将sha1加密后的字符串可与signature对比，标识该请求来源
        return tmpStr;
    }

    private static String byteToStr(byte[] digest) {
        // TODO Auto-generated method stub
        String strDigest = "";
        for(int i = 0; i < digest.length; i++){
            strDigest += byteToHexStr(digest[i]);
        }
        return strDigest;
    }

    private static String byteToHexStr(byte b) {
        // TODO Auto-generated method stub
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(b >>> 4) & 0X0F];
        tempArr[1] = Digit[b & 0X0F];

        String s = new String(tempArr);
        return s;
    }

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.substring(0,8)+uuid.substring(9,13)+uuid.substring(14,18)+uuid.substring(19,23)+uuid.substring(24);
        return uuid;
    }
}

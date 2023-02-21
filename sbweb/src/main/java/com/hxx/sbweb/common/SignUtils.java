package com.hxx.sbweb.common;

import com.hxx.sbweb.model.ApiCommonParam;

import java.security.MessageDigest;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;


public class SignUtils {
    /**
     * 构造签名
     * @param paramJson   ApiCommonParam对象转Json后的字符串
     * @param appSecret
     * @return
     */
    public static String buildCurrentSign(String paramJson, String appSecret){
        Map<String, String> commonMap = new HashMap<String, String>();
        Map<String,Object> jsonObject = JSONObject.parseObject(paramJson,new TypeReference<Map<String,Object>>(){});
        String value = "";
        List<String> keyList = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            value = entry.getValue().toString().trim();
            commonMap.put(entry.getKey(), value);
            if("sign".equalsIgnoreCase(entry.getKey())){
                continue;
            }
            keyList.add(entry.getKey());
        }
        // key排序
        Collections.sort(keyList,keyComparator);

        //拼接数据
        StringBuilder sb = new StringBuilder();
        for(String k:keyList){
            sb.append(commonMap.get(k)).append(":");
        }
        sb.append(appSecret);

        // 计算MD5
        String signStr = null;
        try {
            signStr = getMD5(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return signStr.toLowerCase();
    }

    /**
     * 得到MD5值
     * @param str
     * @return
     * @throws Exception
     */
    private static String getMD5(String str) throws Exception{
        if (str == null || str.length() == 0) {
            return null;
        }
        StringBuffer ret = new StringBuffer();
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes("UTF-8"));
            byte[] hash = digest.digest();
            // 转为ASCII码,byte为8位,计算机为32位,采用补码形式,所以要补全.
            for (int i = 0; i < hash.length; i++) {
                if ((0xFF & hash[i]) < 0x10) {
                    ret.append("0" + Integer.toHexString((hash[i] & 0xFF)));
                } else {
                    ret.append(Integer.toHexString(hash[i] & 0xFF));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return ret.toString().toUpperCase();
    }

    /**
     * 自定义比较器（比较字符串）
     */
    private static Comparator<String> keyComparator = new Comparator<String>(){
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    };

    public static void main(String[] args){
        ApiCommonParam apiParam = new ApiCommonParam();
        apiParam.setAppKey("appKey");//发携客云提供的appKey
        apiParam.setVersion("1.0");//接口版本
        apiParam.setOperateCompanyCode("12345678");//操作者所属公司编码
        apiParam.setOwnerCompanyCode("12345678");//数据所属公司编码,非集团公司默认赋值为操作公司
        apiParam.setTimestamps(System.currentTimeMillis()/1000);//当前时间对应的时间戳（秒数）
        apiParam.setReserver("");//扩展字段

        String appSecret = "appSecret";//携客云提供的appSecret

        String sign = buildCurrentSign(JSON.toJSONString(apiParam),appSecret);
        apiParam.setSign(sign);

        System.out.println("包含签名的请求头对象："+ JSON.toJSONString(apiParam));

    }

}

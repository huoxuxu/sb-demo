package com.hxx.sbweb.common;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 令牌工具类
 *
 * @author xiyunfei
 * @date 2021/01/22
 */
public class TokenUtil {

    /**
     * 解析令牌
     *
     * @param token 令牌(格式：设备编号#用户编号,有效期.校验位)
     * @return {@link Map<String, Object>}
     * @throws IOException    ioexception
     * @throws ParseException 解析异常
     */
    public static Map<String, Object> ParseToken(String token) throws IOException, ParseException {
        token = token.trim();
        int index = token.indexOf(".");
        if (index == -1) {
            return null;
        }
        String str = token.substring(0, index);
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        str = DigestUtil.fromBASE64toStr(str);
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        index = str.indexOf(",");
        // 前半部分为用户和设备信息（需要考虑部分token只是在设备验证完后生成的，并没有包括用户信息）
        String data = str.substring(0, index);
        String[] rList = data.split("#");
        String deviceCode = (rList[0] + "").toUpperCase();
        String userCode = (rList.length > 1) ? rList[1] + "" : "";
        // 后半部分为时间
        String dateStr = str.substring(index + 1);
        Date datetime = DateUtil.getDateFromStamp(dateStr);

        Map<String, Object> map = new HashMap<>();
        map.put("deviceCode", deviceCode);

        if (!StringUtils.isEmpty(userCode)) {
            map.put("userCode", userCode);
        } else {
            map.put("userCode", "");
        }
        if (datetime != null && !datetime.equals(new Date(0))) {
            map.put("expire", datetime);
        }
        return map;
    }

    /**
     * 创建token令牌（令牌格式 设备编号#用户编号,有效期.校验位）
     *
     * @param deviceCode 设备编号
     * @param userCode   用户编号
     * @param expire     有效期
     * @return {@link String}
     */
    public static String CreatorToken(String deviceCode, String userCode, int expire) {
        TokenProviderUtil provider = GetTokenProvider();
        // 拼接设备编号和用户编号
        String str = deviceCode + "#" + (userCode == null ? "" : userCode);
        // 处理时差
        Date dt = new Date();
        dt = DateUtil.addHour(dt, 8);
        // 创建token
        return provider.Encode(str, DateUtil.addHour(dt, expire));
    }

    private static TokenProviderUtil _tokenProviderUtil;

    /**
     * 获取令牌提供类
     *
     * @return {@link TokenProviderUtil}
     */
    public static TokenProviderUtil GetTokenProvider() {
        if (_tokenProviderUtil != null) {
            return _tokenProviderUtil;
        }
        TokenProviderUtil provider = new TokenProviderUtil();
        // 改为rsa加密，规避DSA 加密.NET与JAVA 需要额外的类库中转，简化加密流程
        if (!provider.ReadRSAKey()) {
            throw new NullPointerException("缺失私钥，无法创建令牌");
        }
        return _tokenProviderUtil = provider;
    }
}

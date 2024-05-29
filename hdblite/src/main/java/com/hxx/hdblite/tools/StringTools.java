package com.hxx.hdblite.tools;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

/**
 * 常用字符串操作
 */
public class StringTools {
    public static String Test() {
        "".toLowerCase();
        "".toUpperCase();
        "".isEmpty();//不能为null
        "".trim();
        "".indexOf("1");
        "".replaceAll("", "");//可以正常替换斜杠
        "".split("");//不能用
        //"".substring(0);//超长抛出异常

        //Split
        List<String> ls1 = StringTools.Split("12,,3,45,6,7", ",", true);
        List<String> ls2 = StringTools.Split("12,,3,45,6,7", ",", false);

        return "";
    }

    /**
     * 将类型，尝试转为指定类型
     * 只能处理简单类型，int，long，double
     *
     * @param val
     * @param targetCls
     * @param <T>
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T ChangeType(Object val, Class targetCls) throws Exception {
        if (val == null) throw new Exception("无法转换null值！");

        Class vcls = val.getClass();
        String vclsName = vcls.getName();
        String targetClsName = targetCls.getName();
        if (vclsName.equals(targetClsName)) return (T) val;

        //日期类型处理
        {
            //处理Timestamp与父类Date的问题(子转父)
            if (vclsName.equals("java.sql.Timestamp") && targetClsName.equals("java.util.Date")) {
                long l = ((Timestamp) val).getTime();
                val = new Date(l);
                return (T) val;
            }
            //Date转Timestamp问题（父转子）
            if (vclsName.equals("java.util.Date") && targetClsName.equals("java.sql.Timestamp")) {
                long l = ((Date) val).getTime();
                val = new Timestamp(l);
                return (T) val;
            }
        }

        String valStr = String.valueOf(val);//先转为字符串
        //转为指定类型
        if (targetCls == int.class) {
            val = Double.parseDouble(valStr);
            val = ((Double) val).intValue();
        } else if (targetCls == long.class) {
            val = Double.parseDouble(valStr);
            val = ((Double) val).longValue();
        } else if (targetCls == double.class) {
            val = Double.parseDouble(valStr);
        }
//        else if(sourceCls==java.util.Date.class){
//            //字符串转Date。默认标准字符串2000-01-01 10:11:01
//            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            val=sdf.parse(valStr);
//        }
        else {
            throw new Exception(String.format("没有设置映射关系[%s=>%s]", vclsName, targetClsName));
        }

        return (T) val;
    }

    /**
     * 内部是MessageFormat.format
     * 注意：单引号转义{、}，两个单引号表示一个'
     *
     * @param str
     * @param arguments
     * @return
     */
    public static String F(String str, Object... arguments) {
        return MessageFormat.format(str, arguments);
    }

    /**
     * 当前字符串是否为null或空字符串或空格换行等组成的字符串
     *
     * @param str
     * @return
     */
    public static Boolean IsNullOrEmpty(String str) {
        if (str == null) return true;

        return str.trim().isEmpty();
    }

    /**
     * 将数组转为字符串
     *
     * @param arr 集合，如果是数组，可以使用Arrays.asList
     * @param lzf 连字符
     * @return
     */
    public static <T> String Join(List<T> arr, String lzf) {
        StringJoiner roleJoiner = new StringJoiner(lzf);//需要的间隔符
        // Lambda 实现拼接
        arr.forEach(d -> roleJoiner.add(d == null ? "" : d + ""));
        return roleJoiner.toString();

//
//        StringBuilder sb = new StringBuilder();
//
//        int cou = arr.size();
//        if (cou == 0) return "";
//
//        for (int i = 0; i < cou; i++) {
//            Object item = arr.get(i);
//            if (item == null) {
//                item = "";
//            }
//
//            sb.append(item);
//
//            if (i != cou - 1) {
//                sb.append(lzf);
//            }
//        }
//
//        return sb.toString();
    }

    /**
     * @param str
     * @param startIndex 开始索引
     * @param getCount   取字符个数
     * @return
     */
    public static String Substring(String str, int startIndex, int getCount) {
        if (getCount < 1) return str.substring(startIndex);

        //0123456
        int endIndex = startIndex + getCount;
        return str.substring(startIndex, endIndex);
    }

    public static String SubStr(String str, int startIndex) {
        return Substring(str, startIndex, 0);
    }

    /**
     * 截取指定长度的字符串，超长返回全部
     *
     * @param str
     * @param getCount
     * @return
     */
    public static String Cut(String str, int getCount) {
        if (getCount >= str.length()) return str;

        return Cut(str, 0, getCount);
    }

    /**
     * @param str
     * @param startIndex
     * @param getCount
     * @return
     */
    public static String Cut(String str, int startIndex, int getCount) {
        int zcount = str.length();
        int sycount = zcount - startIndex;
        if (sycount <= 0) return "";

        getCount = Math.min(getCount, sycount);

        return Substring(str, startIndex, getCount);
    }

    /**
     * 是否包含（忽略大小写）
     *
     * @param source 源
     * @param subStr 子串
     * @return
     */
    public static Boolean ContainsIgnoreCase(String source, String subStr) {
        return source.toLowerCase().contains(subStr.toLowerCase());
    }

    public static Boolean EqualsIgnoreCase(String str1, String str2) {
        if (str1 == null || str2 == null) return false;

        return str1.toLowerCase().equals(str2.toLowerCase());
    }

    /**
     * 去除字符串前的指定子串
     *
     * @param str
     * @param trimStr
     * @return
     */
    public static String TrimStart(String str, String trimStr) {
        if (str.startsWith(trimStr)) {
            String str1 = str.substring(trimStr.length());

            return TrimStart(str1, trimStr);
        } else {
            return str;
        }
    }

    public static String TrimEnd(String str, String trimStr) {
        //0123456
        int count = str.length();

        if (str.endsWith(trimStr)) {
            int c1 = count - trimStr.length();
            String str1 = str.substring(0, c1);

            return TrimEnd(str1, trimStr);
        } else {
            return str;
        }
    }

    public static List<String> Split(String str, String zwf, boolean removeEmpty) {
        List<String> data = new ArrayList<>();

        if (zwf.length() == 0) return data;

        int ind = str.indexOf(zwf);
        if (ind == -1) {
            data.add(str);
            return data;
        }

        int len = str.length();
        int si = 0;
        while (true) {
            if (si >= len) return data;

            ind = str.indexOf(zwf, si);
            String str1 = null;
            if (ind == -1) {
                //解析到末尾
                str1 = str.substring(si);
            } else
                str1 = str.substring(si, ind);
            if (removeEmpty) {
                if (!IsNullOrEmpty(str1)) {
                    data.add(str1);
                }
            } else {
                data.add(str1);
            }

            si += str1.length() + 1;
        }
    }


}

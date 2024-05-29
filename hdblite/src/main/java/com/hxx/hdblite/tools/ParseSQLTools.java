package com.hxx.hdblite.tools;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseSQLTools {

    /**
     * 将sqlF转为sql和参数化值得集合
     *
     * @param sqlF  SELECT * from T1Meta Where ID={0} and Name={1} and Name2={0} and Name3 in ({2})
     * @param args1 1,2,[3,4,5]
     * @return SELECT * from T1Meta Where ID=? and Name=? and Name2=? and Name3 in (?,?,?)
     * [1,2,1,3,4,5]
     */
    public static Pair<String, ArrayList> ParseSQLFormat(String sqlF, Object... args1) {
        //SELECT * from T1Meta Where ID={0} and Name={1} and Name2={0} and Name3 in ({2})
        //SELECT * from T1Meta Where ID=? and Name=? and Name2=? and Name3 in (?,?,?,?)   0,1,0,4,5,6,7,

        int zcou = args1.length;
        ArrayList valls = new ArrayList();

        String regex = "\\{\\d+}";//匹配第一个{0}
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(regex);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(sqlF);
        while (m.find()) {
            String regStr = m.group();
            int ind = parseInt(regStr);
            if (ind < zcou) {
                Object val = args1[ind];
                //如果val类型是ArrayList，那么当前就是in
                if (val.getClass().isArray()) {
                    Object[] objVal = (Object[]) val;
                    int osize = objVal.length;
                    ArrayList oarr = new ArrayList(osize);
                    for (int o1 = 0; o1 < osize; o1++) {
                        oarr.add("?");
                    }
                    String oarJ = StringTools.Join(oarr, ",");
                    sqlF = sqlF.replaceFirst("\\{" + regStr.substring(1), oarJ);//?,?,?

                    for (Object o2 : objVal) {
                        valls.add(o2);
                    }
                } else {
                    sqlF = sqlF.replaceFirst("\\{" + regStr.substring(1), "?");
                    valls.add(val);
                }
            }
        }

        return new Pair(sqlF, valls);
    }

    /**
     * 字符串转数字
     *
     * @param str {0}
     * @return
     */
    private static Integer parseInt(String str) {
        //判断匹配到的第一个字符{100}
        str = str.replaceAll("\\{", "");
        str = str.replaceAll("}", "");

        return Integer.parseInt(str);
    }


}

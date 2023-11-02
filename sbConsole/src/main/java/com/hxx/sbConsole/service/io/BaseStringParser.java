package com.hxx.sbConsole.service.io;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-11-01 9:35:11
 **/
@var
@Slf4j
public class BaseStringParser {

    public static void main(String[] args) {
        var str = "['result']['indicatorMonitorDetailService.getSmallGivenSceneDetail']['result']['data'][*]";
        str = "['result\\'s']";
        /*
         * 规则：
         * ['XX']表示一个对象节点
         * [1]或[*]或[1,2,3]表示取数组节点的指定数据
         * */

        // 循环一遍得到所有的节点
        // 排除数组索引操作后组合节点
        /*
        * ['a']、[*]、['b']、[1]、['c']、[1,2,3]
        * 得到：
        * ['a'] all
        * ['a']['b'] index-[1]
        * ['a']['b']['c'] index-[1,2,3]
        * */
        /*
         * ['a']['a1']、[*]、['b']、[1]、['c']、[1,2,3]
         * 得到：
         * ['a']['a1'] all
         * ['a']['a1']['b'] index-[1]
         * ['a']['a1']['b']['c'] index-[1,2,3]
         * */


    }

    static void p1(String str) throws IOException {
        StringReader reader = new StringReader(str);
        while (true) {
            int ref = reader.read();
            if (ref == -1) break;
            char c = (char) ref;

        }
    }

    @Data
    public static class PRModel {
        // 当前的层级节点
        private String Node;
        // 当前节点
        private String CurrentNode;
        // 索引集合
        private String NodeArrayIndex;
    }
}

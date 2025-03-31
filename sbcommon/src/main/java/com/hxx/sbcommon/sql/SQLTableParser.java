package com.hxx.sbcommon.sql;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 问题：
 * 不支持sql文本中包含 FROM|JOIN|UPDATE|INTO 表名
 */
public class SQLTableParser {

    public static List<String> extractTableNames(String sql) {
        Set<String> tableNames = new HashSet<>();
        String regex = "(?i)(?:FROM|JOIN|UPDATE|INTO)\\s+([a-zA-Z0-9_]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sql);

        while (matcher.find()) {
            tableNames.add(matcher.group(1));
        }
        return new ArrayList<>(tableNames);
    }

    public static String replaceTableNames(String sql, Map<String, String> replacementMap) {
        List<String> tableNames = extractTableNames(sql);
        StringBuilder result = new StringBuilder(sql);
        for (String tableName : tableNames) {
            if (replacementMap.containsKey(tableName)) {
                String replacement = replacementMap.get(tableName);
                String keywordPattern = "(?i)(FROM|JOIN|UPDATE|INTO)\\s+";
                Pattern keywordRegex = Pattern.compile(keywordPattern);
                Matcher keywordMatcher = keywordRegex.matcher(result);
                while (keywordMatcher.find()) {
                    int startIndex = keywordMatcher.end();
                    if (result.substring(startIndex).startsWith(tableName)) {
                        result.replace(startIndex, startIndex + tableName.length(), replacement);
                    }
                }
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String sql = "select * from t1 where id=1;\n " +
                "update t1 set name='xx' where id=1;\n " +
                "select * from t3 " +
                "inner join t1 on t3.id=t1.the_t3 " +
                "where gmt_create>='2025-03-30' and gmt_create<='2025-03-31' and name='t1' " +
                "limit 0,10;\n";

        Map<String, String> replacementMap = new HashMap<>();
        replacementMap.put("t1", "tt1");
        replacementMap.put("t3", "t33");

        String newSql = replaceTableNames(sql, replacementMap);
        System.out.println("替换表名后的 SQL 语句：\n" + newSql);
    }
}

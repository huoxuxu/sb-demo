package com.hxx.sbcommon.sql;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLTableParser {

    public static List<String> extractTableNames(String sql) {
        Set<String> tableNames = new HashSet<>();
        String regex = "(?i)(?:FROM|JOIN|UPDATE)\\s+([a-zA-Z0-9_]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sql);

        while (matcher.find()) {
            tableNames.add(matcher.group(1));
        }
        return new ArrayList<>(tableNames);
    }

    public static String replaceTableNames(String sql, Map<String, String> replacementMap) {
        // 提取并替换字符串常量
        Map<Integer, String> stringConstants = new HashMap<>();
        String sanitizedSql = sanitizeSql(sql, stringConstants);

        List<String> tableNames = extractTableNames(sanitizedSql);
        StringBuilder result = new StringBuilder(sanitizedSql);
        for (String tableName : tableNames) {
            if (replacementMap.containsKey(tableName)) {
                String replacement = replacementMap.get(tableName);
                String keywordPattern = "(?i)(FROM|JOIN|UPDATE)\\s+";
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

        // 恢复字符串常量
        return restoreSql(result.toString(), stringConstants);
    }

    private static String sanitizeSql(String sql, Map<Integer, String> stringConstants) {
        StringBuilder sanitized = new StringBuilder();
        int index = 0;
        int constantIndex = 0;
        boolean inString = false;
        while (index < sql.length()) {
            char c = sql.charAt(index);
            if (c == '\'') {
                inString = !inString;
                if (!inString) {
                    int start = sanitized.length();
                    sanitized.append("$$" + constantIndex + "$$");
                    stringConstants.put(constantIndex, sql.substring(sanitized.length() - (index - start), index + 1));
                    constantIndex++;
                }
            }
            if (!inString) {
                sanitized.append(c);
            }
            index++;
        }
        return sanitized.toString();
    }

    private static String restoreSql(String sanitizedSql, Map<Integer, String> stringConstants) {
        StringBuilder restored = new StringBuilder(sanitizedSql);
        for (Map.Entry<Integer, String> entry : stringConstants.entrySet()) {
            int key = entry.getKey();
            String value = entry.getValue();
            String placeholder = "$$" + key + "$$";
            int index = restored.indexOf(placeholder);
            while (index != -1) {
                restored.replace(index, index + placeholder.length(), value);
                index = restored.indexOf(placeholder, index + value.length());
            }
        }
        return restored.toString();
    }

    public static void main(String[] args) {
        String sql =
//                "select * from t1 where id=1; " +
//                "update t1 set name='xx from t1' where id=1; " +
                "select * from t3 " +
                "inner join t1 on t3.id=t1.the_t3 " +
                "where gmt_create>='2025-03-30' and gmt_create<='2025-03-31' and name='from t1' " +
                "limit 0,10;";

        Map<String, String> replacementMap = new HashMap<>();
        replacementMap.put("t1", "tt1");
        replacementMap.put("t3", "t33");

        String newSql = replaceTableNames(sql, replacementMap);
        System.out.println("替换表名后的 SQL 语句：\n" + newSql);
    }
}

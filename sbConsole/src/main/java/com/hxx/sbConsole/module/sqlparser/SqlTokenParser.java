package com.hxx.sbConsole.module.sqlparser;

import java.util.ArrayList;
import java.util.List;

public class SqlTokenParser {

    private static final String[] SQL_KEYWORDS = {
            "SELECT", "FROM", "WHERE", "JOIN", "INNER JOIN", "LEFT JOIN", "RIGHT JOIN", "FULL JOIN",
            "INSERT", "INTO", "VALUES", "UPDATE", "SET", "DELETE", "CREATE", "TABLE", "VIEW", "LIKE"
    };

    public static List<String> parseSQL(String sql) {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        sql = sql.toUpperCase();
        for (int i = 0; i < sql.length(); i++) {
            char c = sql.charAt(i);
            if (Character.isWhitespace(c)) {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
            } else if (c == ',' || c == ';' || c == '(' || c == ')') {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                tokens.add(String.valueOf(c));
            } else {
                token.append(c);
            }
        }
        if (token.length() > 0) {
            tokens.add(token.toString());
        }
        return tokens;
    }

    public static void main(String[] args) {
        String sql = "SELECT column1, column2 FROM table_name WHERE txt=len('update t1');";
        List<String> tokens = parseSQL(sql);
        for (String token : tokens) {
            System.out.println(token);
        }
    }
}

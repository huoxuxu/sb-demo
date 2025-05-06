package com.hxx.sbConsole.service.io;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderReplacerWithStateMachine {

    public static void main(String[] args) {
        try {
            // Hello, John! Your ID is 123.123456
            String input = "Hello, ${name}! Your ID is ${id}.$${id}456";
            Map<String, String> replacements = new HashMap<>();
            replacements.put("name", "John");
            replacements.put("id", "123");

            String output = replacePlaceholders(input, replacements);
            System.out.println(output);

        } catch (Exception ex) {
            System.out.println(ex + "");
        }
    }

    /**
     * 占位符替换
     * 示例：Hello ${code} {"code":"World"}
     * 输出：Hello World
     * 示例：Hello ${code} $${code} {"code":"World"}
     * 输出：Hello World ${code}
     *
     * @param input
     * @param replacements
     * @return
     */
    public static String replacePlaceholders(String input, Map<String, String> replacements) {
        StringBuilder result = new StringBuilder();
        State state = State.NORMAL;
        StringBuilder placeholder = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (state) {
                case NORMAL:
                    if (c == '$') {
                        state = State.DOLLAR;
                    } else {
                        result.append(c);
                    }
                    break;
                case DOLLAR:
                    if (c == '$') {
                        result.append('$');
                        state = State.NORMAL;
                    } else if (c == '{') {
                        state = State.PLACEHOLDER_START;
                    } else {
                        result.append('$').append(c);
                        state = State.NORMAL;
                    }
                    break;
                case PLACEHOLDER_START:
                    if (c == '}') {
                        String key = placeholder.toString();
                        String replacement = replacements.get(key);
                        if (replacement != null) {
                            result.append(replacement);
                        } else {
                            result.append("${").append(key).append("}");
                        }
                        placeholder.setLength(0);
                        state = State.NORMAL;
                    } else {
                        placeholder.append(c);
                    }
                    break;
            }
        }

        if (state != State.NORMAL) {
            result.append("${").append(placeholder).append("}");
        }

        return result.toString();
    }

    private enum State {
        NORMAL,
        // 遇到 $ 符号后的状态
        DOLLAR,
        // 遇到 ${ 后的状态
        PLACEHOLDER_START
    }
}

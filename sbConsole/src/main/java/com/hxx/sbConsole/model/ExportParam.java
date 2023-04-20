package com.hxx.sbConsole.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-04-20 8:59:52
 **/
@lombok.Data
public class ExportParam implements Serializable {
    private static final long serialVersionUID = 5057602270895064220L;

    private String requestId;

    private String bizType;
    private String category;
    private String source;

    private Map<String, String> extra;
    private String strategy;

    private SearchParam search;

    @lombok.Data
    public static class SearchParam implements Serializable {
        private static final long serialVersionUID = 8467370164759091448L;

        private long bizId;
        private long startTime;
        private long endTime;
        private List<Condition> conditions;
    }

    @lombok.Data
    public static class Condition implements Serializable{
        private static final long serialVersionUID = -3011854505282260389L;

        private String left;
        private Op op;
        private Object right;

        public Condition(String left, Op op, Object right) {
            this.left = left;
            this.op = op;
            this.right = right;
        }

    }

    public enum Op{
        range, in, match, eq

    }

    @lombok.Data
    public static class Range implements Serializable{
        private long x;
        private long y;

        public Range(long x, long y) {
            this.x = x;
            this.y = y;
        }
    }

    @lombok.Data
    public static class Match implements Serializable{
        private String code;
        private String name;

        public Match(String code, String name) {
            this.code = code;
            this.name = name;
        }
    }
}

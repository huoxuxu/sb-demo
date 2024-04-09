package com.hxx.sbConsole.service.impl.demo.layout.model;

import lombok.Data;

public class LayoutHtml {

    @Data
    public static class LayoutMargin {

        private int top;
        private int bottom;
        private int left;
        private int right;

    }

    @Data
    public static class LayoutBorder {

        private int width;
        private int color;
        private int style;
        private int radius;

    }

    @Data
    public static class LayoutBackground {

        private String image;
        private int color;

    }

    @Data
    public static class LayoutFont {

        private String family;
        private int size;
        private int color;

    }

}

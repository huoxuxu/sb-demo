package com.hxx.sbConsole.service.impl.demo.layout.model;

import lombok.Data;

@Data
public class Size {
    private int width;
    private int height;

    public Size() {
    }

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }
}

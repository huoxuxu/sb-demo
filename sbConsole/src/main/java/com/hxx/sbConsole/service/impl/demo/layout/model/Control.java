package com.hxx.sbConsole.service.impl.demo.layout.model;

import com.hxx.sbConsole.service.impl.demo.layout.FlexVerticalDemoService;
import lombok.Data;

@Data
public class Control {
    private String name;

    private Point location;
    private Size size;

    public Control() {
    }

    public Control(Point location, Size size) {
        this.location = location;
        this.size = size;
    }

    public Control(Size size) {
        this.size = size;
        this.location = new Point(0, 0);
    }
}

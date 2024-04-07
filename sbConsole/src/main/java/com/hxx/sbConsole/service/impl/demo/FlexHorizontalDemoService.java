package com.hxx.sbConsole.service.impl.demo;

import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import lombok.Data;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlexHorizontalDemoService {

    public static void main(String[] args) {
        try {
            Control panel = new Control(new Point(0, 0), new Size(420, 220));
            Control pn1 = new Control(new Size(50, 50));
            Control pn2 = new Control(new Size(150, 50));
            Control pn3 = new Control(new Size(150, 50));
            Control pn4 = new Control(new Size(50, 50));

            Control pn5 = new Control(new Size(50, 50));
            Control pn6 = new Control(new Size(150, 50));
            Control pn7 = new Control(new Size(50, 50));

            horizontal(panel, pn1, pn2, pn3, pn4, pn5, pn6, pn7);


        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
        System.out.println("ok!");
    }

    static void horizontal(Control panel, Control... children) {
        /*
         * 1.计算行列个数
         * 2.计算每行的基线坐标
         * 3.计算首元素位置
         * 4.后续元素按首元素基线排列
         * */
        Size panelSize = panel.getSize();
        // 多行的集合
        List<List<Control>> rows = procRows(panel, children);
        List<ControlLayout> rowLayouts = rows.stream()
                .map(d -> new ControlLayout(d, null))
                .collect(Collectors.toList());
        // 计算每行的基线坐标
        {
            // 处理高度
            procHeight(rows, panelSize, rowLayouts);
            // 处理宽度
            procWidth(rows, panelSize, rowLayouts);
        }
        // 计算首元素位置
        for (int i = 0; i < rowLayouts.size(); i++) {
            ControlLayout rowLayout = rowLayouts.get(i);
            List<Control> rowControls = rowLayout.getRowControls();
            Control control = rowControls.get(0);
            // 处理x
            {
                int x = rowLayout.getBaseline().getX();
                control.getLocation().setX(x);
            }
            // 处理y
            {
                int y = rowLayout.getBaseline().getY() - (rowLayout.getHeightMax()) / 2;
                control.getLocation().setY(y);
            }
        }

        System.out.println(JsonUtil.toJSON(rowLayouts));
    }

    private static void procWidth(List<List<Control>> rows, Size panelSize, List<ControlLayout> rowLayouts) {
        for (int i = 0; i < rows.size(); i++) {
            List<Control> row = rows.get(i);
            ControlLayout controlLayout = rowLayouts.get(i);
            // 获取当前行总宽度
            int allWidth = row.stream().mapToInt(d -> d.getSize().getWidth()).sum();
            int gapWidth = 0;
            // 子元素行总宽度小于panel宽度
            if (allWidth < panelSize.getWidth()) {
                gapWidth = (panelSize.getWidth() - allWidth) / (row.size() + 1);
            }
            // 开始堆叠元素
            int rowWidth = row.get(0).getSize().getWidth();
            int x = gapWidth;
            if (controlLayout.getBaseline() == null) {
                controlLayout.setBaseline(new Point(x, 0));
            } else {
                controlLayout.getBaseline().setX(x);
            }
            controlLayout.setGapWidth(gapWidth);
        }
    }

    private static void procHeight(List<List<Control>> rows, Size panelSize, List<ControlLayout> rowLayouts) {
        List<Integer> rowHeights = new ArrayList<>();
        for (List<Control> row : rows) {
            // 行中最大
            int rowHeightMax = row.stream().mapToInt(d -> d.getSize().getHeight()).max().getAsInt();
            rowHeights.add(rowHeightMax);
        }
        // 适配panel
        int allHeight = rowHeights.stream().mapToInt(d -> d).sum();
        int gapHeight = 0;
        // 子元素行总高度小于panel高度
        if (allHeight < panelSize.getHeight()) {
            gapHeight = (panelSize.getHeight() - allHeight) / (rows.size() + 1);
        }
        // 开始堆叠元素
        int preHeight = 0;
        for (int i = 0; i < rows.size(); i++) {
            Integer rowHeight = rowHeights.get(i);
            ControlLayout controlLayout = rowLayouts.get(i);
            int y = gapHeight + preHeight + rowHeight / 2;
            if (controlLayout.getBaseline() == null) {
                controlLayout.setBaseline(new Point(0, y));
            } else {
                controlLayout.getBaseline().setY(y);
            }
            controlLayout.setHeightMax(rowHeight);
            controlLayout.setGapHeight(gapHeight);
            preHeight += rowHeight;
        }
    }

    private static List<List<Control>> procRows(Control panel, Control[] children) {
        List<List<Control>> rows = new ArrayList<>();
        int currentRowWidth = 0;
        List<Control> currentRow = new ArrayList<>();
        // loop
        for (Control child : children) {
            int childWidth = child.getSize().getWidth();
            if (currentRowWidth + childWidth > panel.getSize().getWidth()) {
                // 当前行宽度超出限制，保存当前行并开始新的一行
                rows.add(currentRow);
                currentRow = new ArrayList<>();
                currentRowWidth = 0;
            }
            // 添加控件到当前行
            currentRow.add(child);
            // 更新当前行宽度
            currentRowWidth += childWidth;
        }

        // 处理剩余未满一行的控件
        if (!currentRow.isEmpty()) {
            rows.add(currentRow);
        }

        return rows;
    }

    @Data
    static class ControlLayout {
        private List<Control> rowControls;
        private Point baseline;

        //        // 当前行中最宽部分
//        private int widthMax;
        // 当前行中最高部分
        private int heightMax;

        // 横向排列的间隔宽度
        private int gapWidth;
        // 竖向排列的间隔高度
        private int gapHeight;

        public ControlLayout(List<Control> rowControls, Point baseline) {
            this.rowControls = rowControls;
            this.baseline = baseline;
        }
    }

    @Data
    static class Control {
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

    @Data
    static class Point {
        private int x;
        private int y;

        public Point() {
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @Data
    static class Size {
        private int width;
        private int height;

        public Size() {
        }

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}

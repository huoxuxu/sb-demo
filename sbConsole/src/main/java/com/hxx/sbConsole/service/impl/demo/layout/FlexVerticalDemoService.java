package com.hxx.sbConsole.service.impl.demo.layout;

import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import lombok.Data;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlexVerticalDemoService {

    public static void main(String[] args) {
        try {
            {
                System.out.println("多Control垂直居中");
                Control panel = new Control(new Point(0, 0), new Size(500, 222));
                Control pn1 = new Control(new Size(50, 50));
                Control pn2 = new Control(new Size(150, 50));

                Control pn3 = new Control(new Size(150, 150));
                Control pn4 = new Control(new Size(50, 50));

                Control pn5 = new Control(new Size(50, 50));
                Control pn6 = new Control(new Size(150, 50));
                Control pn7 = new Control(new Size(50, 50));

                vertical(panel, pn1, pn2, pn3, pn4, pn5, pn6, pn7);
            }
            {
                System.out.println("单Control垂直居中");
                Control panel = new Control(new Point(0, 0), new Size(500, 222));
                Control pn1 = new Control(new Size(50, 50));

                vertical(panel, pn1);
            }


        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
        System.out.println("ok!");
    }

    static void vertical(Control panel, Control... children) {
        /*
         * 1.计算行列个数
         * 2.计算每行的基线坐标
         * 3.计算首元素位置
         * 4.后续元素按首元素基线排列
         * */
        Size panelSize = panel.getSize();
        // 多列的集合
        List<List<Control>> cols = procCols(panel, children);
        List<ControlLayout> colLayouts = cols.stream()
                .map(d -> new ControlLayout(d, null))
                .collect(Collectors.toList());

        procControlAxis(panelSize, colLayouts);

        System.out.println(JsonUtil.toJSON(colLayouts));
    }

    private static void procControlAxis(Size panelSize, List<ControlLayout> rowLayouts) {
        for (ControlLayout controlLayout : rowLayouts) {
            List<Control> row = controlLayout.getGroupControls();
            controlLayout.setHeightMax(-1);
            // 行中最大
            int rowWidthMax = row.stream().mapToInt(d -> d.getSize().getWidth()).max().getAsInt();
            controlLayout.setWidthMax(rowWidthMax);
        }
        // 适配panel
        int gapWidth = getGapWidth(panelSize.getWidth(), rowLayouts);
        // 基线X轴集合
        List<Integer> baseXAxises = new ArrayList<>();
        int preWidth1 = 0;
        for (int i = 0; i < rowLayouts.size(); i++) {
            ControlLayout controlLayout = rowLayouts.get(i);
            controlLayout.setGapWidth(gapWidth);
            int rowWidth = controlLayout.getWidthMax();
            int baseXAxis = preWidth1 + (i + 1) * gapWidth + rowWidth / 2;
            baseXAxises.add(baseXAxis);
            controlLayout.setBaseline(new Point(baseXAxis, 0));
            preWidth1 += rowWidth;
        }

        // 基线Y轴集合
        for (int i = 0; i < rowLayouts.size(); i++) {
            ControlLayout controlLayout = rowLayouts.get(i);
            List<Control> row = controlLayout.getGroupControls();
            Integer baseXAxis = baseXAxises.get(i);
            int widthMax = controlLayout.getWidthMax();
            // 适配panel
            int gapHeight = getGapHeight(panelSize.getHeight(), row);
            controlLayout.setGapHeight(gapHeight);
            int preHeight = 0;
            for (int j = 0; j < row.size(); j++) {
                Control rowItem = row.get(j);
                int widthCurrent = rowItem.getSize().getWidth();
                /*
                 * XAxis algorithm
                 * 0 baseXAxis - (widthMax / 2) + ((widthMax-widthCurrent)/2)
                 * ...
                 * x baseXAxis - (widthMax / 2) + ((widthMax-widthCurrent)/2)
                 */
                int rowXAxis = baseXAxis - (widthMax / 2) + ((widthMax - widthCurrent) / 2);
                rowItem.getLocation().setX(rowXAxis);
                /*
                 * algorithm
                 * 0 gapHeight
                 * 1 gapHeight+preHeight+gapHeight
                 * 2
                 * ...
                 * x (x+1)gapHeight+preHeight
                 * */
                int baseYAxis = (j + 1) * gapHeight + preHeight;
                rowItem.getLocation().setY(baseYAxis);
                // set preHeight
                preHeight += rowItem.getSize().getHeight();
            }
        }
    }

    private static int getGapWidth(int panelWidth, List<ControlLayout> rowLayouts) {
        int gapWidth = 0;
        int allWidth = rowLayouts.stream().mapToInt(d -> d.getWidthMax()).sum();
        // 子元素列总宽度小于panel宽度
        if (allWidth < panelWidth) {
            gapWidth = (panelWidth - allWidth) / (rowLayouts.size() + 1);
        }
        return gapWidth;
    }

    private static int getGapHeight(int panelHeight, List<Control> row) {
        int gapHeight = 0;
        int allHeight = row.stream().mapToInt(d -> d.getSize().getHeight()).sum();
        if (allHeight < panelHeight) {
            gapHeight = (panelHeight - allHeight) / (row.size() + 1);
        }
        return gapHeight;
    }

    private static List<List<Control>> procCols(Control panel, Control[] children) {
        List<List<Control>> cols = new ArrayList<>();
        int currentRowHeight = 0;
        int panelHeight = panel.getSize().getHeight();
        List<Control> col = new ArrayList<>();
        // loop
        for (Control child : children) {
            int childHeight = child.getSize().getHeight();
            if (currentRowHeight + childHeight > panelHeight) {
                // 当前高度超出限制，保存当前行并开始新的一行
                cols.add(col);
                col = new ArrayList<>();
                currentRowHeight = 0;
            }
            // 添加控件到当前行
            col.add(child);
            // 更新当前行宽度
            currentRowHeight += childHeight;
        }

        // 处理剩余未满一行的控件
        if (!col.isEmpty()) {
            cols.add(col);
        }

        return cols;
    }

    @Data
    static class ControlLayout {
        private List<Control> groupControls;
        private Point baseline;

        // 当前行中最宽部分
        private int widthMax;
        // 当前行中最高部分
        private int heightMax;

        // 横向排列的间隔宽度
        private int gapWidth;
        // 竖向排列的间隔高度
        private int gapHeight;

        public ControlLayout(List<Control> groupControls, Point baseline) {
            this.groupControls = groupControls;
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

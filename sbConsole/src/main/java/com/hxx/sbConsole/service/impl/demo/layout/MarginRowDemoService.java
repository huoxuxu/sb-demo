package com.hxx.sbConsole.service.impl.demo.layout;

import com.hxx.sbConsole.service.impl.demo.layout.model.*;
import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import lombok.var;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Arrays;

/*
    # 上边距10px，左右自动居中，下边距无效
    margin:10px auto;
    # 上边距10px，左边距10px，右、下边距无效
    margin:10px 10px auto;
    # 上边距10px，右边距10px，左、下边距无效
    margin:10px 10px 0px auto;
*/
@var
public class MarginRowDemoService {

    public static void main(String[] args) {
        Control panel = new Control(new Point(0, 0), new Size(500, 222));
        Control pn1 = new Control(new Size(50, 50));
        Control pn2 = new Control(new Size(150, 50));

        Control pn3 = new Control(new Size(150, 150));
        Control pn4 = new Control(new Size(50, 50));

        try {
            LayoutHtml.LayoutMargin layoutMargin = new LayoutHtml.LayoutMargin();
            layoutMargin.setTop(6);
            layoutMargin.setLeft(5);

            {
                marginTopLeft(layoutMargin.getTop(), layoutMargin.getLeft(), pn1, pn2, pn3, pn4);
                System.out.println(JsonUtil.toJSON(Arrays.asList(pn1, pn2, pn3, pn4)));
            }
            {
                marginTopRight(panel, layoutMargin.getTop(), layoutMargin.getLeft(), pn1, pn2, pn3, pn4);
                System.out.println(JsonUtil.toJSON(Arrays.asList(pn1, pn2, pn3, pn4)));
            }
            {
                marginTopAuto(panel, layoutMargin.getTop(), pn1, pn2, pn3, pn4);
                System.out.println(JsonUtil.toJSON(Arrays.asList(pn1, pn2, pn3, pn4)));
            }
        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
        System.out.println("ok");
    }

    /**
     * 每个行元素都设置上边距，左右自动居中，下边距无效
     * margin:10px auto;
     * 上边距10px，左右自动居中，下边距无效
     *
     * @param marginTop
     * @param children
     */
    public static void marginTopAuto(Control panel, int marginTop, Control... children) {
        // 计算x轴
        var xAxis = panel.getSize().getWidth() / 2;
        var preHeight = 0;
        for (int i = 0; i < children.length; i++) {
            Control child = children[i];
            var yAxis = (i + 1) * marginTop + preHeight;
            var marginLeft = xAxis - child.getSize().getWidth() / 2;
            child.getLocation().setX(marginLeft);
            child.getLocation().setY(yAxis);

            preHeight += child.getSize().getHeight();
        }
    }

    /**
     * 上边距10px，左边距10px，右、下边距无效
     * margin:10px 10px auto;
     *
     * @param marginTop
     * @param marginLeft
     * @param children
     */
    public static void marginTopLeft(int marginTop, int marginLeft, Control... children) {
        var xAxis = marginLeft;
        var preHeight = 0;
        for (int i = 0; i < children.length; i++) {
            Control child = children[i];
            var yAxis = (i + 1) * marginTop + preHeight;
            child.getLocation().setX(xAxis);
            child.getLocation().setY(yAxis);
            preHeight += child.getSize().getHeight();
        }
    }

    /**
     * 上边距10px，左边距10px，右、下边距无效
     * margin-top: 10px;
     * margin-right: 10px;
     *
     * @param marginTop
     * @param marginRight
     * @param children
     */
    public static void marginTopRight(Control panel, int marginTop, int marginRight, Control... children) {
        int width = panel.getSize().getWidth();
        var preHeight = 0;
        for (int i = 0; i < children.length; i++) {
            Control child = children[i];
            var xAxis = width - child.getSize().getWidth() - marginRight;
            var yAxis = (i + 1) * marginTop + preHeight;
            child.getLocation().setX(xAxis);
            child.getLocation().setY(yAxis);
            preHeight += child.getSize().getHeight();
        }
    }

}

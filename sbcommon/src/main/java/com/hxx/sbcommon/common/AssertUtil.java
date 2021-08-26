package com.hxx.sbcommon.common;

import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-10 16:56:05
 **/
public class AssertUtil {
    public AssertUtil() {
    }

    public static void isNull(Object object) {
        Assert.isNull(object, "the object argument must be null");
    }

    public static void isNull(Object object, String message) {
        Assert.isNull(object, message);
    }

    public static void notNull(Object obj) {
        Assert.notNull(obj, "the object argument must be null");
    }

    public static void notNull(Object obj, String msg) {
        Assert.notNull(obj, msg);
    }

    public static void notEmpty(Object[] array) {
        Assert.notEmpty(array, "this array must not be empty: it must contain at least 1 element");
    }

    public static void notEmpty(Object[] array, String message) {
        Assert.notEmpty(array, message);
    }

    public static void notEmpty(Collection<?> collection) {
        Assert.notEmpty(collection, "this collection must not be empty: it must contain at least 1 element");
    }

    public static void notEmpty(Collection<?> collection, String message) {
        Assert.notEmpty(collection, message);
    }

    public static void notEmpty(Map<?, ?> map) {
        Assert.notEmpty(map, "this map must not be empty; it must contain at least one entry");
    }

    public static void notEmpty(Map<?, ?> map, String message) {
        Assert.notEmpty(map, message);
    }

    public static void isTrue(boolean expression) {
        Assert.isTrue(expression, "this expression must be true");
    }

    public static void isTrue(boolean expression, String msg) {
        Assert.isTrue(expression, msg);
    }

    public static void hasLength(String text) {
        Assert.hasLength(text, "this String argument must have length; it must not be null or empty");
    }

    public static void hasLength(String text, String message) {
        Assert.hasLength(text, message);
    }

    public static void hasText(String text) {
        Assert.hasText(text, "this String argument must have text; it must not be null, empty, or blank");
    }

    public static void hasText(String text, String message) {
        Assert.hasText(text, message);
    }

}

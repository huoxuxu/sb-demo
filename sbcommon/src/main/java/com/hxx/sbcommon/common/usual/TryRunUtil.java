package com.hxx.sbcommon.common.usual;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-10-13 17:11:34
 **/
@Slf4j
public class TryRunUtil {

    // 调用指定方法，并返回值
    public static <T, R> R exec(T t, Function<T, R> fun) {
        try {
            return fun.apply(t);
        } catch (Exception e) {
            log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    // 调用指定方法，并返回值
    public static <T, T1, R> R exec(T t, T1 t1, BiFunction<T, T1, R> fun) {
        try {
            return fun.apply(t, t1);
        } catch (Exception e) {
            log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    // 调用指定方法，并返回值
    public static <T, T1, T2, R> R exec(T t, T1 t1, T2 t2, BiFunction3<T, T1, T2, R> fun) {
        try {
            return fun.apply(t, t1, t2);
        } catch (Exception e) {
            log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    // 调用指定方法，并返回值
    public static <T, T1, T2, T3, R> R exec(T t, T1 t1, T2 t2, T3 t3, BiFunction4<T, T1, T2, T3, R> fun) {
        try {
            return fun.apply(t, t1, t2, t3);
        } catch (Exception e) {
            log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * 调用并忘记
     *
     * @param act
     * @param <T>
     */
    public static <T> void run(Consumer<T> act) {
        try {
            act.accept(null);
        } catch (Exception e) {
            log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 调用并忘记
     *
     * @param t
     * @param act
     * @param <T>
     */
    public static <T> void run(T t, Consumer<T> act) {
        try {
            act.accept(t);
        } catch (Exception e) {
            log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 调用并忘记
     *
     * @param t
     * @param t1
     * @param act
     * @param <T>
     * @param <T1>
     */
    public static <T, T1> void run(T t, T1 t1, BiConsumer<T, T1> act) {
        try {
            act.accept(t, t1);
        } catch (Exception e) {
            log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
        }
    }


    @FunctionalInterface
    public interface BiFunction3<T, T1, T2, R> {
        R apply(T t, T1 t1, T2 t2);
    }

    @FunctionalInterface
    public interface BiFunction4<T, T1, T2, T3, R> {
        R apply(T t, T1 t1, T2 t2, T3 t3);
    }
}

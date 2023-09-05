package com.hxx.sbcommon.common.basic.langType.impl.dateTimeLang;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 支持long、String、Date、LocalDate、LocalDateTime
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-27 11:02:11
 **/
@Slf4j
public class LocalDateTimeTypeHandler implements LangTypeHandler<LocalDateTime> {
    private final String formatPattern;

    /**
     * 默认 yyyy-MM-dd HH:mm:ss
     */
    public LocalDateTimeTypeHandler() {
        this("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * @param pattern yyyy-MM-dd HH:mm:ss
     */
    public LocalDateTimeTypeHandler(String pattern) {
        this.formatPattern = pattern;
    }

    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public LocalDateTime change(Object val) {
        if (val == null) {
            return null;
        }

        // 整型
//        if (val instanceof Byte) {
//            return ((Byte) val).intValue();
//        }
//        if (val instanceof Short) {
//            return ((Short) val).intValue();
//        }
//        if (val instanceof Integer) {
//            return (Integer) val;
//        }
        if (val instanceof Long) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli((Long) val), ZoneId.systemDefault());
        }

        // 浮点型
        if (val instanceof Float) {
            long longDate = ((Float) val).longValue();
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(longDate), ZoneId.systemDefault());
        }
        if (val instanceof Double) {
            long longDate = ((Double) val).longValue();
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(longDate), ZoneId.systemDefault());
        }
        if (val instanceof BigDecimal) {
            long longDate = ((BigDecimal) val).longValue();
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(longDate), ZoneId.systemDefault());
        }

        // 日期
        if (val instanceof Date) {
            return LocalDateTime.ofInstant(((Date) val).toInstant(), ZoneId.systemDefault());
        }

        if (val instanceof LocalDateTime) {
            return (LocalDateTime) val;
        }

        if (val instanceof LocalDate) {
            return ((LocalDate) val).atStartOfDay();
        }

        // 字符串
        if (val instanceof String) {
            try {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(this.formatPattern);
                return LocalDateTime.parse((String) val, dateTimeFormatter);
            } catch (Exception e) {
                log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
                throw new IllegalArgumentException("转换类型失败，提供值：" + val);
            }
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}

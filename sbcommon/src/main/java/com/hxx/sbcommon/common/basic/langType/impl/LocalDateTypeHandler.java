package com.hxx.sbcommon.common.basic.langType.impl;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-27 11:06:21
 **/
@Slf4j
public class LocalDateTypeHandler implements LangTypeHandler<LocalDate> {
    private DateTimeFormatter dateTimeFormatter;

    /**
     * 默认 yyyy-MM-dd HH:mm:ss
     */
    public LocalDateTypeHandler() {
        String pattern = "yyyy-MM-dd";
        dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    }

    /**
     * @param pattern yyyy-MM-dd
     */
    public LocalDateTypeHandler(String pattern) {
        dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    }

    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public LocalDate convert(Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof LocalDate) {
            return (LocalDate) val;
        }

        if (val instanceof String) {
            try {
                return LocalDate.parse((String) val, dateTimeFormatter);
            } catch (Exception e) {
                log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
                throw new IllegalArgumentException("转换类型失败，提供值：" + val);
            }
        }

        if (val instanceof Long) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli((Long) val), ZoneId.systemDefault())
                    .toLocalDate();
        }

        if (val instanceof Date) {
            return LocalDateTime.ofInstant(((Date) val).toInstant(), ZoneId.systemDefault())
                    .toLocalDate();
        }

        if (val instanceof LocalDateTime) {
            return ((LocalDateTime) val).toLocalDate();
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}

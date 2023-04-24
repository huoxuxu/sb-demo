package com.hxx.sbcommon.common.basic.langType.impl.dateTimeLang;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 支持：long、String、LocalDateTime、LocalDate
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-27 10:53:05
 **/
@Slf4j
public class DateTypeHandler implements LangTypeHandler<Date> {
    private SimpleDateFormat dateTimeFormatter;

    /**
     * 默认 yyyy-MM-dd HH:mm:ss
     */
    public DateTypeHandler() {
        String defaultPattern = "yyyy-MM-dd HH:mm:ss";
        dateTimeFormatter = new SimpleDateFormat(defaultPattern);
    }

    /**
     * @param pattern yyyy-MM-dd HH:mm:ss
     */
    public DateTypeHandler(String pattern) {
        dateTimeFormatter = new SimpleDateFormat(pattern);
    }

    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public Date change(Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof Date) {
            return (Date) val;
        }

        if (val instanceof String) {
            try {
                return dateTimeFormatter.parse((String) val);
            } catch (Exception e) {
                log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
                throw new IllegalArgumentException("转换类型失败，提供值：" + val);
            }
        }

        if (val instanceof Long) {
            try {
                return new Date((Long) val);
            } catch (Exception e) {
                log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
                throw new IllegalArgumentException("转换类型失败，提供值：" + val);
            }
        }

        if (val instanceof LocalDateTime) {
            return Date.from(((LocalDateTime) val).atZone(ZoneId.systemDefault())
                    .toInstant());
        }

        if (val instanceof LocalDate) {
            return Date.from(((LocalDate) val).atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toInstant());
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}

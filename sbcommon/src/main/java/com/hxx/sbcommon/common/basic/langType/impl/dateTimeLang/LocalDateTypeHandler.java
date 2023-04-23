package com.hxx.sbcommon.common.basic.langType.impl.dateTimeLang;

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
    private LocalDateTimeTypeHandler localDateTimeTypeHandler;

    /**
     * 默认 yyyy-MM-dd HH:mm:ss
     */
    public LocalDateTypeHandler() {
        String pattern = "yyyy-MM-dd";
        localDateTimeTypeHandler = new LocalDateTimeTypeHandler(pattern);
    }

    /**
     * @param pattern yyyy-MM-dd
     */
    public LocalDateTypeHandler(String pattern) {
        localDateTimeTypeHandler = new LocalDateTimeTypeHandler(pattern);
    }

    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public LocalDate change(Object val) {
        if (val == null) {
            return null;
        }

        LocalDateTime ldt = localDateTimeTypeHandler.change(val);
        return ldt.toLocalDate();
    }
}

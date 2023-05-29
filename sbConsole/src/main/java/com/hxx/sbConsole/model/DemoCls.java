package com.hxx.sbConsole.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-09 18:05:02
 **/
@lombok.Data
public class DemoCls {
    private int id;
    private String code;
    private String name;

    @lombok.Data
    public static class BasicTypeCls {
        private Boolean booleanVal;
        private Byte byteVal;
        private Character characterVal;

        private String stringVal;

        private Short shortVal;
        private Integer integerVal;
        private Long longVal;
        private BigInteger bigIntegerVal;

        private Float floatVal;
        private Double doubleVal;
        private BigDecimal bigDecimalVal;

        private Date dateVal;
        private LocalDateTime localDateTimeVal;
    }
}
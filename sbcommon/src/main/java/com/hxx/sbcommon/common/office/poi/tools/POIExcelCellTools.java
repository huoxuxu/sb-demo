package com.hxx.sbcommon.common.office.poi.tools;

import org.apache.poi.ss.usermodel.Cell;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2024-01-11 10:32:32
 **/
public class POIExcelCellTools {

    /**
     * java值写入Cell，不支持的java类型会报错
     *
     * @param val
     * @param cell
     */
    public static void write2Cell(Object val, Cell cell) {
        // 数值型double 日期型Date 文本String 布尔型boolean 空null
        {
            Double cval = getNumericVal(val);
            if (cval != null) {
                cell.setCellValue(cval);
                return;
            }
        }
        {
            String cval = getStringVal(val);
            if (cval != null) {
                cell.setCellValue(cval);
                return;
            }
        }
        {
            Boolean cval = getBooleanVal(val);
            if (cval != null) {
                cell.setCellValue(cval);
                return;
            }
        }
        {
            Date cval = getDateVal(val);
            if (cval != null) {
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                cell.setCellValue(f.format(cval));
                return;
            }
        }

        throw new IllegalStateException("不支持的数据类型！" + val.getClass());
    }

    private static Double getNumericVal(Object val) {
        // 整型
        if (val instanceof Byte) {
            return ((Byte) val).doubleValue();
        }
        if (val instanceof Short) {
            return ((Short) val).doubleValue();
        }
        if (val instanceof Integer) {
            return ((Integer) val).doubleValue();
        }
        if (val instanceof Long) {
            return ((Long) val).doubleValue();
        }

        // 浮点型
        if (val instanceof Float) {
            return ((Float) val).doubleValue();
        }
        if (val instanceof Double) {
            return (Double) val;
        }
        if (val instanceof BigDecimal) {
            return ((BigDecimal) val).doubleValue();
        }
        return null;
    }

    private static Date getDateVal(Object val) {
        if (val instanceof Date) {
            return (Date) val;
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
        return null;
    }

    private static String getStringVal(Object val) {
        // 字符串
        if (val instanceof String) {
            return (String) val;
        }
        return null;
    }

    private static Boolean getBooleanVal(Object val) {
        // 布尔型
        if (val instanceof Boolean) {
            return (Boolean) val;
        }
        return null;
    }

}

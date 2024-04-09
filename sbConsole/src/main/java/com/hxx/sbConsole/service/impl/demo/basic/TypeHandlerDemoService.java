package com.hxx.sbConsole.service.impl.demo.basic;

import com.hxx.sbConsole.model.DemoCls;
import com.hxx.sbConsole.model.KV;
import com.hxx.sbConsole.model.enums.LinePatternEnum;
import com.hxx.sbConsole.service.common.typeHandler.KVTypeHandler;
import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;
import com.hxx.sbcommon.common.basic.langType.LangTypeHandlerFactory;
import com.hxx.sbcommon.common.basic.langType.impl.dateTimeLang.DateTypeHandler;
import com.hxx.sbcommon.common.basic.langType.impl.dateTimeLang.LocalDateTimeTypeHandler;
import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-04-23 13:19:13
 **/
public class TypeHandlerDemoService {
    public static void main(String[] args) {
        try {
            System.out.println("==================================================\n");
            // 整型
            numberCase();
            System.out.println("==================================================\n");
            // 浮点型
            floatCase();
            System.out.println("==================================================\n");
            // 字符串
            stringCase();
            System.out.println("==================================================\n");
            // 日期型
            datetimeCase();
            dateCase();
            System.out.println("==================================================\n");
            // 布尔型
            booleanCase();
            System.out.println("==================================================\n");
            // 枚举
            enumCase();
            System.out.println("==================================================\n");
            // 注册类型处理器
            registerHandlerCase();
            System.out.println("==================================================\n");
            // 取消注册类型处理器
            unregisterHandlerCase();


        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
        System.out.println("ok");
    }

    private static void unregisterHandlerCase() {
        {
            LangTypeHandlerFactory.UnregisterTypeHandler(Integer.class);

            Object val = LangTypeHandlerFactory.convert("1", Integer.class);
            System.out.println("DemoCls =>KV: " + JsonUtil.toJSON(val) + " type: " + val.getClass());
        }
        {
            LangTypeHandlerFactory.UnregisterTypeHandler(KV.class);
            DemoCls demoCls = new DemoCls();
            {
                demoCls.setId(989);
                demoCls.setName("刘德华");
            }
            Object val = LangTypeHandlerFactory.convert(demoCls, KV.class);
            System.out.println("DemoCls =>KV: " + JsonUtil.toJSON(val) + " type: " + val.getClass());
        }
    }

    private static void registerHandlerCase() {
        LangTypeHandlerFactory.RegisterTypeHandler(KV.class, new KVTypeHandler());
        DemoCls demoCls = new DemoCls();
        {
            demoCls.setId(989);
            demoCls.setName("刘德华");
        }
        Object val = LangTypeHandlerFactory.convert(demoCls, KV.class);
        System.out.println("DemoCls =>KV: " + JsonUtil.toJSON(val) + " type: " + val.getClass());

    }

    static void numberCase() {
        {
            long num = 0;
            Object val = LangTypeHandlerFactory.convert(num, Byte.class);
            System.out.println("long =>byte: " + val + " type: " + (val == null ? "" : val.getClass()));
        }
        {
            long num = 125;
            Object val = LangTypeHandlerFactory.convert(num, Byte.class);
            System.out.println("long =>byte: " + val + " type: " + (val == null ? "" : val.getClass()));
        }
        {
            long num = -123;
            Object val = LangTypeHandlerFactory.convert(num, Byte.class);
            System.out.println("long =>byte: " + val + " type: " + (val == null ? "" : val.getClass()));
        }
        {
            long num = 9876;
            Object val = LangTypeHandlerFactory.convert(num, Byte.class);
            System.out.println("long =>byte: " + val + " type: " + (val == null ? "" : val.getClass()));
        }
        {
            Long num = null;
            Object val = LangTypeHandlerFactory.convert(num, Byte.class);
            System.out.println("long =>byte: " + val + " type: " + (val == null ? "" : val.getClass()));
        }
    }

    static void floatCase() {
        {
            float num = 0;
            Object val = LangTypeHandlerFactory.convert(num, BigDecimal.class);
            System.out.println("float =>BigDecimal: " + val + " type: " + val.getClass());
        }
        {
            Float num = null;
            Object val = LangTypeHandlerFactory.convert(num, BigDecimal.class);
            System.out.println("float =>BigDecimal: " + val + " type: " + (val == null ? "" : val.getClass()));
        }
        {
            long num = 9876;
            Object val = LangTypeHandlerFactory.convert(num, BigDecimal.class);
            System.out.println("long =>BigDecimal: " + val + " type: " + val.getClass());
        }
    }

    static void stringCase() {
        {
            long num = 987654321;
            Object val = LangTypeHandlerFactory.convert(num, String.class);
            System.out.println("long =>String: " + val + " type: " + val.getClass());
        }
        {
            float num = 987.456123014789F;
            Object val = LangTypeHandlerFactory.convert(num, String.class);
            System.out.println("float =>String: " + val + " type: " + val.getClass());
        }
        {
            BigDecimal num = new BigDecimal("987.456123014789");
            Object val = LangTypeHandlerFactory.convert(num, String.class);
            System.out.println("BigDecimal =>String: " + val + " type: " + val.getClass());
        }
        {
            boolean num = true;
            Object val = LangTypeHandlerFactory.convert(num, String.class);
            System.out.println("boolean =>String: " + val + " type: " + val.getClass());
        }
        {
            boolean num = false;
            Object val = LangTypeHandlerFactory.convert(num, String.class);
            System.out.println("boolean =>String: " + val + " type: " + val.getClass());
        }

    }

    static void datetimeCase() {
        {
            long num = 1682228684000L;
            Object val = LangTypeHandlerFactory.convert(num, LocalDateTime.class);
            System.out.println("long =>LocalDateTime: " + val + " type: " + val.getClass());
        }
        {
            String num = "2023-04-23 13:46:19";
            Object val = LangTypeHandlerFactory.convert(num, LocalDateTime.class);
            System.out.println("String =>LocalDateTime: " + val + " type: " + val.getClass());
        }
        {
            String num = "2023/04/23 13:46:19";
            LangTypeHandler typeHandler = LangTypeHandlerFactory.getInstance(LocalDateTimeTypeHandler.class, "yyyy/MM/dd HH:mm:ss");
            Object val = typeHandler.change(num);
            System.out.println("String =>LocalDateTime: " + val + " type: " + val.getClass());
        }
        {
            Date num = new Date();
            Object val = LangTypeHandlerFactory.convert(num, LocalDateTime.class);
            System.out.println("Date =>LocalDateTime: " + val + " type: " + val.getClass());
        }

    }

    static void dateCase() {
        {
            long num = 1682228684000L;
            Object val = LangTypeHandlerFactory.convert(num, Date.class);
            System.out.println("long =>Date: " + val + " type: " + val.getClass());
        }
        {
            LocalDateTime num = LocalDateTime.now();
            Object val = LangTypeHandlerFactory.convert(num, Date.class);
            System.out.println("LocalDateTime =>Date: " + val + " type: " + val.getClass());
        }
        {
            String num = "2023-04-23 13:46:19";
            Object val = LangTypeHandlerFactory.convert(num, Date.class);
            System.out.println("String =>Date: " + val + " type: " + val.getClass());
        }
        {
            String num = "2023/04/23 13:46:19";
            LangTypeHandler typeHandler = LangTypeHandlerFactory.getInstance(DateTypeHandler.class, "yyyy/MM/dd HH:mm:ss");
            Object val = typeHandler.change(num);
            System.out.println("String =>Date: " + val + " type: " + val.getClass());
        }

    }

    static void booleanCase() {
        {
            long num = 1682228684000L;
            Object val = LangTypeHandlerFactory.convert(num, Boolean.class);
            System.out.println("long =>boolean: " + val + " type: " + val.getClass());
        }
        {
            float num = 1682228684000.666F;
            Object val = LangTypeHandlerFactory.convert(num, Boolean.class);
            System.out.println("long =>boolean: " + val + " type: " + val.getClass());
        }
        {
            String num = "true";
            Object val = LangTypeHandlerFactory.convert(num, Boolean.class);
            System.out.println("String =>boolean: " + val + " type: " + val.getClass());
        }
        {
            String num = "True";
            Object val = LangTypeHandlerFactory.convert(num, Boolean.class);
            System.out.println("String =>boolean: " + val + " type: " + val.getClass());
        }
        {
            String num = "TRUE";
            Object val = LangTypeHandlerFactory.convert(num, Boolean.class);
            System.out.println("String =>boolean: " + val + " type: " + val.getClass());
        }

    }

    static void enumCase() {
        Class<LinePatternEnum> enumCls = LinePatternEnum.class;
        LinePatternEnum linePatternEnum = LinePatternEnum.BIG;
        {
            // 同类型
            LinePatternEnum eval = (LinePatternEnum) LangTypeHandlerFactory.convert(linePatternEnum, enumCls);
            System.out.println(eval);
        }
        {
            // 字符串
            LinePatternEnum eval = (LinePatternEnum) LangTypeHandlerFactory.convert("BIG", enumCls);
            System.out.println(eval);
        }
        {
            // 字符串小写  不通过
//                    LinePatternEnum eval = (LinePatternEnum) LangTypeHandlerFactory.convert("big", enumCls);
//                    System.out.println(eval);
        }
        {
            // 索引位
            LinePatternEnum eval = (LinePatternEnum) LangTypeHandlerFactory.convert(1, enumCls);
            System.out.println(eval);
        }
        {
            // 索引位 0
            LinePatternEnum eval = (LinePatternEnum) LangTypeHandlerFactory.convert(0L, enumCls);
            System.out.println(eval);
        }
    }
}

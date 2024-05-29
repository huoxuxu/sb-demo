package com.hxx.hdblite.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ModelTools {

    /**
     * 字典转实体，
     * 注意，不忽略实体属性的大小写
     * 注意，实体必须是标准Bean([get|is]|set后的字段名首字母必须大写，其他无所谓)
     * 注意，类型必须一致,源中为null的字段，不会拷贝
     *
     * @param dic
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T ToEntity(Map<String, Object> dic, Class clazz) throws Exception {
        Field[] fields = clazz.getDeclaredFields();

        Object obj = clazz.newInstance();
        for (Field f : fields) {
            String fieldName = f.getName();

            if (!dic.containsKey(fieldName)) continue;//数据源不包含此字段，跳过

            Object val = dic.get(fieldName);
            if (val == null) continue;//数据源值为null，跳过

            Class vcls = val.getClass();

            Class fieldType = f.getType();
            //标准Bean为set后字段首字母大写
            String firstCharStr = fieldName.substring(0, 1);
            String setFieldName = String.format("set%s%s", firstCharStr.toUpperCase(), fieldName.substring(1));

            try {
                //如果数据库类型和当前类型不一致，尝试先转换
                if (!fieldType.getName().equals(vcls.getName())) {
                    continue;
                }

                Method method = clazz.getMethod(setFieldName, fieldType);
                method.invoke(obj, val);
            } catch (Exception ex) {
                String valTypeName = vcls.getName();
                String msg = String.format("字段名：%s 字段类型：%s 源字段类型：%s 源字段值：%s",
                        fieldName, fieldType, valTypeName, val);

                throw new Exception(msg, ex);
            }
        }
        return (T) obj;
    }

}

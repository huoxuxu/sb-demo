package com.hxx.hdblite.tools.exclude;

import com.hxx.hdblite.tools.MapTools;
import com.hxx.hdblite.tools.StringTools;
import com.hxx.hdblite.tools.TypeChange.ChangeBaseTypeFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ModelTools {
    /**
     * 将类变成字典
     * 只处理属性
     * @param model
     * @return
     * @throws Exception
     */
    public static Map<String, Object> ToDic(Object model) throws Exception {
        Map<String, Object> dic = new HashMap<>();

        Class cls = model.getClass();

        while (cls != Object.class) {
            Field[] fieldArr = cls.getDeclaredFields();

            for (Field field : fieldArr) {
                String fieldName = field.getName();
                if (dic.containsKey(fieldName)) continue;

                String getPrefix = "get%s%s";
                Class fieldType = field.getType();
                if (fieldType == Boolean.class) {
                    getPrefix = "is%s%s";
                }

                String firstCharStr = fieldName.substring(0, 1);
                //String setFieldName=String.format("set%s%s",firstCharStr.toUpperCase(),fieldName.substring(1));
                String getFieldName = String.format(getPrefix, firstCharStr.toUpperCase(), fieldName.substring(1));

                Method getMethod = cls.getMethod(getFieldName);
                Object val = getMethod.invoke(model);

                dic.put(fieldName, val);
            }

            cls = cls.getSuperclass();
        }

        return dic;
    }

    /**
     * 实体对象转成Map
     * 只处理字段
     * @param obj 实体对象
     * @return
     */
    public static Map<String, Object> object2Map(Object obj) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            throw new Exception("实体对象转成Map错误",e);
        }

        return map;
    }

    /**
     * Map转成实体对象
     * 只处理字段
     * @param map map实体对象包含属性
     * @param clazz 实体对象类型
     * @return
     */
    public static Object map2Object(Map<String, Object> map, Class<?> clazz) {
        if (map == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = clazz.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }

                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 字典转实体，支持给父类字段赋值
     * 注意，不忽略实体属性的大小写
     * 注意，实体必须是标准Bean([get|is]|set后的字段名首字母必须大写，其他无所谓)
     * 注意，类型必须一致,源中为null的字段，不会拷贝
     *
     * @param dic
     * @param target
     * @throws Exception
     */
    public static void Copy(Map<String, Object> dic, Object target) throws Exception {
        Class clazz = target.getClass();

        while (clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field f : fields) {
                String fieldName = f.getName();

                //数据源不包含此字段，跳过
                //if (!dic.containsKey(fieldName)) continue;
                if (!MapTools.containsKey(dic, fieldName)) continue;

                //Object val = dic.get(fieldName);
                Object val = MapTools.getVal(dic, fieldName);
                if (val == null) continue;//数据源值为null，跳过

                Class vcls = val.getClass();

                Class fieldType = f.getType();
                //标准Bean为set后字段首字母大写
                String firstCharStr = fieldName.substring(0, 1);
                String setFieldName = String.format("set%s%s", firstCharStr.toUpperCase(), fieldName.substring(1));

                try {
                    //如果数据库类型和当前类型不一致，尝试先转换
                    if (!fieldType.getName().equals(vcls.getName())) {
                        val = ChangeBaseTypeFactory.Get(val, fieldType);
                        //val=StringTools.ChangeType(val,fieldType);
                    }

                    Method method = clazz.getMethod(setFieldName, fieldType);
                    method.invoke(target, val);
                } catch (Exception ex) {
                    String valTypeName = vcls.getName();
                    String msg = String.format("字段名：%s 字段类型：%s 源字段类型：%s 源字段值：%s",
                            fieldName, fieldType, valTypeName, val);

                    throw new Exception(msg, ex);
                }
            }

            clazz = clazz.getSuperclass();
        }
    }

    /**
     * 字典转实体，支持给父类字段赋值
     * @param dic key= propKdbFieldVMap 中的val，val= 需要给target复制的值
     * @param target 需要赋值的对象
     * @param propKdbFieldVMap key= 对象的属性名，val= dic 的key
     * @throws Exception
     */
    public static void Copy2(Map<String, Object> dic, Object target, Map<String, String> propKdbFieldVMap) throws Exception {
        if (propKdbFieldVMap == null) propKdbFieldVMap = new HashMap<>();

        Class clazz = target.getClass();

        while (clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field f : fields) {
                String fieldName = f.getName();

                //获取 dic 中的Key
                String dbFieldName = "";
                {
                    for (Map.Entry<String, String> item : propKdbFieldVMap.entrySet()) {
                        if (StringTools.EqualsIgnoreCase(item.getKey(), fieldName)) {
                            dbFieldName = item.getValue();
                        }
                    }
                    //未在 propKdbFieldVMap 找到 对应的数据库字段名
                    if (dbFieldName.equals("")) continue;
                }

                Object val = MapTools.getVal(dic, dbFieldName);
                if (val == null) continue;//数据源值为null，跳过

                Class vcls = val.getClass();

                Class fieldType = f.getType();
                //标准Bean为set后字段首字母大写
                String firstCharStr = fieldName.substring(0, 1);
                String setFieldName = String.format("set%s%s", firstCharStr.toUpperCase(), fieldName.substring(1));

                try {
                    //如果数据库类型和当前类型不一致，尝试先转换
                    if (!fieldType.getName().equals(vcls.getName())) {
                        val = ChangeBaseTypeFactory.Get(val, fieldType);
                        //val=StringTools.ChangeType(val,fieldType);
                    }

                    Method method = clazz.getMethod(setFieldName, fieldType);
                    method.invoke(target, val);
                } catch (Exception ex) {
                    String valTypeName = vcls.getName();
                    String msg = String.format("字段名：%s 字段类型：%s 源字段类型：%s 源字段值：%s",
                            fieldName, fieldType, valTypeName, val);

                    throw new Exception(msg, ex);
                }
            }

            clazz = clazz.getSuperclass();
        }
    }

    //支持获取父类私有字段
    public static Map<String, Object> getObjectToMap(Object t) throws IllegalAccessException {
        Class cls = t.getClass();
        Map<String, Object> param = new HashMap<>();

        while (cls != Object.class) {
            Field[] fields = cls.getDeclaredFields();//获取所有私有字段
            for (Field field : fields) {
                field.setAccessible(true);
                param.put(field.getName(), field.get(t));
            }

            cls = cls.getSuperclass();
        }

        return param;
    }

}

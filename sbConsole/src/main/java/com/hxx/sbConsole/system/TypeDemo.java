package com.hxx.sbConsole.system;

import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import com.hxx.sbcommon.common.reflect.BeanTypeUtil;
import lombok.Data;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeDemo {

    public static void main(String[] args) throws NoSuchFieldException {
        // 测试两个泛型类型的字段怎么对比其类型相同
        {
            List<Field> declaredFields = FieldUtils.getAllFieldsList(BeanDemo.class);
            Map<String, List<Field>> targetFieldMap = declaredFields.stream()
                    .collect(Collectors.groupingBy(d -> d.getName()));
            List<Field> left = targetFieldMap.get("left");
            List<Field> right = targetFieldMap.get("right");

            boolean flag = BeanTypeUtil.eq(left.get(0), right.get(0));
            System.out.println(flag); // 应该返回true
        }

        // 测试获取嵌套泛型的类型
        {
            List<Field> declaredFields = FieldUtils.getAllFieldsList(BeanDemo.class);
            Map<String, List<Field>> targetFieldMap = declaredFields.stream()
                    .collect(Collectors.groupingBy(d -> d.getName()));
            List<Field> left = targetFieldMap.get("left");
            List<Type[]> genericTypeLevels = BeanTypeUtil.getGenericTypeLevel(left.get(0));
            System.out.println(JsonUtil.toJSON(genericTypeLevels));
        }

        // 变量类型
        {
            BeanDemo<Integer> beanDemo = new BeanDemo<>();
            List<BeanDemo<List<String>>> left = new ArrayList<>();
            BeanDemo<List<String>> left1 = new BeanDemo<>();
            left.add(left1);
            beanDemo.setLeft(left);

            Class<?> leftClass = left.getClass();
            TypeVariable<?>[] typeParameters = leftClass.getTypeParameters();
            System.out.println();
        }
    }


    @Data
    static class BeanDemo<T> {

        private List<Integer> left0;
        private List<Integer> right0;

        private List<BeanDemo<String>> left1;
        private List<BeanDemo<Integer>> right1;

        private List<BeanDemo<List<String>>> left;
        private List<BeanDemo<List<Integer>>> right;

        private T val;

    }
}

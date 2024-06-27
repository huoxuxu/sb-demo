package com.hxx.sbConsole.other.reflect;

import com.hxx.sbConsole.model.User;
import com.hxx.sbConsole.model.inherit.Dog;
import com.hxx.sbConsole.model.inherit.HaBaDog;
import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import com.hxx.sbcommon.common.office.EasyExcelHelper;
import com.hxx.sbcommon.common.reflect.BeanCopyUtil;
import com.hxx.sbcommon.common.reflect.BeanInfoUtil;
import com.hxx.sbcommon.common.reflect.ReflectUseful;
import com.hxx.sbcommon.model.Result;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-25 13:31:40
 **/
public class ReflectDemoService {
    public static void main(String[] args) {
        try {
            // BeanInfoUtil
            System.out.println("case0()\n");
            case0();

            // ReflectUseful
            System.out.println("case1()\n");
            case1();

            System.out.println("case2()\n");
            case2();

            // 泛型
            System.out.println("case3()\n");
            case3();

            // beanCopy
            System.out.println("case4()\n");
            case4();

        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
        System.out.println("ok");
    }

    static void case0() throws Exception {
        {
            EasyExcelHelper.DemoEasyExcelVO demoVO = new EasyExcelHelper.DemoEasyExcelVO();
            demoVO.setCode("cd");
            demoVO.setName("nm");

            // bean to map
            Map<String, Object> map = BeanInfoUtil.toMap(demoVO);
            System.out.println(map);

            // map to bean
            demoVO = new EasyExcelHelper.DemoEasyExcelVO();
            BeanInfoUtil.copyToBean(demoVO, map);
            System.out.println(demoVO);
        }
        {
            // =================泛型字段===================================
            Result<String> result = new Result<>("", "xx", "m12");
            Map<String, Object> objMap1 = BeanInfoUtil.toMap(result);
            System.out.println("objMap：" + JsonUtil.toJSON(objMap1));

            result = new Result<>("", "", "");
            BeanInfoUtil.copyToBean(result, objMap1);
            System.out.println(result);
        }
    }

    static void case1() throws Exception {
        Dog dog = new HaBaDog();
        Class<? extends Dog> aClass = dog.getClass();

        Map<String, Method> methods = ReflectUseful.getMethodAsMap(aClass);

        ReflectUseful reflectUseful = new ReflectUseful(aClass);
        List<ReflectUseful.PropInfo> propInfos = reflectUseful.getPropInfos();
        List<String> props = propInfos.stream().map(d -> d.getName()).collect(Collectors.toList());
        System.out.println("props: " + JsonUtil.toJSON(props));

        // BeanInfoUtil
        Map<String, Object> map = BeanInfoUtil.toMap(dog);
        System.out.println("props: " + JsonUtil.toJSON(map.keySet()));

        // ==
        System.out.println("ok");
    }

    static void case2() throws Exception {
        User user = new User();
        {
            user.setId(123);
            user.setCode("ls");
            user.setName("里斯");
        }

        Map<String, Object> map = BeanInfoUtil.toMap(user);
        System.out.println("user对象：" + JsonUtil.toJSON(user));
        System.out.println("userMap：" + JsonUtil.toJSON(map));

        User dest = new User();
        BeanInfoUtil.copyTo(user, dest);
        System.out.println("dest对象：" + JsonUtil.toJSON(dest));

        System.out.println("==================================================");

        Dog dog = new HaBaDog();
        BeanInfoUtil.copyToObj(user, dog);
        System.out.println("dog：" + JsonUtil.toJSON(dog));

    }

    static void case3() throws Exception {
        // =================泛型字段===================================
        Result<String> result = new Result<>("", "xx", "m12");
        ReflectUseful reflectUseful = new ReflectUseful(result.getClass());

        List<ReflectUseful.PropInfo> propInfos = reflectUseful.getPropInfos();
        List<String> props = propInfos.stream().map(d -> d.getName()).collect(Collectors.toList());
        System.out.println("props: " + JsonUtil.toJSON(props));
    }

    static void case4() throws Exception {
        Dog dog = new HaBaDog();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "ddog");
        map1.put("age", "100岁");
        {
            Class<? extends Dog> dogCls = dog.getClass();
            dogCls.isPrimitive();
        }

        // beanToMap
        {
            Map<String, Object> map2 = BeanCopyUtil.toMap(dog);
            System.out.println("dog-map: " + JsonUtil.toJSON(map2));
        }

//        // copyTo
//        {
//            map1.put("female", null);
//            map1.put("haba", null);
//            BeanCopyUtil.copyMapTo(dog, map1, false);
//            System.out.println("dog: " + JsonUtil.toJSON(dog));
//        }

        {
            // 通过类加载器重新加载 MyClass (卸载不成功)
            ClassLoader classLoader = dog.getClass().getClassLoader();
            classLoader.loadClass("com.hxx.sbConsole.model.inherit.HaBaDog");

            Map<String, Object> map2 = BeanCopyUtil.toMap(dog);
            System.out.println("dog-map: " + JsonUtil.toJSON(map2));
        }
    }

}

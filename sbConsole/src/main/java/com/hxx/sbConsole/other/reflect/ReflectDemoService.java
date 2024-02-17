package com.hxx.sbConsole.other.reflect;

import com.hxx.sbConsole.model.User;
import com.hxx.sbConsole.model.inherit.Dog;
import com.hxx.sbConsole.model.inherit.HaBaDog;
import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import com.hxx.sbcommon.common.office.EasyExcelHelper;
import com.hxx.sbcommon.common.reflect.BeanInfoUtil;
import com.hxx.sbcommon.common.reflect.ReflectUseful;
import com.hxx.sbcommon.model.Result;

import java.util.List;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-25 13:31:40
 **/
public class ReflectDemoService {
    public static void main(String[] args) {
        try {
            // BeanInfoUtil
            case0();
            // ReflectUseful
            case1();
            case2();

        } catch (Exception ex) {
            System.out.println(ex + "");
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
        {
            Dog dog = new HaBaDog();
            ReflectUseful reflectUseful = new ReflectUseful(dog.getClass());
            List<String> props = reflectUseful.getProps();
            System.out.println("props: " + JsonUtil.toJSON(props));

            Map<String, Object> map = BeanInfoUtil.toMap(dog);
            System.out.println("props: " + JsonUtil.toJSON(map.keySet()));
        }
        {
            // =================泛型字段===================================
            Result<String> result = new Result<>("", "xx", "m12");
            ReflectUseful reflectUseful = new ReflectUseful(result.getClass());

            List<String> props = reflectUseful.getProps();
            System.out.println("props: " + JsonUtil.toJSON(props));
        }
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

}

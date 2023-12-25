package javat.modules.reflect;

import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.basic.OftenUtil;
import models.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-20 17:35:35
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class ReflectionTest {

    @Test
    public void case1() {
        Person person = new Person();
        Class<? extends Person> cls = person.getClass();
        // 查找字段
        {
            Field alive = ReflectionUtils.findField(cls, "alive");
            Field alive2 = ReflectionUtils.findField(cls, "alive", Boolean.class);
            System.out.println(alive == alive2); // false
        }
        // 设置字段值
        {
            Field id = ReflectionUtils.findField(cls, "id");
            // 因为字段是私有的，所以此处不能直接设置值
            ReflectionUtils.makeAccessible(id);
            ReflectionUtils.setField(id, person, 1999);

            id = ReflectionUtils.findField(cls, "id");
            // 因为字段是私有的，所以此处不能直接设置值
            ReflectionUtils.makeAccessible(id);
            Object idVal = ReflectionUtils.getField(id, person);
            System.out.println(idVal != null && 1999L == (Long) idVal);// true
        }
        // 查找&调用方法
        {
            // 无参
            Method getIdMethod = ReflectionUtils.findMethod(cls, "getId");
            System.out.println(getIdMethod == null); // false
            // 带参
            Method setIdMethod = ReflectionUtils.findMethod(cls, "setId", long.class);
            System.out.println(setIdMethod == null); // false

            // 带参调用
            Object ignore = ReflectionUtils.invokeMethod(setIdMethod, person, 2000L);
            // 无参调用
            Object idVal = ReflectionUtils.invokeMethod(getIdMethod, person);
            System.out.println(idVal == null || 2000L == (Long) idVal); // true
        }
        // 操作
        {
            Method getIdMethod = ReflectionUtils.findMethod(cls, "getId");
            ReflectionUtils.makeAccessible(getIdMethod);
        }

        System.out.println("ok");
    }
}

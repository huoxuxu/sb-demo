import com.hxx.sbConsole.SbConsoleApplication;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class ReflectTest {

    @Test
    public void test() {
        System.out.println("==============test==============");

    }

    @Test
    public void test1() {
        Class<Boolean> booleanClass = boolean.class;
        Class<Boolean> booleanClass1 = Boolean.class;
        Class<Boolean> booleanClass2 = boolean.class;

        // false
        System.out.println(booleanClass == booleanClass1);
        // false
        System.out.println(booleanClass.equals(booleanClass1));
        // true
        System.out.println(booleanClass == booleanClass2);
        String[] split = StringUtils.split(" 1, 2 ,3", ",");
        split=" 1, 2 ,3".split(",");
        for (String s : split) {
            System.out.println(s);
        }
    }





}

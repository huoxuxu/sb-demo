package java;

import com.hxx.sbConsole.SbConsoleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

        // false
        System.out.println(booleanClass == booleanClass1);
        // false
        System.out.println(booleanClass.equals(booleanClass1));

    }


}

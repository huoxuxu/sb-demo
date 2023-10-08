package javat.basic;

import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.basic.text.StringUtil;
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
public class BasicTest {

    @Test
    public void test() {
        System.out.println("==============test==============");

    }

    @Test
    public void case1() {
        System.out.println("==============test==============");
        Integer a = null;
        System.out.println(a + "1");

        Integer a1 = null;
        System.out.println(a1 + 1);
    }

    @Test
    public void case2() {
        System.out.println("==============test==============");
        boolean b = "".startsWith("");
        boolean c = "".contains("");
        String a1 = StringUtil.getEndNumber("A");
        String a2 = StringUtil.getEndNumber("A1");
        String a3 = StringUtil.getEndNumber("A123");

        Integer a = null;
        System.out.println(a + "1");

    }

}

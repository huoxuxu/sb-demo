package basic;

import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.basic.OftenUtil;
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
public class StringTest {

    @Test
    public void test() {
        System.out.println("==============test==============");
        String str = "1,2-3^4$5";
        // 原始的不能正常处理正则的分隔符，因为其入参需要传入正则表达式
        String[] split = str.split("^");
        System.out.println(split);

        // 可以正常处理！！！
        System.out.println(OftenUtil.Split(str, "^"));

        System.out.println("ok");
    }

    @Test
    public void split() {
        System.out.println("==============test==============");
        String str = "1,2-3^4$5";
        // 原始的不能正常处理正则的分隔符，因为其入参需要传入正则表达式
        String[] split = str.split("^");
        System.out.println(split);

        // 可以正常处理！！！
        System.out.println(OftenUtil.Split(str, "^"));

        System.out.println("ok");
    }

}

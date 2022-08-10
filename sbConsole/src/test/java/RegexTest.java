import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.basic.RegexUtil;
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
public class RegexTest {

    @Test
    public void test() {
        System.out.println("==============test==============");
        String tmpPath = System.getProperty("java.io.tmpdir");
        System.out.println("临时目录：" + tmpPath);
    }

    @Test
    public void isHasAlphabet() {
        System.out.println("==============test==============");
        {
            boolean flag = RegexUtil.isHasAlphabet("aAzZ");
            System.out.println("包含字母a-zA-Z：" + flag);
        }
        {
            boolean flag = RegexUtil.isHasAlphabet("aAzZ+-*/");
            System.out.println("包含字母a-zA-Z：" + flag);
        }
        {
            boolean flag = RegexUtil.isHasAlphabet("1+-*/");
            System.out.println("包含字母a-zA-Z：" + flag);
        }
    }

}

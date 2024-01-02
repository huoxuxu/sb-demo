package javat.basic;

import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.basic.text.RegexUtil;
import com.hxx.sbcommon.common.json.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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

    }

    @Test
    public void test_RegexMatch() {
        System.out.println("==============test==============");
        String[] interfCodes = {"001", "001-A", "0001", "0001-A"};
        String regex = "(^\\d{3,4}+$)|(^\\d{3,4}-[A-Z]{1}+$)";
        for (String interfCode : interfCodes) {
            System.out.println(interfCode + "  " + (RegexUtil.regexMatch(interfCode, regex) ? "ok" : "no match"));
        }
    }

    @Test
    public void test_regexMatchGroups() {
        System.out.println("==============test==============");
        List<String> ls = RegexUtil.regexMatchGroups("A-B-C-D-EF-", "[A-Z]{1}-");
        System.out.println(JsonUtil.toJSON(ls));
    }


    @Test
    public void isHasAlphabet() {
        System.out.println("==============包含字母==============");
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

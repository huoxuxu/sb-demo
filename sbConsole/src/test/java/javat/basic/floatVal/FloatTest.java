package javat.basic.floatVal;

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
public class FloatTest {

    @Test
    public void test() {
        System.out.println("==============test==============");

    }

    @Test
    public void test_precision() {
        System.out.println("==============test==============");
        {
            // 32位单精度浮点数的有效位数是7位。(有效数位包含整数)
            // 64位双精度浮点数的有效位数是16位。
            Float dVal = 123456789.12345678F;
            System.out.println(String.format("%.10f", dVal));// 123456792.0000000000
            dVal = 12345678.12345678F;
            System.out.println(String.format("%.10f", dVal));// 12345678.0000000000
            dVal = 1234567.12345678F;
            System.out.println(String.format("%.10f", dVal));// 1234567.1250000000
            dVal = 123456.12345678F;
            System.out.println(String.format("%.10f", dVal));// 123456.1250000000

            dVal = 12345678.98765432F;
            System.out.println(String.format("%.10f", dVal));// 12345679.0000000000
            dVal = 1234567.98765432F;
            System.out.println(String.format("%.10f", dVal));// 1234568.0000000000
            dVal = 123456.98765432F;
            System.out.println(String.format("%.10f", dVal));// 123456.9843750000

            dVal = 0.123456789F;
            System.out.println(dVal);// 0.12345679
        }
    }

}

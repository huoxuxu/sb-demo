package javat.basic;

import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.basic.number.BigDecimalUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class BigDecimalTest {

    @Test
    public void stripTrailingZeros() {
        System.out.println("==============test==============");
        // Initialize two variables - double and
        // String type
        double val = 1234521.000;
        String str = "1211000.00";

        // Initialize three BigDecimal objects
        BigDecimal b_dec1 = new BigDecimal(val);
        BigDecimal b_dec2 = new BigDecimal(str);

        // returns this BigDecimal (b_dec1) but
        // with removed any trailing zeros i.e. 1234521.000
        // it returns 1234521 because traling zeros will be removed
        BigDecimal bd = b_dec1.stripTrailingZeros();
        System.out.println("b_dec1.stripTrailingZeros(): " + bd);

        // returns this BigDecimal (b_dec2) but
        // with removed any trailing zeros i.e. 1211000
        // it returns 1.211E+6 and after decimal represent 6 digits
        bd = b_dec2.stripTrailingZeros();
        System.out.println("b_dec2.stripTrailingZeros(): " + bd);
    }

    @Test
    public void format() {
        {
            BigDecimal val = new BigDecimal("1.2357000");
            String format = BigDecimalUtil.Format(val);
            System.out.println(format);
        }
        {
            BigDecimal val = new BigDecimal("1211000.00");
            String format = BigDecimalUtil.Format(val);
            System.out.println(format);
        }
        {
            BigDecimal val = new BigDecimal(1.23570001);
            String format = BigDecimalUtil.Format(val);
            System.out.println(format);
        }

    }
}

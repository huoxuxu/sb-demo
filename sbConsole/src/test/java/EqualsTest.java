import com.hxx.sbConsole.SbConsoleApplication;
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
public class EqualsTest {
    @Test
    public void testDecimalEqual() {
        Long lll = null;
        BigDecimal val = BigDecimal.valueOf(lll);
        BigDecimal compareVal = BigDecimal.valueOf(1);
        int compFlag = val.compareTo(compareVal);

        BigDecimal tLenVal = BigDecimal.valueOf(20);
        BigDecimal tLenVal2 = BigDecimal.valueOf(20.0);
        pl(tLenVal.compareTo(tLenVal2) + "");
    }

    @Test
    public void testEqual() {
        System.out.println("==============test==============");
        int int1 = 12;
        int int2 = 12;

        int int3 = 128;
        int int4 = 128;

        Integer integer1 = new Integer(12);
        Integer integer2 = new Integer(12);
        Integer integer3 = new Integer(127);

        Integer a1 = 127;
        Integer b1 = 127;

        Integer a = 128;
        Integer b = 128;

        System.out.println("int1 == int2 -> " + (int1 == int2));
        System.out.println("int1 == integer1 -> " + (int1 == integer1));
        System.out.println("a1 == b1 -> " + (a1 == b1));
        System.out.println("int3 == int4 -> " + (int3 == int4));
        System.out.println();
        System.out.println("integer1 == integer2 -> " + (integer1 == integer2));
        System.out.println("integer3 == a1 -> " + (integer3 == a1));
        System.out.println("a == b -> " + (a == b));
    }

    @Test
    public void testEquals() {
        System.out.println("==============test==============");
        int int12 = 12;

        long long128 = 1281;
        int int128 = 1281;

        Integer new_integer12_1 = new Integer(12);
        Integer new_integer12_2 = new Integer(12);
        Integer new_integer127 = new Integer(127);
        Integer new_integer128 = new Integer(1281);

        Integer valOf127_1 = 127;
        Integer valOf127_2 = 127;

        Integer valOf128_1 = 1281;
        Integer valOf128_2 = 1281;

        pl("");
        pl("new_integer12_1 equals new_integer12_2 -> " + (new_integer12_1.equals(new_integer12_2)));
        pl("valOf127_1 equals valOf127_2 -> " + (valOf127_1.equals(valOf127_2)));
        pl("valOf128_1 equals valOf128_2 -> " + (valOf128_1.equals(valOf128_2)));

        pl("");
        pl("new_integer12_1 equals int12 -> " + (new_integer12_1.equals(int12)));
        pl("new_integer127 equals valOf127_1 -> " + (new_integer127.equals(valOf127_1)));
        pl("valOf128_1 equals int128 -> " + (valOf128_1.equals(int128)));
        pl("");
        pl("valOf128_1 equals long128 -> " + (valOf128_1.equals(long128)));
    }

    private static void pl(String str) {
        System.out.println(str);
    }

}

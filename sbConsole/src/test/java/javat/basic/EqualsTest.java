package javat.basic;

import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.basic.OftenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class EqualsTest {

    @Test
    public void testObjectsEqual() {
        Long a = 123L;
        // 会隐式类型转换
        System.out.println("a == 123 " + (a == 123));// true
        // 类型不一致时直接返回false
        System.out.println("a.equals(123) " + (a.equals(123)));// false
        // 先比较引用，如果不等再比较类型，不一致时直接返回false
        System.out.println("Objects.equals(a, 123) " + (Objects.equals(a, 123))); // false
        System.out.println("OftenUtil.NumberUtil.eq(a,123) " + OftenUtil.NumberUtil.basicEquals(a, 123));
    }

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

        Integer integer1 = new Integer(12);
        Integer integer2 = new Integer(12);

        Integer newInteger127 = new Integer(127);

        Integer a127 = 127;
        Integer b127 = 127;

        int int128880 = 128880;
        int int128880Same = 128880;

        Integer integer128880 = 128880;
        Integer integerValueOf128880 = Integer.valueOf(128880);
        Integer newInteger128880 = new Integer(128880);

        System.out.println("int1 == int2 -> " + (int1 == int2));
        System.out.println("int1 == integer1 -> " + (int1 == integer1));
        System.out.println("a127 == b127 -> " + (a127 == b127));
        System.out.println("int128880 == int128880Same -> " + (int128880 == int128880Same));
        System.out.println();
        System.out.println("integer1 == integer2 -> " + (integer1 == integer2)); // false
        System.out.println("integer1 Objects.equals integer2 -> " + Objects.equals(integer1, integer2));
        System.out.println("newInteger127 == a127 -> " + (newInteger127 == a127)); // false
        System.out.println("newInteger127 Objects.equals  a127 -> " + Objects.equals(newInteger127, a127));
        System.out.println("integer128880 == integerValueOf128880 -> " + (integer128880 == integerValueOf128880)); // false
        System.out.println("integer128880 eq integerValueOf128880 -> " + (integer128880.equals(integerValueOf128880)));
        System.out.println("int128880 == integer128880 -> " + (int128880 == integer128880));
        System.out.println("int128880 == integerValueOf128880 -> " + (int128880 == integerValueOf128880));
        System.out.println("int128880 == newInteger128880 -> " + (int128880 == newInteger128880));
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

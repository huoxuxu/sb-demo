package javat;

import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.other.HexUtil;
import com.hxx.sbcommon.common.other.encoder.BCD8421Util;
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
public class BCD8421Test {

    @Test
    public void test() {
        System.out.println("==============test==============");

        String mobile = "89";
        byte[] bytes = BCD8421Util.string2Bcd(mobile);
        String bcdStr = HexUtil.byteToHex(bytes);
        System.out.println("BCD: " + bcdStr);

        byte[] bcdBytes = HexUtil.hexToByte(bcdStr);
        String oriStr = BCD8421Util.bcd2String(bcdBytes);
        System.out.println("Ori: " + oriStr);

    }


}

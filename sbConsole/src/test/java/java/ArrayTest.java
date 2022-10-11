package demo;

import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.basic.OftenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class ArrayTest {

    @Test
    public void test() {
        System.out.println("==============test==============");
        String tmpPath = System.getProperty("java.io.tmpdir");
        System.out.println("临时目录：" + tmpPath);

        {
            String[] arr1 = {"ABC", "D1"};
            boolean arrFlag2 = OftenUtil.isEmptyArray(arr1);
            System.out.println("==>" + arrFlag2);

            Integer[] arr2;
            {
                arr2 = new Integer[0];
                boolean arr2Flag = OftenUtil.isEmptyArray(arr2);
                System.out.println("==>" + arr2Flag);
            }
            {
                arr2 = new Integer[1];
                boolean arr2Flag = OftenUtil.isEmptyArray(arr2);
                System.out.println("==>" + arr2Flag);
            }
        }
        {
            // 有问题
            int[] arr2;
            {
                arr2 = new int[0];
                boolean arr2Flag = OftenUtil.isEmptyArray(arr2);
                System.out.println("==>" + arr2Flag);
            }
            {
                arr2 = new int[1];
                boolean arr2Flag = OftenUtil.isEmptyArray(arr2);
                System.out.println("==>" + arr2Flag);
            }
        }

        System.out.println("");
    }

}

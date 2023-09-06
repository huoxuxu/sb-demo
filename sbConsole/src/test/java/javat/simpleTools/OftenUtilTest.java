package javat.simpleTools;

import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbConsole.module.easyExcel.EasyExcelDemo2;
import com.hxx.sbConsole.service.impl.CommonDataService;
import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.sbcommon.common.json.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class OftenUtilTest {

    @Test
    public void array2List() {
        String[] billCodes = new String[]{"1", "2 ", ""};
        List<String> list1 = Arrays.asList(billCodes);
        OftenUtil.assertCond(list1.size() != billCodes.length, "断言失败！");
    }

    @Test
    public void list2Array() {
        List<String> ls = Arrays.asList("1", "2 ", "");
        String[] billCodes = ls.toArray(new String[0]);

        OftenUtil.assertCond(ls.size() != billCodes.length, "断言失败！");
    }

    @Test
    public void CollectionUtil_partitionConsumer() throws Exception {
        System.out.println("==============test==============");
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
        OftenUtil.CollectionUtil.partitionConsumer(data, 4, d -> {
            System.out.println("partition: " + d.size() + " data: " + JsonUtil.toJSON(d));
        });
    }


}

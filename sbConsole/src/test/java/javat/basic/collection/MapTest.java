package javat.basic.collection;

import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.basic.OftenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Map
 * 可以多次put ，会用最后一次put值替换
 * getOrDefault 方法，key可以传null或者空字符串，如果key不存在，会返回orDefault
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class MapTest {

    @Test
    public void put() {
        /*
        * 结论：
        * 可以添加null键
        * 添加多个相同键会覆盖
        * */

        System.out.println("==============test==============");
        Map<String, String> map = new HashMap<>();
        map.put("", "998");
        map.put("", "9981");
        {
            String val = map.getOrDefault(null, "");
            OftenUtil.assertCond(!"".equals(val), "断言失败");
            System.out.println(val);
        }
        map.put(null, "99811");
        {
            String val = map.getOrDefault(null, "");
            OftenUtil.assertCond(!"99811".equals(val), "断言失败");
            System.out.println(val);
        }
    }

    @Test
    public void getOrDefault() {
        System.out.println("==============test==============");
        Map<String, String> map = new HashMap<>();
        {
            String val = map.getOrDefault("", "998");
            System.out.println(val);
        }
        {
            String val = map.getOrDefault(null, "998");
            System.out.println(val);
        }
    }


}

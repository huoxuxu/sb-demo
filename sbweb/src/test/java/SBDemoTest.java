import com.hxx.sbweb.SbwebApplication;
import com.hxx.sbweb.common.JsonUtil;
import com.hxx.sbweb.domain.Demo;
import com.hxx.sbweb.mapper.DemoMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-08-29 15:25:29
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SbwebApplication.class)
public class SBDemoTest {

    @Autowired
    private DemoMapper demoMapper;


    @Test
    public void testEntry() {
        List<Demo> data = demoMapper.selectAll();
        System.out.println(JsonUtil.toJSON(data));
        if (CollectionUtils.isEmpty(data)) return;

        Demo demo = demoMapper.selectByCode(data.get(0).getCode());
        System.out.println(JsonUtil.toJSON(demo));
    }

}

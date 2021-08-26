import com.hxx.mbtest.MbtestApplication;
import com.hxx.mbtest.entity.T1;
import com.hxx.mbtest.mapper.T1Mapper;
import com.hxx.sbcommon.common.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MbtestApplication.class)
public class DemoTest {

    @Test
    public void test() {
        System.out.println("==============test==============");

    }


}

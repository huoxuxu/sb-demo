package demo;

import com.hxx.sbConsole.SbConsoleApplication;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class DemoTest {

    @Test
    public void test() {
        System.out.println("==============test==============");
        String tmpPath = System.getProperty("java.io.tmpdir");
        System.out.println("临时目录：" + tmpPath);

        Class<Byte[]> cls1 = Byte[].class;
        Class<byte[]> cls2 = byte[].class;
        System.out.println(cls1);
        System.out.println(cls2);
    }


}

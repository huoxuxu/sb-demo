import com.hxx.mbtest.MbtestApplication;
import com.hxx.mbtest.service.impl.T1SelectServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MbtestApplication.class)
public class T1SelectServiceTest {

    @Autowired
    private T1SelectServiceImpl t1SelectService;

    @Test
    public void Run() {
        System.out.println("==============t1SelectService.Run==============");

        t1SelectService.run();

    }


}

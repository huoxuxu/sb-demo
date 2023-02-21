import com.hxx.mbtest.MbtestApplication;
import com.hxx.mbtest.service.T1Service;
import com.hxx.mbtest.service.impl.T1InsertServiceImpl;
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
public class T1InsertServiceTest {
    @Autowired
    private T1InsertServiceImpl t1Service;


    @Test
    public void addUser() {
        System.out.println("==============t1Service.addUser==============");

        t1Service.addUser();

    }

    @Test
    public void addUserDynamic() {
        System.out.println("==============t1Service.addUserDynamic==============");

        t1Service.addUserDynamic();

    }

    @Test
    public void insertBatch() {
        System.out.println("==============t1Service.insertBatch==============");

        t1Service.insertBatch();

    }


}

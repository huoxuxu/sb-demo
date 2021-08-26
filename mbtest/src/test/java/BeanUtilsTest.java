import com.hxx.mbtest.MbtestApplication;
import com.hxx.sbcommon.common.JsonUtil;
import models.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MbtestApplication.class)
public class BeanUtilsTest {

    @Test
    public void copyProperties() {
        System.out.println("==============test==============");
        Employee src = new Employee(1, "hxx", Employee.Gender.FEMALE, LocalDate.now(), 98,true);
        Employee tar = new Employee();

        BeanUtils.copyProperties(src, tar);
        System.out.println(JsonUtil.toJSON(tar));

    }


}

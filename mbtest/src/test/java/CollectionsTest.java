import com.hxx.mbtest.MbtestApplication;
import models.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MbtestApplication.class)
public class CollectionsTest {

    @Test
    public void test() {
        List<Employee> data = new ArrayList<>();
        {
            Employee emp = new Employee(0, "name1", Employee.Gender.FEMALE, LocalDate.now(), 98.7,true);
            data.add(emp);
        }
        {
            Employee emp = new Employee(0, "name1", Employee.Gender.FEMALE, LocalDate.MIN, 98.7,true);
            data.add(emp);
        }
        data.sort(Comparator.comparing(Employee::getCreateTime));
        System.out.println("==============test==============");

    }


}

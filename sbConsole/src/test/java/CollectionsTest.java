import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbConsole.model.Employee;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class CollectionsTest {

    @Test
    public void sort_LocalDate() {
        List<Employee> data = new ArrayList<>();
        {
            Employee emp = new Employee(0, "name1", Employee.Gender.FEMALE, LocalDate.now(), 98.7, true, new Date());
            data.add(emp);
        }
        {
            Employee emp = new Employee(0, "name1", Employee.Gender.FEMALE, LocalDate.MIN, 98.7, true, new Date());
            data.add(emp);
        }
        data.sort(Comparator.comparing(Employee::getCreateTime));

        data.stream().forEach(d -> System.out.println(d.getCreateTime()));
    }

    @Test
    public void partition() {
        List<String> tls = new ArrayList<>();
        tls.add("1");
        tls.add("2");
        tls.add("3");
        tls.add("4");
        tls.add("5");

        int batch = 2;
        for (int i = 0; i < tls.size(); i += batch) {
            int toIndex = i + batch >= tls.size() ? tls.size() : i + batch;
            List<String> data = tls.subList(i, toIndex);

            data.stream().forEach(d -> System.out.print(d));
            System.out.println();
        }
    }

    @Test
    public void list2Arr() {
        String[] billCodes = new String[]{"1", "2 ", ""};
        List<String> list = new ArrayList<>();
        for (int i = 0; i < billCodes.length; i++) {
            String item = billCodes[i].trim();
            if (!StringUtils.isEmpty(item)) {
                list.add(item);
            }
        }
        billCodes = new String[list.size()];
        billCodes = list.toArray(billCodes);

        for (int i = 0; i < billCodes.length; i++) {
            System.out.println(billCodes[i]);
        }
    }


}

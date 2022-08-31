import com.hxx.sbConsole.SbConsoleApplication;
import org.apache.ibatis.type.JdbcType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-30 14:19:06
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class EnumTest {

    @Test
    public void test() {
        System.out.println("==============test==============");
        {
            JdbcType jdbcType = JdbcType.ARRAY;
            System.out.println("enum ordinal: " + jdbcType.ordinal() + " name: " + jdbcType.name());
        }
        {
            JdbcType jdbcType = JdbcType.SMALLINT;
            System.out.println("enum ordinal: " + jdbcType.ordinal() + " name: " + jdbcType.name());
        }

    }


}

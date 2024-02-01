import com.hxx.mbtest.MbtestApplication;
import com.hxx.mbtest.entity.T1;
import com.hxx.mbtest.mapper.T1Mapper;
import com.hxx.sbcommon.common.io.json.JsonUtil;
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
public class T1MapperTest {
    @Autowired
    private T1Mapper T1Mapper;

    @Test
    public void findById() {
        System.out.println("==============findById==============");
        T1 t1 = T1Mapper.selectUserById(0);
        System.out.println(JsonUtil.toJSON(t1));
    }

    @Test
    public void selectAll() {
        System.out.println("==============selectAll==============");
        List<T1> list = T1Mapper.selectAll();

        System.out.println(JsonUtil.toJSON(list));
    }

    /**
     * 批量插入
     */
    @Test
    public void insertBatch() {
        List<T1> tls = new ArrayList<>();
        {
            T1 t = new T1();
            t.setCode("linghuchong");
            t.setName("令狐冲");
            t.setScore(90.0);
            t.setEnabled(true);

            LocalDateTime now = LocalDateTime.now();
            t.setBirthday(now.minusYears(1));
            t.setCreateTime(now);

            tls.add(t);
        }
        {
            T1 t = new T1();
            t.setCode("renyingying");
            t.setName("任盈盈");
            t.setScore(98.0);
            t.setEnabled(true);

            LocalDateTime now = LocalDateTime.now();
            t.setBirthday(now.minusYears(1));
            t.setCreateTime(now);

            tls.add(t);
        }


        int ret = T1Mapper.insertBatch(tls);
        System.out.println("==============insertBatch==============");
        System.out.println("影响行数：" + ret);

        // tls中id 会被赋值
        System.out.println(JsonUtil.toJSON(tls));
    }


}

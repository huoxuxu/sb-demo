package dal;

import com.hxx.sbMyBatis.SbMyBatisApplication;
import com.hxx.sbMyBatis.dal.entity.DemoTab;
import com.hxx.sbMyBatis.dal.mapper.DemoTabMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-09-01 9:29:04
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SbMyBatisApplication.class)
public class DemoTabMapperTest {

    @Autowired
    private DemoTabMapper demoTabMapper;

    @Test
    public void testCase0() {
        List<DemoTab> ls = demoTabMapper.selectAll();
        System.out.println(ls);
    }

    @Test
    public void testCase1() {
        List<Map<String, String>> maps = demoTabMapper.selectMap();
        System.out.println(maps);
    }

}

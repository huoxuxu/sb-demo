package java.resources;

import com.hxx.sbConsole.SbConsoleApplication;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class ResTest {

    @Test
    public void test() {
        System.out.println("==============test==============");

    }

    @Test
    public void loadResFile() throws IOException {
        //获取文件的URL
        File file = ResourceUtils.getFile("classpath:json/person.json");
        System.out.println("文件:" + file.getPath());
        //转成string输入文本
        String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        System.out.println("内容：" + content);
    }


}

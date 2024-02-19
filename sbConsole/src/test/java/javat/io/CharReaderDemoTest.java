package javat.io;

import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.io.cfg.ResourcesUtil;
import com.hxx.sbcommon.common.io.reader.CharReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class CharReaderDemoTest {

    @Test
    public void test() throws IOException {
        System.out.println("==============test==============");
        String path1 = "demo/1.txt";
        {
            try (InputStream resStream = ResourcesUtil.getResourceStream(path1)) {
                try (InputStreamReader isr = new InputStreamReader(resStream)) {
                    CharReader charReader = new CharReader(isr, 3);
                    while (true) {
                        int position = charReader.getPosition();
                        char next = charReader.next();
                        if (next == (char) -1) {
                            break;
                        }

                        // show
                        System.out.println(position + ": " + show(next) + " ");
                    }
                }
            }
        }
        System.out.println("ok");
    }

    private String show(char ch) {
        if (ch == '\r') {
            return "\\r";
        } else if (ch == '\n') {
            return "\\n";
        } else if (ch == '\t') {
            return "\\t";
        } else {
            return ch + "";
        }
    }

}

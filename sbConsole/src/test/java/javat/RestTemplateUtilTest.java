package javat;

import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.json.JsonUtil;
import com.hxx.sbcommon.common.http.restTemplate.RestTemplateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class RestTemplateUtilTest {

    @Test
    public void test() {
        System.out.println("==============test==============");

    }

    @Test
    public void get() {
        System.out.println("==============get==============");
        String url = "http://localhost:8089/ServerInfo/GetRequestInfo?a=1&b=2";
        ResponseEntity<String> resp = RestTemplateUtil.get(url);
        String str = JsonUtil.toJSON(resp);
        pl(str);
    }

    @Test
    public void getHttps() {
        System.out.println("==============get==============");
        String url = "https://www.jianshu.com/p/90ec27b3b518";
        ResponseEntity<String> resp = RestTemplateUtil.get(url);
        String str = JsonUtil.toJSON(resp);
        pl(str);
    }

    @Test
    public void post() {
        System.out.println("==============get==============");
        String url = "http://localhost:8089/ServerInfo/GetRequestInfo?a=1&b=2";
        ResponseEntity<String> resp = RestTemplateUtil.postWithJson(url, "{\"id\":99}", header -> {
            header.set("abv", "123321");
        });
        String str = JsonUtil.toJSON(resp);
        pl(str);
    }

    @Test
    public void postKV() {
        System.out.println("==============get==============");
        String url = "http://localhost:8089/ServerInfo/GetRequestInfo?a=1&b=2";
        Map<String, Object> map = new HashMap<>();
        map.put("TT","11");
        map.put("TTw","1122");
        ResponseEntity<String> resp = RestTemplateUtil.postWithKV(url, map, header -> {
            header.set("abv", "123321");
        });
        String str = JsonUtil.toJSON(resp);
        pl(str);
    }


    private static void pl(String msg) {
        System.out.println(msg);
    }
}

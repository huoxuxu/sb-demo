package javat.modules.io.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import lombok.var;
import models.generics.GenericsChildVO;
import models.generics.GenericsVO;
import models.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@var
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class JsonArrayTest {

    @Test
    public void parseMap() {
        System.out.println("==============test==============");
        String json = getJson();
        HashMap<String, String> map = JsonUtil.parseMap(json);
        System.out.println(JsonUtil.toJSON(map));

        System.out.println("ok");
    }

    @Test
    public void parseGenerics() {
        System.out.println("==============test==============");
        String json = getGenericsJson();
        var result = JSONObject.parseObject(json, new TypeReference<GenericsVO<GenericsChildVO<Long>>>() {
        });
        System.out.println(JsonUtil.toJSON(result));

// 或者type的构造中使用参数
//        EnjoyResponseDTO<EnjoyPageResponseDataDTO<T>> result = JSONObject.parseObject(str, new TypeReference<EnjoyResponseDTO<EnjoyPageResponseDataDTO<T>>>(Long.class) {
//        });


        System.out.println("ok");
    }

    private String getJson() {
        Person person = new Person();
        person.setId(1);
        person.setName("娃哈哈");
        person.setAge(18);
        return JsonUtil.toJSON(person);
    }

    String getGenericsJson() {
        GenericsVO<GenericsChildVO<Long>> vo = new GenericsVO<>();
        GenericsChildVO<Long> cvo = new GenericsChildVO<>();
        cvo.setData(1999L);
        vo.setData(cvo);
        return JsonUtil.toJSON(vo);
    }


}

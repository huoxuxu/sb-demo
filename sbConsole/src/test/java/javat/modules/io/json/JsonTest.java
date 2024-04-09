package javat.modules.io.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import lombok.var;
import models.*;
import models.generics.GenericsChildVO;
import models.generics.GenericsImplVO;
import models.generics.GenericsLongImplVO;
import models.generics.GenericsVO;
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
public class JsonTest {

    public static void main(String[] args) {
        {

            // 使用匿名内部类获取范型类型
            TypeReference<GenericsVO<GenericsChildVO<Long>>> typeReference = new TypeReference<GenericsVO<GenericsChildVO<Long>>>() {
            };
            System.out.println(typeReference);
        }
        {

        }
        {
            GenericsLongImplVO a = new GenericsLongImplVO();
            System.out.println(a.getId() + "_" + a.getInsId());

            GenericsImplVO<Long> longGenericsImplVO = new GenericsImplVO<>();
            System.out.println(longGenericsImplVO.getId() + "_" + longGenericsImplVO.getInsId());
            // 范型基类的静态字段，共享
        }


        System.out.println("ok");
    }

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

        System.out.println("ok");
    }

    @Test
    public <T> void parseGenericsT() {
        System.out.println("==============test==============");
        String json = getGenericsJson();
        // 如果参数中有编译时未知的，执行时才知道的泛型（一般是用来继承中使用泛型），则使用有参构造方法就很有必有了，
        // 可以在使用有参的构造之前，获取实际的泛型，作为参数构造出TypeReference
        //如果 T 是 泛型变量 ， 没传后面的修饰参数 T 会被识别成 T 的上边界（ 根据 T的定义 如果： <T extends Number> T 被识别为Number，如果是<T> T被识别为 Object）
        GenericsVO<GenericsChildVO<T>> result = JSONObject.parseObject(json, new TypeReference<GenericsVO<GenericsChildVO<T>>>(Long.class) {
        });
        System.out.println(JsonUtil.toJSON(result));

        System.out.println("ok");
    }

    @Test
    public void parseGenericsMore() {
        System.out.println("==============test==============");
        String json = getGenericsJson2();

        GenericsImplVO<Long> a = new GenericsImplVO<>();
        GenericsVO<Long> data = a.getData(json);
        System.out.println(data.getData());

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

    String getGenericsJson2() {
        GenericsVO<Long> vo = new GenericsVO<>();
        vo.setData(1999L);
        return JsonUtil.toJSON(vo);
    }

}

package com.hxx.sbweb.controller.webDemo.bindReqPara;

import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import com.hxx.sbweb.controller.base.BaseRestController;
import com.hxx.sbweb.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 请求参数映射示例
 * 不使用@RequestParam，要求request传入参数名称和 controller方法的形参名称一致，方可绑定成功
 *
 * @RequestParam注解 用来处理Content-Type: 为 application/x-www-form-urlencoded编码的内容。提交方式为get或post。
 * （Http协议中，form的enctype属性为编码方式，常用有两种：application/x-www-form-urlencoded和multipart/form-data，默认为application/x-www-form-urlencoded）；
 * @RequestBody注解 用来处理HttpEntity（请求体）传递过来的数据，一般用来处理非Content-Type: application/x-www-form-urlencoded编码格式的数据；
 * GET请求中，因为没有HttpEntity，所以@RequestBody并不适用；
 * POST请求中，通过HttpEntity传递的参数，必须要在请求头中声明数据的类型Content-Type，SpringMVC通过使用HandlerAdapter配置的HttpMessageConverters来解析HttpEntity中的数据，然后绑定到相应的bean上。
 */
@Slf4j
@RestController
@RequestMapping("/reqpara")
public class RequestParaController extends BaseRestController {

    // =================================GET====================================

    /**
     * 请求参数名和方法参数名一致
     * http://localhost:8082/reqpara/get?p=jay1
     *
     * @param p
     * @return
     */
    @GetMapping("/get")
    public String get(String p) {
        return "name is:" + p;
    }

    /**
     * 请求参数名和方法参数名不一致
     * http://localhost:8082/reqpara/getNoSame?name=jay
     *
     * @param str
     * @return
     */
    @GetMapping("/getNoSame")
    public String getNoSame(@RequestParam(value = "name") String str) {
        return "name is:" + str;
    }

    /**
     * 请求参数名和方法名不一致且包含默认值
     * http://localhost:8082/reqpara/getParaDefault
     *
     * @param str
     * @return
     */
    @GetMapping("/getParaDefault")
    public String getnameWithDefaultParam(@RequestParam(value = "name", defaultValue = "sx1999") String str) {
        return "name is:" + str;
    }

    /**
     * /reqpara/model?id=1&name=2
     *
     * @param u
     * @return
     */
    @GetMapping("/model")
    public String getModel(User u) {
        return JsonUtil.toJSON(u);
    }

    /**
     * /reqpara/arr?kk=1&kk=2
     *
     * @param kk
     * @return
     */
    @GetMapping("/arr")
    public String getArr(String[] kk) {
        return StringUtils.join(kk, ",");
    }

    /**
     * /reqpara/list?kk=1&kk=2
     * 必须使用@RequestParam 修饰入参
     *
     * @param kk
     * @return
     */
    @GetMapping("/list")
    public String getList(@RequestParam List<String> kk) {
        return StringUtils.join(kk, ",");
    }

    /**
     * /reqpara/dataParam?date=2088/08/08&date1=2088-08-18&date2=2088/08/28 8:08:08
     *
     * @param date
     * @param date1
     * @param date2
     * @return
     */
    @RequestMapping("/dataParam")
    public String dataParam(Date date,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date date1,
                            @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss") Date date2) {
        System.out.println("参数传递 date ==> " + OftenUtil.DateTimeUtil.fmt2Str(date));
        System.out.println("参数传递 date1(yyyy-MM-dd) ==> " + OftenUtil.DateTimeUtil.fmt2Str(date1));
        System.out.println("参数传递 date2(yyyy/MM/dd HH:mm:ss) ==> " + OftenUtil.DateTimeUtil.fmt2Str(date2));
        return JsonUtil.toJSON(Arrays.asList(OftenUtil.DateTimeUtil.fmt2Str(date),
                OftenUtil.DateTimeUtil.fmt2Str(date1),
                OftenUtil.DateTimeUtil.fmt2Str(date2)));
    }

}

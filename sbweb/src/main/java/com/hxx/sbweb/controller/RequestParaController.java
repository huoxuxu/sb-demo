package com.hxx.sbweb.controller;

import com.hxx.sbweb.common.ResultHandler;
import com.hxx.sbweb.domain.User;
import com.hxx.sbweb.model.ResultBean;
import com.hxx.sbweb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

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
public class RequestParaController {

    // =================================GET====================================

    /**
     * 请求参数名和方法参数名不一致
     * http://localhost:8082/reqpara/getname?name=jay
     *
     * @param str
     * @return
     */
    @GetMapping("/getname")
    public String getname(@RequestParam(value = "name") String str) {
        return "name is:" + str;
    }

    /**
     * 请求参数名和方法参数名一致
     * http://localhost:8082/reqpara/getname2?str=jay1
     *
     * @param str
     * @return
     */
    @GetMapping("/getname2")
    public String getname2(String str) {
        return "name is:" + str;
    }

    /**
     * 请求参数名和方法名不一致且包含默认值
     * http://localhost:8082/reqpara/get_default
     *
     * @param str
     * @return
     */
    @GetMapping("/get_default")
    public String getnameWithDefaultParam(@RequestParam(value = "name", defaultValue = "sx1999") String str) {
        return "name is:" + str;
    }

    /**
     * 映射URLPath参数
     * http://localhost:8082/reqpara/id/199
     *
     * @param id
     * @return
     */
    @GetMapping("/id/{id}")
    public String id(@PathVariable("id") Integer id) {
        return "id:" + id;
    }

    // =================================POST====================================

    /**
     * http://localhost:8082/reqpara/reqJson
     * {"id":99,"name":"sx1999"}
     *
     * @param u
     * @return
     */
    @PostMapping("/reqJson")
    public String reqJson(@Valid @RequestBody User u) {
        return u.getId() + "_" + u.getName();
    }

    /**
     * http://localhost:8082/reqpara/reqList
     * [1,22,33]
     *
     * @param ids
     * @return
     */
    @PostMapping("/reqList")
    public String req1(@RequestBody List<Integer> ids) {
        return ids + "";
    }

    @RequestMapping("/testUrlPathParam/{param1}/{param2}")
    public String testUrlPathParam(@PathVariable String param1,
                                   @PathVariable String param2) {
        System.out.println("通过PathVariable获取的参数param1=" + param1);
        System.out.println("通过PathVariable获取的参数param2=" + param2);
        return "";
    }

    @RequestMapping("/testHeaderParam")
    public void testHeaderParam(@RequestHeader String param1) {
        System.out.println("通过RequestHeader获取的参数param1=" + param1);
    }

    @RequestMapping("/testCookieParam")
    public void testCookieParam(HttpServletRequest request, HttpServletResponse response,
                                @CookieValue String sessionid) {
        System.out.println("通过CookieValue获取的参数sessionid=" + sessionid);
    }

    @RequestMapping("/testHttpEntity")
    public ResponseEntity<String> handle(HttpEntity<byte[]> requestEntity) throws UnsupportedEncodingException {
        String requestHeader = requestEntity.getHeaders()
                .getFirst("MyRequestHeader");
        byte[] requestBody = requestEntity.getBody();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("MyResponseHeader", "MyValue");
        return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED);
    }

    /**
     * 只在当前的Controller里面起作用
     * Valid特性 会抛出此异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object exceptionHandler(MethodArgumentNotValidException e) {
        log.error("请求参数缺失错误，msg:{}", e.getMessage());
        List<ObjectError> errorList = e.getBindingResult()
                .getAllErrors();
        StringBuffer sb = new StringBuffer();
        for (ObjectError objectError : errorList) {
            sb.append(objectError.getDefaultMessage())
                    .append(",");
        }
        return ResultHandler.error("PARAM_ERROR" + sb);
    }

    /**
     * 只在当前的Controller里面起作用
     * RequestParam特性 会抛出此异常
     *
     * @param ex
     * @param request
     * @param response
     * @throws IOException
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void processMethod(MissingServletRequestParameterException ex,
                              HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        System.out.println("抛异常了！" + ex.getLocalizedMessage());
        log.error("抛异常了！" + ex.getLocalizedMessage());
        response.getWriter()
                .printf(ex.getMessage());
        response.flushBuffer();
    }

    /**
     * 异常页面控制，会跳转到对应的ModelAndView
     *
     * @param runtimeException
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public String runtimeExceptionHandler(RuntimeException runtimeException,
                                          ModelMap modelMap) {
        log.error(runtimeException.getLocalizedMessage());

        modelMap.put("status", false);
        return "exception";
    }

}

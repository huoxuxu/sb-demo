package com.hxx.sbweb.controller;

import com.hxx.sbweb.common.ResultHandler;
import com.hxx.sbweb.domain.User;
import com.hxx.sbweb.model.ResultBean;
import com.hxx.sbweb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * 请求参数映射示例
 */
@Slf4j
@RestController
@RequestMapping("/reqpara")
public class RequestParaController {

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
     * http://localhost:8082/reqpara/getname2?str=jay
     *
     * @param str
     * @return
     */
    @GetMapping("/getname2")
    public String getname2(@RequestParam String str) {
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
     * 请求ContentType必须是Json
     * http://localhost:8082/reqpara/reqJson
     * {"id":99,"name":"sx1999"}
     *
     * @param u
     * @return
     */
    @RequestMapping("/reqJson")
    public String reqJson(@Valid @RequestBody User u) {
        return u.getId() + "_" + u.getName();
    }

    /**
     * 映射URLPath参数
     * http://localhost:8082/reqpara/id/199
     *
     * @param id
     * @return
     */
    @RequestMapping("/id/{id}")
    public String id(@PathVariable("id") Integer id) {
        return "id:" + id;
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
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        StringBuffer sb = new StringBuffer();
        for (ObjectError objectError : errorList) {
            sb.append(objectError.getDefaultMessage()).append(",");
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
        response.getWriter().printf(ex.getMessage());
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

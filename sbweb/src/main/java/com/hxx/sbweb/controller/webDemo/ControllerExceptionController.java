package com.hxx.sbweb.controller.webDemo;

import com.hxx.sbcommon.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/controllererr")
public class ControllerExceptionController {

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
        return Result.failed("PARAM_ERROR" + sb);
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

    @RequestMapping("/get")
    public String get(int k) throws Exception {
        int a = 99 - 98 - 1;
        if (k == 1) {
            throw new Exception("123");
        } else if (k == 2) {
            throw new IOException("234");
        }

        return "ook";
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

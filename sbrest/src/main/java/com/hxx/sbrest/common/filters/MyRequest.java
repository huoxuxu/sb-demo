package com.hxx.sbrest.common.filters;

import com.hxx.sbrest.common.config.MyFilterOneConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-30 10:02:28
 **/
public class MyRequest extends HttpServletRequestWrapper {

    public MyRequest(HttpServletRequest request) {
        super(request);
    }

    /**
     * 重写getParameter方法
     * 对请求结果进行过滤
     *
     * @param name
     * @return
     */
    public String getParameter(String name) {
        return  MyFilterOneConfig.filter(super.getRequest().getParameter("name"));
    }

    /**
     * 重写getParameterValues方法
     * 通过循环取出每一个请求结果,再对请求结果进行过滤
     *
     * @param name
     * @return
     */
    public String[] getParameterValues(String name) {
        String values[]=super.getRequest().getParameterValues("name");
        for(int i=0;i<values.length;i++) {
            values[i]=MyFilterOneConfig.filter(values[i]);
        }
        return values;
    }


}

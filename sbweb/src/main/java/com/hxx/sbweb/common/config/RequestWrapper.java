//package com.hxx.sbweb.common.config;
//
//import javax.servlet.ServletInputStream;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
//import java.io.*;
//
///**
// * 包装类，HttpServletRequestWrapper实现HttpServletRequest
// * @Author: huoxuxu
// * @Description:
// * @Date: 2023-09-21 13:10:57
// **/
//public class RequestWrapper  extends HttpServletRequestWrapper {
//
//    private final String body;
//
//    public RequestWrapper(HttpServletRequest request) throws IOException {
//        super(request);
//        StringBuilder stringBuilder = new StringBuilder();
//        BufferedReader bufferedReader = null;
//        try {
//            InputStream inputStream = request.getInputStream();
//            if (inputStream != null) {
//                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                char[] charBuffer = new char[128];
//                int bytesRead = -1;
//                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
//                    stringBuilder.append(charBuffer, 0, bytesRead);
//                }
//            } else {
//                stringBuilder.append("");
//            }
//        } catch (IOException ex) {
//            throw ex;
//        } finally {
//            if (bufferedReader != null) {
//                try {
//                    bufferedReader.close();
//                } catch (IOException ex) {
//                    throw ex;
//                }
//            }
//        }
//        body = stringBuilder.toString();
//    }
//
//    //每个getInputStream的方法都是根据body字符串创建新输入流
//    @Override
//    public ServletInputStream getInputStream() throws IOException {
//        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
//        ServletInputStream servletInputStream = new ServletInputStream() {
//            public int read() throws IOException {
//                return byteArrayInputStream.read();
//            }
//        };
//        return servletInputStream;
//    }
//
//    //每个getBufferReader的方法都是根据body字符串创建新Reader读取流
//    @Override
//    public BufferedReader getReader() throws IOException {
//        return new BufferedReader(new InputStreamReader(this.getInputStream()));
//    }
//
//    //获取body字符串，单例
//    public String getBody() {
//        return this.body;
//    }
//}
package com.hxx.sbConsole.dutyChain;

import com.hxx.sbConsole.dutyChain.base.AbstractServiceHandler;
import com.hxx.sbConsole.dutyChain.base.IFilterCallback;
import com.hxx.sbConsole.model.ChainRequest;
import com.hxx.sbConsole.model.ChainResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-07 11:27:56
 **/
@Slf4j
public class AddUserServiceHandler extends AbstractServiceHandler {

    @Override
    public void handle(ProcessContext processContext, IFilterCallback callback)
            throws Exception {
        // 设置响应内容
        ChainRequest req = (ChainRequest) processContext.getRequest();
        ChainResponse resp = (ChainResponse) processContext.getResponse();
        resp.setErrCode("SUCCESS");
        resp.setErrMsg("添加用户成功");
        resp.setUserName(req.getUserName());
        resp.setAge(req.getAge());

        // 此处为一个完整服务正向责任链的执行终点，此处执行回调，逆向执行责任链
        callback.onPostProcess(processContext);
    }
}
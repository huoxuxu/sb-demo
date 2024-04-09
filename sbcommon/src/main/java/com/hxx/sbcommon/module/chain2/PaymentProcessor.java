package com.hxx.sbcommon.module.chain2;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 17:05:44
 **/
public interface PaymentProcessor {
    /**
     * 节点处理
     *
     * @param context
     */
    void handle(Payment context);
}

package com.hxx.sbcommon.module.chain2.demo;

import com.hxx.sbcommon.module.chain2.AbstractPaymentProcessor;
import com.hxx.sbcommon.module.chain2.Payment;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 17:01:47
 **/
public class PayPal2Processor extends AbstractPaymentProcessor {
    @Override
    public void doHandler(Payment content) throws Exception {
        System.out.println("Processed PayPal payment.");
    }
}

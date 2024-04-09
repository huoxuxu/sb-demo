package com.hxx.sbcommon.module.chain2.demo;

import com.hxx.sbcommon.module.chain2.AbstractPaymentProcessor;
import com.hxx.sbcommon.module.chain2.Payment;
import com.hxx.sbcommon.module.chain2.PaymentHandleChainService;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 17:02:13
 **/
public class PaymentPricessorClient {
    public static void main(String[] args) throws Exception {

        new AbstractPaymentProcessor.Builder()
                .addHandler(new CreditCard2Processor())
                .addHandler(new PayPal2Processor())
                .build().execute(new Payment());
    }
}

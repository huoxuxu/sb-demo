package com.hxx.sbcommon.module.chain2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 17:04:05
 **/
public class PaymentHandleChainService {

    public void execute(Payment payment) throws Exception {
        List<AbstractPaymentProcessor> paymentProcessors = new ArrayList<>();

        for (AbstractPaymentProcessor paymentProcessor : paymentProcessors) {
            paymentProcessor.doHandler(payment);
        }
    }
}

package com.hxx.sbrest.common.properties;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-30 15:02:33
 **/
public class ValidateCodeProperties {
    private SmsCodeProperties sms = new SmsCodeProperties();


    public SmsCodeProperties getSms() {
        return sms;
    }

    public void setSms(SmsCodeProperties sms) {
        this.sms = sms;
    }


}

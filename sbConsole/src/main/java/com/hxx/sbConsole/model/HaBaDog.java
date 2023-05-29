package com.hxx.sbConsole.model;

import lombok.Data;

/**
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-06-14 9:34:16
 **/
@Data
public class HaBaDog extends Dog{
    private String name;

    private boolean haba;

    public HaBaDog(){
        score=1;
    }

    public void demo(){
        this.demoMethod();
    }

    // get&set==================
    public boolean isHaba() {
        return haba;
    }

    public void setHaba(boolean haba) {
        this.haba = haba;
    }

}

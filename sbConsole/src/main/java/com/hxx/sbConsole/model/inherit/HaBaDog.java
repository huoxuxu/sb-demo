package com.hxx.sbConsole.model.inherit;

import com.hxx.sbConsole.model.inherit.Dog;
import lombok.Data;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-06-14 9:34:16
 **/
public class HaBaDog extends Dog implements IDog {

    private boolean haba = true;


    public HaBaDog() {
        score = 1;
    }

    public void demo() {
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

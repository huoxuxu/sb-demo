package com.hxx.sbConsole.model.inherit;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 15:57:32
 **/
@lombok.Data
public class Dog implements IDog {
    private String name;
    private String age;
    private boolean enabled;

    // 默认 protected
    protected int score;

    // 默认 protected
    void demoMethod() {
    }

    @Override
    public void wang() {
        System.out.println("汪汪...");
    }

    // get&set
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

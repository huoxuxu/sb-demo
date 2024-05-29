package com.hxx.hdblite.model;

/**
 * init-method="init" destroy-method="destory"
 */
//@Bean(initMethod = "init",destroyMethod = "destory")
public class Person {

    private String name;
    private int age;
    private double weight;

    public Person() {
        System.out.println("执行person构造方法");
    }

    public void init() {
        System.out.println("执行person初始化方法");
    }

    public void destory() {
        System.out.println("执行person销毁方法");
    }

    public void fun() {
        System.out.println("正在使用person实例");
    }

    //get&set
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

代理
实现方式：
 1.继承
 2.组合

继承
源码：
package com.example.demo.proxy;

public class HouseOwner {
    public void rent() {
        System.out.println("和房东签署协议，租房完成！");
    }
}

public class Intermediary extends HouseOwner {
    public void rent() {
        System.out.println("找房....");
        super.rent();
        System.out.println("收房租....");
    }
}

public class Test {
    public static void main(String[] args) {
        HouseOwner houseOwner = new Intermediary();
        houseOwner.rent();
    }
}

组合
源码：
package com.example.demo.proxy;

public interface Owner {
    void rent();
}

public class HouseOwner implements Owner {
    public void rent() {
        System.out.println("和房东签署协议，租房完成！");
    }
}

public class Intermediary implements Owner {

    private Owner owner;

    public Intermediary(Owner owner) {
        this.owner = owner;
    }

    public void rent() {
        System.out.println("找房....");
        owner.rent();
        System.out.println("收房租....");
    }
}

public class Test {
    public static void main(String[] args) {
        Owner owner = new Intermediary(new HouseOwner());
        owner.rent();
    }
}

JDK动态代理自动生成的类
源码：

package com.example.demo.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

public final class $Proxy0 extends Proxy implements Owner {
    private static Method m1;
    private static Method method;

    static {
        try {
            m1 = Class.forName("java.lang.Object").getMethod("equals", new Class[] { Class.forName("java.lang.Object") });
            method = Class.forName("com.example.demo.proxy.Owner").getMethod("rent");
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public $Proxy0(InvocationHandler invocationHandler) {
        super(invocationHandler);
    }

    public final boolean equals(Object var1) throws  {
        return (Boolean)super.h.invoke(this, m1, new Object[]{var1});
    }

    public final void rent() {
        super.h.invoke(this, method, new Object[]{});
    }
}









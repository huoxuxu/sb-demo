package models.generics;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2024-01-10 13:13:04
 **/
@Data
public class GenericsLongImplVO extends BaseGenericsVO<Long> implements Serializable {
    private static final long serialVersionUID = 1347039272844558668L;

    private Type[] genericTypes;
    private Type genericType;

    private long val;


    public GenericsLongImplVO() {
        // 获取带有泛型参数的Class对象
        Class cls = getClass();
        Type superClass = cls.getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            this.genericTypes = ((ParameterizedType) superClass).getActualTypeArguments();
            if (this.genericTypes.length > 0 && this.genericTypes[0] instanceof Class) {
                Type actualTypeArgument = this.genericTypes[0];
                this.genericType = actualTypeArgument;
                System.out.println(actualTypeArgument);
            }
        }
    }

}

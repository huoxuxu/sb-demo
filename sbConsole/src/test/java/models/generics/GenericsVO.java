package models.generics;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2024-01-08 16:56:51
 **/
@Data
public class GenericsVO<T> implements Serializable {
    private static final long serialVersionUID = 981029545873110483L;

    private T data;

}

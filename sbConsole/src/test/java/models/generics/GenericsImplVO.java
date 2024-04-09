package models.generics;

import lombok.Data;
import models.Person;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2024-01-08 17:31:57
 **/
@Data
public class GenericsImplVO<T> extends BaseGenericsVO<T> implements Serializable {
    private static final long serialVersionUID = -1496066348250714104L;

}

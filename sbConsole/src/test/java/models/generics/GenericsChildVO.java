package models.generics;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2024-01-08 16:58:41
 **/
@Data
public class GenericsChildVO<T> implements Serializable {
    private static final long serialVersionUID = 3995694471465924563L;

    private T data;
}

package models.generics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.UUID;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2024-01-08 16:56:51
 **/
@Data
public abstract class BaseGenericsVO<T> implements Serializable {
    private static final long serialVersionUID = 981029545993110483L;

    private static final String ID = UUID.randomUUID() + "";

    private final String insId;

    private T data;

    protected BaseGenericsVO() {
        insId = UUID.randomUUID() + "";
    }

    public String getId() {
        return ID;
    }

    public String getInsId() {
        return insId;
    }

    public GenericsVO<T> getData(String str) {
        /* 此时T未知类型，不能直接使用无参，会导致T类型变成JSONObject */
        return JSON.parseObject(str, new TypeReference<GenericsVO<T>>(getType(0)) {
        });
    }

    /* 获取泛型的实际类型 */
    protected Type getType(int index) {
        Type superClass = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) superClass).getActualTypeArguments()[index];
        return type;
    }
}

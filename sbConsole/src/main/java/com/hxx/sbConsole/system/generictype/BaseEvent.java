package com.hxx.sbConsole.system.generictype;

import com.hxx.sbcommon.common.reflect.BeanTypeUtil;
import lombok.Data;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

@Data
public class BaseEvent<T> implements ResolvableTypeProvider {

    private T data;

    public BaseEvent(T data) {
        this.data = data;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forInstance(getData());
    }

}

package com.hxx.sbConsole.service.common;

import com.hxx.sbConsole.commons.annotation.ConditionalOnFrequency;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-03 16:27:18
 **/
public class OnFrequencyConditional extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 获取注解 ConditionalOnFrequency 上设置的属性值
        Map<String, Object> annAttrs = metadata.getAnnotationAttributes(ConditionalOnFrequency.class.getName());
        Object value = annAttrs.get("value");
        Object havingValue = annAttrs.get("havingValue");

        // 以下代码为自定义匹配规则
        if (value == null || havingValue == null) {
            return new ConditionOutcome(false, "属性值为空");
        }

        // 如果在application.properties 中有配置 frequency 那么这里就可以获取到
        Environment environment = context.getEnvironment();
        String v = environment.getProperty(value.toString());
        // 判断 application.properties 中配置值与在 FrequencyAutoConfiguration 上定义的值是否一致。
        if (havingValue.equals(v)) {
            // 如果 environment 中的值与指定的 value 一致，则返回true
            return new ConditionOutcome(true, "匹配");
        }

        return new ConditionOutcome(false, "不匹配");
    }

}

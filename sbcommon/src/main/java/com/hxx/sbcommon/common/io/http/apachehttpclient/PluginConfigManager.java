package com.hxx.sbcommon.common.io.http.apachehttpclient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.*;

public class PluginConfigManager {
    private static final String APP_CAT_PROPERTIES_CLASSPATH = "classpath*:META-INF/titans/plugin/**";
    private static final Map<String, Properties> CANT_PROPERTIES_MAP = new HashMap<>();

    static {
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        try {
            //获取资源
            Resource[] resources = patternResolver.getResources(APP_CAT_PROPERTIES_CLASSPATH);
            for (Resource resource : resources) {
                Properties props = new Properties();
                //加载资源
                props.load(resource.getInputStream());
                String fileName = resource.getFilename();
                if (CANT_PROPERTIES_MAP.containsKey(fileName)) {
                    Properties properties = CANT_PROPERTIES_MAP.get(fileName);
                    Iterator<Map.Entry<Object, Object>> it = props.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<Object, Object> entry = it.next();
                        properties.setProperty((String) entry.getKey(), (String) entry.getValue());
                    }
                } else {
                    //装载资源
                    CANT_PROPERTIES_MAP.put(fileName, props);
                }
            }

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 获取指定文件名内插件对应的类包装列表，排序
     *
     * @param fileName 插件对应的文件名
     * @param reverse  true: 优先级由高到低， false: 优先级由低到高
     * @return
     */
    public static List<PluginDecorator<Class>> getOrderedPluginClasses(String fileName, boolean reverse) {
        Set<String> classNames = getPropertyValueSet(fileName);
        return getOrderedPluginClass(classNames, reverse);
    }


    /**
     * 根据指定类名得到排序的插件类
     *
     * @param classNames
     * @param reverse    true:order大的在前面  false: order小的在前面
     * @return
     */
    public static List<PluginDecorator<Class>> getOrderedPluginClass(Set<String> classNames, boolean reverse) {
        if (classNames == null || classNames.isEmpty()) {
            return new ArrayList<>();
        }
        List<PluginDecorator<Class>> result = new ArrayList<>();

        for (String className : classNames) {
            String errorClassNames = "";
            try {
                Class<?> clazz = Class.forName(className);
                TitansPlugin tp = clazz.getAnnotation(TitansPlugin.class);
                PluginDecorator pd = null;
                if (tp != null) {
                    pd = new PluginDecorator(clazz, tp.order());
                } else {
                    pd = new PluginDecorator(clazz, 0);
                }
                result.add(pd);
            } catch (Exception e) {
                errorClassNames = errorClassNames + className + ",";
            }
            if (!StringUtils.isEmpty(errorClassNames)) {
                throw new IllegalArgumentException("Can't init " + errorClassNames + " class");
            }
        }

        if (!result.isEmpty()) {
            if (reverse) {
                Collections.sort(result);
                Collections.reverse(result);
            } else {
                Collections.sort(result);
            }
        }
        return result;
    }

    /**
     * 获取properties 文件全部value
     *
     * @param fileName 文件名
     * @return value-SET
     */
    public static Set<String> getPropertyValueSet(String fileName) {
        Set<String> setStr = new HashSet<>();
        Properties property = getProperty(fileName);
        if (property != null) {
            Set<String> strings = property.stringPropertyNames();
            for (String key : strings) {
                setStr.add(property.get(key).toString());
            }
        }
        return setStr;
    }

    /**
     * 获取K-V对象Properties
     *
     * @param filName 文件名
     * @return Properties
     */
    public static Properties getProperty(String filName) {
        Properties properties = CANT_PROPERTIES_MAP.get(filName);
        return properties;
    }

}

package com.hxx.sbConsole.module.dynamicproxy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName RepositoryScanner
 * @Description 扫描类并将其实例化注入IOC容器
 * @Author Silwings
 * @Date 2021/3/7 16:09
 * @Version V1.0
 **/
@Component
public class RepositoryScanner implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware, ApplicationContextAware {
    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    private MetadataReaderFactory metadataReaderFactory;
    private ResourcePatternResolver resourcePatternResolver;
    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        // 获取启动类所在包
        List<String> packages = AutoConfigurationPackages.get(applicationContext);
        // 开始扫描包，获取字节码
        Set<Class<?>> beanClazzSet = scannerPackages(packages.get(0));
        for (Class beanClazz : beanClazzSet) {
            // 判断是否是需要被代理的接口
            if (isNotNeedProxy(beanClazz)) {
                continue;
            }
            // BeanDefinition构建器
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();

            //在这里，我们可以给该对象的属性注入对应的实例。
            definition.getConstructorArgumentValues()
                    .addGenericArgumentValue(beanClazz);
            // 如果构造函数中不止一个值,可以使用这个api,指定是第几个参数
            //   definition.getConstructorArgumentValues()
            //					.addIndexedArgumentValue(1,null);

            // 定义Bean工程(最终会用上面add的构造函数参数值作为参数调用RepositoryFactory的构造方法)
            definition.setBeanClass(RepositoryFactory.class);
            //这里采用的是byType方式注入，类似的还有byName等
            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
            String simpleName = beanClazz.getSimpleName();
            // 首字母小写注入容器
            simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
            beanDefinitionRegistry.registerBeanDefinition(simpleName, definition);
        }
    }

    /**
     * description: 是否是需要被代理的接口
     * version: 1.0
     * date: 2021/3/7 17:35
     * author: Silwings
     *
     * @param beanClazz 类对象
     * @return boolean 如果不是需要被代理的接口返回true
     */
    private boolean isNotNeedProxy(Class beanClazz) {
        // 如果不是接口,或者其实现的接口小于等于0,或者其实现的第一个接口不是Repository,或者没有添加@NeedProxy注解,则说明不是需要被代理的接口
        return !beanClazz.isInterface() || beanClazz.getInterfaces().length <= 0 || beanClazz.getInterfaces()[0] != Repository.class || null == AnnotatedElementUtils.findMergedAnnotation(beanClazz, NeedProxy.class);
    }

    /**
     * description: 根据包路径获取包及子包下的所有类
     * version: 1.0
     * date: 2021/3/7 17:34
     * author: Silwings
     *
     * @param basePackage 需要扫描的包
     * @return java.util.Set<java.lang.Class < ?>>
     */
    private Set<Class<?>> scannerPackages(String basePackage) {
        Set<Class<?>> set = new LinkedHashSet<>();
        // 此处固定写法即可,含义就是包及子包下的所有类
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                resolveBasePackage(basePackage) + '/' + DEFAULT_RESOURCE_PATTERN;
        try {
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                    String className = metadataReader.getClassMetadata().getClassName();
                    Class<?> clazz;
                    try {
                        clazz = Class.forName(className);
                        set.add(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }

    /**
     * description: 解析包名
     * version: 1.0
     * date: 2021/3/7 17:31
     * author: Silwings
     *
     * @param basePackage 需要解析的路径
     * @return java.lang.String 解析后的路径
     */
    private String resolveBasePackage(String basePackage) {
        // 将类名转换为资源路径
        return ClassUtils.convertClassNameToResourcePath(
                // 解析占位符
                this.applicationContext.getEnvironment().resolveRequiredPlaceholders(basePackage));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        // 该方法空实现即可,用不到
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}

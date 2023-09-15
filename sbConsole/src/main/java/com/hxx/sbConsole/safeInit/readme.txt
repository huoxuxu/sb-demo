https://mp.weixin.qq.com/s/XJ8wGiN9LTYA_i7Z1YB3CQ

初始化的顺序为：
1.构造器方法
2.@PostConstruct 注解方法
3.InitializingBean的afterPropertiesSet()
4.Bean定义的initMethod属性方法

应用初始化顺序
init-InitializingBean
init-LogicInConstructorExampleBean
init-@PostConstruct
init-initMethod
init-ApplicationListener<ContextRefreshedEvent>
init-@EventListener
init-ApplicationRunner
init-CommandLineRunner
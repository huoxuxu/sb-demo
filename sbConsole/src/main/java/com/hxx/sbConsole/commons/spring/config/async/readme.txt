使用：
1.声明线程池
2.实现AsyncConfigurer 类，从而获取自定义Spring异步执行配置项的能力
3.只需在需要异步的方法上添加 @Async 即可

注意：
异步方法使用注解@Async的返回值只能为void或者Future及其子类。
当返回结果为其他类型时，方法还是会异步执行，但是返回值都是null，部分源码如下：AsyncExecutionInterceptor#invoke

@Async注解会在以下几个场景失效：

异步方法是static静态方法
异步类不是一个Spring容器的bean（一般使用注解@Component和@Service，并且能被Spring扫描到）；
SpringBoot应用中没有添加@EnableAsync注解；
在同一个类中，一个方法调用另外一个有@Async注解的方法，注解不会生效。原因是@Async注解的方法，是在代理类中执行的。

SpringBoot中如何优雅的使用多线程
通过上边几个示例，@Async实际还是通过Future或CompletableFuture来异步执行的，Spring又封装了一下，让我们使用的更方便。

@Async注解会在以下几个场景失效，也就是说明明使用了@Async注解，但就没有走多线程。

异步方法使用static关键词修饰；
异步类不是一个Spring容器的bean（一般使用注解@Component和@Service，并且能被Spring扫描到）；
SpringBoot应用中没有添加@EnableAsync注解；
在同一个类中，一个方法调用另外一个有@Async注解的方法，注解不会生效。原因是@Async注解的方法，是在代理类中执行的。
需要注意的是： 异步方法使用注解@Async的返回值只能为void或者Future及其子类，当返回结果为其他类型时，方法还是会异步执行，但是返回值都是null，部分源码如下：
AsyncExecutionInterceptor#invoke

SpringBoot中如何优雅的使用多线程
通过上边几个示例，@Async实际还是通过Future或CompletableFuture来异步执行的，Spring又封装了一下，让我们使用的更方便。
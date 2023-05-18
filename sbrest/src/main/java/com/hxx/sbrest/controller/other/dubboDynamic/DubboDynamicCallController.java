package com.hxx.sbrest.controller.other.dubboDynamic;

import com.hxx.sbcommon.common.json.JsonUtil;
import com.hxx.sbrest.model.T1Model;
import com.hxx.sbrest.service.BasicTestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-07 17:40:20
 **/
@Slf4j
@RestController
@RequestMapping("ddc")
public class DubboDynamicCallController {

    /**
     * http://localhost:1080/ddc/call
     *
     * @param group
     */
    @RequestMapping("/call")
    public void call(String group) {
        String zkUri = "zookeeper://101.91.15.32:2181";
        String customerDubboService = "com.a.b.data.service.dubbo.c";
        String methodName = "getData";
        String[] methodParaTypes = new String[]{"com.a.b.data.model.req.c.d"};

        Map<String, Object> map = new HashMap<>();
        {
            map.put("code", 0);
            map.put("name", "");
        }
        Object[] methodParaVals = new Object[]{map};

        try {
            dynamicCall(zkUri, customerDubboService, methodName, methodParaTypes, methodParaVals);
        } catch (Exception ex) {
            System.out.println(ex + "");
        }
    }

    //    public MyDubboGroupService getInvokeService(String group) {
//        ApplicationConfig application = new ApplicationConfig();
//        application.setName(“students-consumer”);
//
//        RegistryConfig registry = new RegistryConfig();
//        registry.setAddress(“127.0.0.1:2181”);
//        registry.setProtocol(“zookeeper”);
//
//        ReferenceConfig referenceConfig = new ReferenceConfig<>();
//        // 远程服务调用重试次数，不包括第一次调用，不需要重试请设为0
//        //referenceConfig.setRetries(0);
//        // 负载均衡策略，可选值：random,roundrobin,leastactive，分别表示：随机，轮询，最少活跃调用
//        //referenceConfig.setLoadbalance(“leastactive”);
//        referenceConfig.setApplication(application);
//        referenceConfig.setRegistry(registry);
//        referenceConfig.setGroup(group);
//        referenceConfig.setInterface(MyDubboGroupService.class);
//        return referenceConfig.get();
//    }
//定义泛化调用服务类
    private static GenericService genericService;

    public static void dynamicCall(String zkUri, String customerDubboService, String methodName, String[] methodParaTypes, Object[] methodParaVals) throws InterruptedException {
        //创建ApplicationConfig
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("generic-call-consumer");
        //创建注册中心配置
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(zkUri);
        //创建服务引用配置
        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        //设置接口
        referenceConfig.setInterface(customerDubboService);
        applicationConfig.setRegistry(registryConfig);
        referenceConfig.setApplication(applicationConfig);
        //重点：设置为泛化调用
        //注：不再推荐使用参数为布尔值的setGeneric函数
        //应该使用referenceConfig.setGeneric("true")代替
//        referenceConfig.setGeneric(true);
        referenceConfig.setGeneric("true"); // 声明为泛化接口
//        //设置异步，不必须，根据业务而定。
//        referenceConfig.setAsync(true);
        //设置超时时间
        referenceConfig.setTimeout(7000);

        //获取服务，由于是泛化调用，所以获取的一定是GenericService类型
        genericService = referenceConfig.get();

        //使用GenericService类对象的$invoke方法可以代替原方法使用
        //第一个参数是需要调用的方法名
        //第二个参数是需要调用的方法的参数类型数组，为String数组，里面存入参数的全类名。
        //第三个参数是需要调用的方法的参数数组，为Object数组，里面存入需要的参数。
        Object result = genericService.$invoke(methodName, methodParaTypes, methodParaVals);
        //使用CountDownLatch，如果使用同步调用则不需要这么做。
        CountDownLatch latch = new CountDownLatch(1);
        //获取结果
        CompletableFuture<String> future = RpcContext.getContext()
                .getCompletableFuture();
        future.whenComplete((value, t) -> {
            System.err.println("invokeSayHello(whenComplete): " + value);
            latch.countDown();
        });
        //打印结果
        System.err.println("invokeSayHello(return): " + result);
        latch.await();
    }

}

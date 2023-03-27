package com.newland.sf;

import com.newland.sf.config.EventContractConfig;
import com.newland.sf.utils.NacosParserHelper;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@SpringBootApplication
//@ComponentScan({"com.newland.sf.component"})
@RestController
public class SpringCloudEventsApplication {

    private static BlockingQueue queue = new LinkedBlockingQueue<String>(10);

    @Value("${key}")
    private String key;

    @Resource
    private EventContractConfig eventContractConfig;

    @Resource
    private FlowExecutor flowExecutor;

    @GetMapping("test")
    public String execute(@RequestParam("chainId") String chainId) {

        System.out.println(key);
        if (eventContractConfig.getEventContract() != null) {
            eventContractConfig.getEventContract().forEach(v -> {
                System.out.println(v.getExpression() + ";" + v.getContractName() + ";" + v.getVersion());
            });
        }

        LiteflowResponse response = flowExecutor.execute2Resp(chainId, null);

        NacosParserHelper nacosParserHelper = new NacosParserHelper();
        System.out.println(nacosParserHelper.getContent("flow.el.xml", "DEFAULT_GROUP"));

        Consumer<String> parseConsumer = t -> {
            try {
                System.out.println(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        nacosParserHelper.listener("flow.el.xml", "DEFAULT_GROUP", parseConsumer);


        return response.getRequestId();
    }

    @GetMapping("custom")
    public String custom(@RequestParam("chainId") String chainId) {
        LiteflowResponse response = flowExecutor.execute2Resp(chainId, null);
        return response.getRequestId();
    }

    @GetMapping("queue")
    public Boolean queue() throws InterruptedException {

        if (queue.size() >= 10) {
            System.out.println("已超过队列长度");
            return false;
        }

        System.out.println("插入结果：" + queue.offer(System.currentTimeMillis(), 1000L, TimeUnit.MILLISECONDS));
        System.out.println("size:" + queue.size());

        return true;

    }

    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                try {
                    System.out.println(String.valueOf(Thread.currentThread().getId()) + ":" + queue.take());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                try {
                    System.out.println(String.valueOf(Thread.currentThread().getId()) + ":" + queue.take());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        SpringApplication.run(SpringCloudEventsApplication.class, args);
    }
}

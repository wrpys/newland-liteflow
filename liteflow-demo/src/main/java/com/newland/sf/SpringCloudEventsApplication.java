package com.newland.sf;

import cn.hutool.core.collection.ListUtil;
import com.newland.sf.config.EventContractConfig;
import com.newland.sf.model.Cdr;
import com.newland.sf.queue.EventQueue;
import com.newland.sf.utils.NacosParserHelper;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.FlowBus;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.flow.element.Chain;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.Condition;
import com.yomahub.liteflow.flow.element.condition.EndCondition;
import com.yomahub.liteflow.flow.element.condition.IfCondition;
import com.yomahub.liteflow.flow.element.condition.ThenCondition;
import com.yomahub.liteflow.flow.element.condition.WhenCondition;
import com.yomahub.liteflow.model.base.Event;
import com.yomahub.liteflow.slot.ContractContext;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.DefaultContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private EventQueue eventQueue;

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

    @GetMapping("test2")
    public String execute2(@RequestParam("contractId") String contractId, @RequestParam("chainId") String chainId) {

        ContractContext<Cdr> contractContext = new ContractContext<>(Cdr.class);
        Cdr cdr = new Cdr();
        cdr.setContractId(contractId);
        contractContext.setInput(cdr);

        LiteflowResponse response = flowExecutor.execute2Resp(contractId, chainId, null, contractContext);

        Map<String, Map<String, Chain>> chainMap = FlowBus.getChainMap();
        for (String k : chainMap.keySet()) {
            System.out.println("===" + k + "===begin");
            Map<String, Chain> v = chainMap.get(k);
            for (String k2 : v.keySet()) {
                Chain v2 = v.get(k2);
                System.out.println("---" + k2 + "---begin");
                List<Condition> cs = v2.getConditionList();
                for (Condition c : cs) {
                    condition(c);
                }
                System.out.println("---" + k2 + "---end");
            }
            System.out.println("===" + k + "===end");
        }

        return response.getExecuteStepStr();
    }

    private void condition(Condition c) {
        if (c instanceof EndCondition) {
            endCondition((EndCondition) c);
        } else if (c instanceof IfCondition) {
            ifCondition((IfCondition) c);
        } else if (c instanceof ThenCondition) {
            thenCondition((ThenCondition) c);
        } else if (c instanceof WhenCondition) {
            whenCondition((WhenCondition) c);
        } else {
            throw new RuntimeException("未知类型");
        }
    }

    private void endCondition(EndCondition endCondition) {
        System.out.println(endCondition.getConditionType().getType() + "," + endCondition.getId() + "," + endCondition.getRunId());
    }

    private void ifCondition(IfCondition ifCondition) {

        System.out.println(ifCondition.getConditionType().getType() + "," + ifCondition.getId() + "," + ifCondition.getRunId());

        Executable executable = ifCondition.getTrueCaseExecutableItem();
        if (executable instanceof Condition) {
            condition((Condition) executable);
        } else if (executable instanceof Node) {
            Node n = (Node) executable;
            System.out.println(n.getType().getCode() + "," + n.getId() + "," + n.getRunId());
        }

        executable = ifCondition.getFalseCaseExecutableItem();
        if (executable instanceof Condition) {
            condition((Condition) executable);
        } else if (executable instanceof Node) {
            Node n = (Node) executable;
            System.out.println(n.getType().getCode() + "," + n.getId() + "," + n.getRunId());
        }

    }

    private void thenCondition(ThenCondition thenCondition) {
        System.out.println(thenCondition.getConditionType().getType() + "," + thenCondition.getId() + "," + thenCondition.getRunId());

        List<Executable> executables = thenCondition.getExecutableList();
        if (executables != null) {
            for (Executable executable : executables) {
                if (executable instanceof Condition) {
                    condition((Condition) executable);
                } else if (executable instanceof Node) {
                    Node n = (Node) executable;
                    System.out.println(n.getType().getCode() + "," + n.getId() + "," + n.getRunId());
                }
            }
        }
    }

    private void whenCondition(WhenCondition whenCondition) {
        System.out.println(whenCondition.getConditionType().getType() + "," + whenCondition.getId() + "," + whenCondition.getRunId());

        List<Executable> executables = whenCondition.getExecutableList();
        if (executables != null) {
            for (Executable executable : executables) {
                if (executable instanceof Condition) {
                    condition((Condition) executable);
                } else if (executable instanceof Node) {
                    Node n = (Node) executable;
                    System.out.println(n.getType().getCode() + "," + n.getId() + "," + n.getRunId());
                }
            }
        }
    }

    @GetMapping("test3")
    public Boolean execute3(@RequestParam("contractId") String contractId, @RequestParam("nodeId") String nodeId) throws Exception {

        ContractContext<Cdr> contractContext = new ContractContext<>(Cdr.class);
        Cdr cdr = new Cdr();
        cdr.setContractId(contractId);
        contractContext.setInput(cdr);

        Integer slotIndex = DataBus.offerSlotByBean(ListUtil.toList(contractContext));

        flowExecutor.invoke(contractId, nodeId, slotIndex);

        LiteflowResponse response = LiteflowResponse.newMainResponse(DataBus.getSlot(slotIndex));
        return response.isSuccess();
    }

    @GetMapping("custom")
    public String custom(@RequestParam("chainId") String chainId) {

        Map<String, Object> input = new HashMap<>();
        input.put("id", "1");
        input.put("eventType", "custom");

        Map<String, Object> result = new HashMap<>();
        result.put("code", "1");
        result.put("data", "test");

        DefaultContext defaultContext = new DefaultContext();
        defaultContext.setData("input", input);
        defaultContext.setData("result", result);
        LiteflowResponse response = flowExecutor.execute2Resp(chainId, null, defaultContext);
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

    @PostConstruct
    public void init() {
        new Thread(() -> {
            while (true) {
                try {
                    ProceedingJoinPoint joinPoint = eventQueue.getQueue().take();
                    Object[] args = joinPoint.getArgs();
                    Event input = (Event) args[0];
                    Object output = joinPoint.proceed();

                    ContractContext context = new ContractContext<>(Cdr.class);
                    context.setInput(input);
                    context.setOutput((Event) output);
                    LiteflowResponse response = flowExecutor.execute2Resp(input.getFunName(), null, context);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

}

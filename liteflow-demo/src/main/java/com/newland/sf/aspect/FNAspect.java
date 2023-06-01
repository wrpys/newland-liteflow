package com.newland.sf.aspect;

import cn.hutool.core.util.ObjectUtil;
import com.newland.sf.annotation.SLFFunction;
import com.newland.sf.utils.Json;
import com.newland.sf.work.WorkThread;
import com.yomahub.liteflow.model.base.Event;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FNAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(FNAspect.class);

    @Autowired
    private WorkThread workThread;

    @Around("@annotation(slfFunction)")
    public Object around(ProceedingJoinPoint joinPoint, SLFFunction slfFunction) throws Throwable {

        LOGGER.info("SLFFunction aroundApply begin");

        String name = slfFunction.name();
        String version = slfFunction.version();

        LOGGER.info("SLFFunction name:{},version:{}", name, version);

        Object[] args = joinPoint.getArgs();
        if (args.length != 1) {
            throw new Exception("参数个数有误！");
        }
        Event input = (Event) args[0];
        LOGGER.info("SLFFunction name:{},version:{},input:{}", name, version, Json.toJson(input));

        Event result = ObjectUtil.cloneByStream(input);

        // 任务池空闲
        if (workThread.isFree()) {

            // 事件步骤注册 TODO

            // 注册成功
            boolean flag = workThread.execute(input, joinPoint, input.getClass());
            if (flag) {
                result.setCode("1");
                result.setMsg("操作成功！");
            } else {
                result.setCode("0");
                result.setMsg("操作失败，请重试！");
            }
        }
        // 任务池已满
        else {
            result.setCode("2");
            result.setMsg("任务池已满！");
        }
        return result;
    }

}

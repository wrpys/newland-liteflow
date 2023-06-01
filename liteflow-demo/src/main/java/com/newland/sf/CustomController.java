package com.newland.sf;

import cn.hutool.core.util.ObjectUtil;
import com.newland.sf.annotation.SLFFunction;
import com.newland.sf.config.EventContractConfig;
import com.newland.sf.model.Cdr;
import com.newland.sf.utils.Json;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.model.base.Event;
import com.yomahub.liteflow.slot.ContractContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 话单处理
 *
 * @author WRP
 * @since 2023/3/28
 */
@RestController
@RequestMapping("custom")
public class CustomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomController.class);

    @Autowired
    private EventContractConfig eventContractConfig;

    @Resource
    private FlowExecutor flowExecutor;

    /**
     * 离线话单流程
     *
     * @param cdr
     * @return
     */
    @PostMapping("lixian")
    public Event lixian(@RequestBody Cdr cdr) {
        LOGGER.info("lixian CDR:{}", Json.toJson(cdr));

        // 执行契约
        ContractContext<Cdr> contractContext = new ContractContext<>(Cdr.class);
        cdr.setContractId("flow.el-1.0.0.xml");
        cdr.setChainId("main");
        contractContext.setData(cdr);
        LiteflowResponse response = flowExecutor.execute2Resp("flow.el-1.0.0.xml", "main", null, contractContext);

        Event result = ObjectUtil.cloneByStream(cdr);

        if (response.isSuccess()) {
            result.setCode("1");
            result.setMsg("操作成功！");
        } else {
            result.setCode("0");
            result.setMsg(response.getMessage());
        }

        result.setContractId("flow.el.xml");

        return result;
    }

    /**
     * 查重函数服务
     *
     * @param cdr
     * @return
     */
    @SLFFunction(name = "chachong", version = "1.0.0")
    @PostMapping("chachong/1.0.0")
    public Cdr chachong(@RequestBody Cdr cdr) {
        LOGGER.info("chachong CDR:{}", Json.toJson(cdr));
        cdr.setChachongData("chachongData");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return cdr;
    }

    /**
     * 要素求取函数服务
     *
     * @param cdr
     * @return
     */
    @SLFFunction(name = "yaosuqiuqu", version = "1.0.0")
    @PostMapping("yaosuqiuqu/1.0.0")
    public Cdr yaosuqiuqu(@RequestBody Cdr cdr) {
        LOGGER.info("yaosuqiuqu CDR:{}", Json.toJson(cdr));
        cdr.setYaosuqiuquData("yaosuqiuquData");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return cdr;
    }

    /**
     * 批价函数服务
     *
     * @param cdr
     * @return
     */
    @SLFFunction(name = "pijia", version = "1.0.0")
    @PostMapping("pijia/1.0.0")
    public Cdr pijia(@RequestBody Cdr cdr) {
        LOGGER.info("pijia CDR:{}", Json.toJson(cdr));
        cdr.setPijiaData("pijiaData");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return cdr;
    }

    /**
     * 扣款函数服务
     *
     * @param cdr
     * @return
     */
    @SLFFunction(name = "koukuan", version = "1.0.0")
    @PostMapping("koukuan/1.0.0")
    public Cdr koukuan(@RequestBody Cdr cdr) {
        LOGGER.info("koukuan CDR:{}", Json.toJson(cdr));
        cdr.setKoukuanData("koukuanData");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return cdr;
    }

    /**
     * 结束回调
     *
     * @param cdr
     * @return
     */
    @PostMapping("end")
    public Cdr end(@RequestBody Cdr cdr) {
        LOGGER.info("end CDR:{}", Json.toJson(cdr));
        // 清除会话 TODO
        cdr.setCode("1");
        cdr.setMsg("操作成功！");
        return cdr;
    }

}

package com.newland.sf;

import com.newland.sf.annotation.SLFFunction;
import com.newland.sf.config.EventContract;
import com.newland.sf.config.EventContractConfig;
import com.newland.sf.model.Cdr;
import com.newland.sf.utils.Json;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.slot.ContractContext;
import com.yomahub.liteflow.util.SpringExpressionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
    public Cdr lixian(@RequestBody Cdr cdr) {
        LOGGER.info("CDR:{}", Json.toJson(cdr));

        List<EventContract> eventContractList = eventContractConfig.getEventContract();
        if (CollectionUtils.isEmpty(eventContractList)) {
            cdr.setCode("0");
            cdr.setMsg("无事件类型与契约关系配置！");
            return cdr;
        }

        final StringBuilder contractId = new StringBuilder();
        for (EventContract eventContract : eventContractList) {
            if (SpringExpressionUtil.parseExpression(eventContract.getExpression(), cdr)) {
                contractId.append(eventContract.getContractName()).append("-").append(eventContract.getVersion());
                break;
            }
        }
        if (contractId.length() == 0) {
            cdr.setCode("0");
            cdr.setMsg("匹配不到契约，请检查！");
            return cdr;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO 契约下载
                // 执行契约
                ContractContext<Cdr> contractContext = new ContractContext<>(Cdr.class);
                contractContext.setData(cdr);

//                DefaultContext defaultContext = new DefaultContext();
//                defaultContext.setData("input", cdr);

                LiteflowResponse response = flowExecutor.execute2Resp(contractId.toString(), null, contractContext);
            }
        }).start();

        cdr.setContractId(contractId.toString());
        cdr.setCode("1");
        cdr.setMsg("操作成功！");

        return cdr;
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
        LOGGER.info("CDR:{}", Json.toJson(cdr));
        cdr.setChachongData("data1");

        ContractContext<Cdr> contractContext = new ContractContext<>(Cdr.class);
        contractContext.setData(cdr);

        LiteflowResponse response = flowExecutor.execute2Resp(cdr.getContractId(), cdr.getChainId(), null, contractContext);

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
        LOGGER.info("CDR:{}", Json.toJson(cdr));
        cdr.setYaosuqiuquData("data2");
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
        LOGGER.info("CDR:{}", Json.toJson(cdr));
        cdr.setPijiaData("data3");
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
        LOGGER.info("CDR:{}", Json.toJson(cdr));
        cdr.setKoukuanData("data4");
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
        LOGGER.info("CDR:{}", Json.toJson(cdr));
        // 清楚会话cache
        cdr.setCode("1");
        cdr.setMsg("操作成功！");
        return cdr;
    }

}

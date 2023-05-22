package com.yomahub.liteflow.flow.element.condition;

import cn.hutool.http.HttpUtil;
import com.yomahub.liteflow.enums.CmpStepTypeEnum;
import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.flow.entity.CmpStep;
import com.yomahub.liteflow.model.base.Event;
import com.yomahub.liteflow.slot.ContractContext;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;
import com.yomahub.liteflow.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 结束处理Condition
 *
 * @author WRP
 * @since 2023/3/27
 */
public class EndCondition extends Condition {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndCondition.class);

    @Override
    public void execute(Integer slotIndex) throws Exception {
        //在元数据里加入step信息
        Slot slot = DataBus.getSlot(slotIndex);
        CmpStep cmpStep = new CmpStep(this.getId(), null, this.getRunId(), CmpStepTypeEnum.SINGLE);
        slot.addStep(cmpStep);

        ContractContext context = slot.getContextBean(ContractContext.class);

        // TODO 调用结束回调接口
        StringBuilder url = new StringBuilder("http://localhost:8080/custom/end");
        LOGGER.info("url:{}", url);
        String body = HttpUtil.post(url.toString(), JsonUtil.toJsonString(context.getOutput()));
        Event event = JsonUtil.parseObject(body, Event.class);
        if (Objects.equals(event.getCode(), "1")) {

        } else {

        }

    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.TYPE_END;
    }

}

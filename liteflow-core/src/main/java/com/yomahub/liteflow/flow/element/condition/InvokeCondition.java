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

import java.util.Objects;

/**
 * 函数调用Condition
 *
 * @author WRP
 * @since 2023/3/27
 */
public class InvokeCondition extends Condition {

    private String funName;
    private String funVersion;

    @Override
    public void execute(Integer slotIndex) throws Exception {
        //在元数据里加入step信息
        Slot slot = DataBus.getSlot(slotIndex);
        CmpStep cmpStep = new CmpStep(this.getId(), this.getId(), this.getRunId(), CmpStepTypeEnum.SINGLE);
        slot.addStep(cmpStep);

        ContractContext context = slot.getContextBean(ContractContext.class);

        // TODO 调用函数服务
        StringBuilder url = new StringBuilder("http://localhost:8080/custom/");
        url.append(funName).append("/").append(funVersion);
        String body = HttpUtil.post(url.toString(), JsonUtil.toJsonString(context.getData()));

        Event event = JsonUtil.parseObject(body, Event.class);
        if (Objects.equals(event.getCode(), "1")) {

        } else {

        }

    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.TYPE_INVOKE;
    }

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }

    public String getFunVersion() {
        return funVersion;
    }

    public void setFunVersion(String funVersion) {
        this.funVersion = funVersion;
    }
}

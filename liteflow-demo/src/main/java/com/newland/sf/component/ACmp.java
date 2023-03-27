package com.newland.sf.component;

import com.newland.sf.model.FuncParam;
import com.newland.sf.utils.Json;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WRP
 * @since 2023/3/14
 */
@LiteflowComponent(id = "a", name = "A")
public class ACmp extends NodeComponent {

    private final Logger LOGGER = LoggerFactory.getLogger(ACmp.class);

    @Override
    public void process() {
        //do your business
        LOGGER.info("执行ACmp");

//        FuncParam param = this.getCmpData(FuncParam.class);

//        LOGGER.info("执行ACmp param:{}", Json.toJson(param));

    }

}

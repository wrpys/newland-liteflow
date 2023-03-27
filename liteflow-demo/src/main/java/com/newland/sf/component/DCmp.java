package com.newland.sf.component;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WRP
 * @since 2023/3/14
 */
@LiteflowComponent(id = "d", name = "D")
public class DCmp extends NodeComponent {

    private final Logger LOGGER = LoggerFactory.getLogger(DCmp.class);

    @Override
    public void process() {
        //do your business
        LOGGER.info("执行DCmp");
    }

}

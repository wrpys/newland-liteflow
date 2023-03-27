package com.newland.sf.component;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WRP
 * @since 2023/3/14
 */
@LiteflowComponent(id = "$end", name = "END")
public class EndCmp extends NodeComponent {

    private final Logger LOGGER = LoggerFactory.getLogger(EndCmp.class);

    @Override
    public void process() {
        //do your business
        LOGGER.info("执行EndCmp");

    }

}

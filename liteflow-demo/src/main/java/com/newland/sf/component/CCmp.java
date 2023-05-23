package com.newland.sf.component;

import com.newland.sf.model.Cdr;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.slot.ContractContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WRP
 * @since 2023/3/14
 */
public class CCmp extends NodeComponent {

    private final Logger LOGGER = LoggerFactory.getLogger(CCmp.class);

    @Override
    public void process() {
        //do your business
        LOGGER.info("执行CCmp");

        ContractContext<Cdr> context = this.getContextBean(ContractContext.class);

        Cdr cdr = context.getData();
        String test = cdr.getData() == null ? "" : cdr.getData() + ",";
        cdr.setData(test + "c");

        context.setData(cdr);
    }

}

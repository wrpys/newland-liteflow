package com.newland.sf.component;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeIfComponent;

/**
 * @author WRP
 * @since 2023/3/14
 */
@LiteflowComponent(id = "x", name = "X")
public class XCmp extends NodeIfComponent {
    @Override
    public boolean processIf() throws Exception {
        //do your biz
        return false;
    }
}

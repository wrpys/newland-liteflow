package com.yomahub.liteflow.parser.factory;

import com.yomahub.liteflow.parser.base.BaseXmlFlowParser;

/**
 * Flow Parser 工厂接口
 * <p>
 *
 * @author junjun
 */
public interface FlowParserFactory {

//    BaseJsonFlowParser createJsonELParser(String path);

    BaseXmlFlowParser createXmlELParser(String path);

//    BaseYmlFlowParser createYmlELParser(String path);

}

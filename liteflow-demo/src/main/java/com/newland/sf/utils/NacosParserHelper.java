package com.newland.sf.utils;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.client.config.NacosConfigService;
import com.yomahub.liteflow.spi.holder.ContextAwareHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * Nacos client for http api
 * <a href="https://nacos.io/zh-cn/docs/open-api.html">OpenAPI</a>
 *
 * @author mll
 * @since 2.9.0
 */
public class NacosParserHelper {
    private static final Logger LOG = LoggerFactory.getLogger(NacosParserHelper.class);

    private NacosConfigService configService;

    public NacosParserHelper() {
        try {
            try {
                this.configService = ContextAwareHolder.loadContextAware().getBean(NacosConfigService.class);
            } catch (Exception ignored) {
            }
            if (this.configService == null) {
                Properties properties = new Properties();
                properties.put(PropertyKeyConst.SERVER_ADDR, "10.1.18.214:8848");
                properties.put(PropertyKeyConst.NAMESPACE, "a187b275-65be-4e47-ab35-656515dca154");
                this.configService = new NacosConfigService(properties);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getContent(String dataId, String group) {
        try {
            return configService.getConfig(dataId, group, 3000L);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 监听 nacos 数据变化
     */
    public void listener(String dataId, String group, Consumer<String> parseConsumer) {
        try {
            this.configService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    LOG.info("stating load flow config.... {} ", configInfo);
                    parseConsumer.accept(configInfo);
                }

                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

}

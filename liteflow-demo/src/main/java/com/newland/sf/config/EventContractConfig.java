package com.newland.sf.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 事件类型与契约关系配置
 *
 * @author WRP
 * @since 2023/3/23
 */
@Configuration
@ConfigurationProperties(prefix = EventContractConfig.PREFIX)
public class EventContractConfig {

    public static final String PREFIX = "slf";

    private List<EventContract> eventContract;

    public List<EventContract> getEventContract() {
        return eventContract;
    }

    public void setEventContract(List<EventContract> eventContract) {
        this.eventContract = eventContract;
    }
}

package com.yomahub.liteflow.flow;

import com.yomahub.liteflow.util.CopyOnWriteHashMap;

import java.util.Map;

/**
 * @author WRP
 * @since 2023/5/24
 */
public class FlowContent {

    /**
     * Map<requestId, Map<runId, result>>
     */
    private static final Map<String, Map<String, Object>> flowStepMap = new CopyOnWriteHashMap<>();

    public static Map<String, Map<String, Object>> getFlowStep() {
        return flowStepMap;
    }

    public static void addStepResult(String requestId, String runId, Object result) {
        if (!flowStepMap.containsKey(requestId)) {
            flowStepMap.put(requestId, new CopyOnWriteHashMap<>());
        }
        flowStepMap.get(requestId).put(runId, result);
    }

    public static Object getStepResult(String requestId, String runId) {
        return flowStepMap.get(requestId).get(runId);
    }

    public static void setStepResultMap(String requestId, Map<String, Object> stepResultMap) {
        if (stepResultMap == null) {
            stepResultMap = new CopyOnWriteHashMap<>();
        }
        flowStepMap.put(requestId, stepResultMap);
    }

    public static Map<String, Object> getStepResultMap(String requestId) {
        Map<String, Object> stepResultMap = flowStepMap.get(requestId);
        return stepResultMap == null ? new CopyOnWriteHashMap<>() : stepResultMap;
    }

    public static void removeStepResultMap(String requestId) {
        flowStepMap.remove(requestId);
    }

}

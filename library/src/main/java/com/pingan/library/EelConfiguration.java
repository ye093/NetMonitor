package com.pingan.library;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yejy on 17/5/19.
 */
public class EelConfiguration {

    /**
     * Eel 插件的Token
     */
    private String applicationToken;

    /**
     * 自定义请求参数
     */
    private Map<String,String> httpRequestParams;

    public EelConfiguration() {
        httpRequestParams = new HashMap<>();
    }

    public Map<String, String> getHttpRequestParams() {
        return httpRequestParams;
    }

    public void setHttpRequestParams(Map<String, String> httpRequestParams) {
        this.httpRequestParams = httpRequestParams;
    }


    public String getApplicationToken() {
        return applicationToken;
    }

    public void setApplicationToken(String applicationToken) {
        this.applicationToken = applicationToken;
    }
}

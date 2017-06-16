package com.pingan.library.common;

/**
 * Created by yejy on 2017/6/6.
 */

public class NetMonitorEntity {
    private String url;
    private String method;
    private String params;
    private String requestHeaders;
    private String timeout;
    private String sendTimeTag;
    private String responseTimeTag;
    private String finishTimeTag;
    private String responseHeaders;
    private String hostIpAddress;
    private String statusCode;
    private String errorMsg;
    private String businessContent;
    private String code;

    //标示
    public boolean preRead;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(String requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getSendTimeTag() {
        return sendTimeTag;
    }

    public void setSendTimeTag(String sendTimeTag) {
        this.sendTimeTag = sendTimeTag;
    }

    public String getResponseTimeTag() {
        return responseTimeTag;
    }

    public void setResponseTimeTag(String responseTimeTag) {
        this.responseTimeTag = responseTimeTag;
    }

    public String getFinishTimeTag() {
        return finishTimeTag;
    }

    public void setFinishTimeTag(String finishTimeTag) {
        this.finishTimeTag = finishTimeTag;
    }

    public String getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(String responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public String getHostIpAddress() {
        return hostIpAddress;
    }

    public void setHostIpAddress(String hostIpAddress) {
        this.hostIpAddress = hostIpAddress;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getBusinessContent() {
        return businessContent;
    }

    public void setBusinessContent(String businessContent) {
        this.businessContent = businessContent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return "NetMonitorEntity{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", params='" + params + '\'' +
                ", requestHeaders='" + requestHeaders + '\'' +
                ", timeout='" + timeout + '\'' +
                ", sendTimeTag='" + sendTimeTag + '\'' +
                ", responseTimeTag='" + responseTimeTag + '\'' +
                ", finishTimeTag='" + finishTimeTag + '\'' +
                ", responseHeaders='" + responseHeaders + '\'' +
                ", hostIpAddress='" + hostIpAddress + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", businessContent='" + businessContent + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}

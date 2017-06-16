package com.pingan.library.aspectjx;


import com.pingan.library.common.TransactionData;
import com.pingan.library.log.IAgentLog;
import com.pingan.library.log.LogManager;
import com.pingan.library.util.Util;

public final class TransactionState {

    private static IAgentLog log;

    private String url;
    private String httpMethod;
    private int statusCode;
    private int errorCode;
    private long bytesSent;
    private long bytesReceived;
    private long startTime;
    private long endTime;
    private String appData;
    private String carrier;
    private String wanType;
    private State state;
    private String contentType;
    private TransactionData transactionData;

    public TransactionState() {
        this.startTime = System.currentTimeMillis();
        this.carrier = "unknown";
        this.wanType = "unknown";
        this.state = State.READY;
    }

    public void setCarrier(final String carrier) {
        if (!this.isSent()) {
            this.carrier = carrier;
        } else {
            TransactionState.log.warning("setCarrier(...) called on TransactionState in " + this.state.toString() + " state");
        }
    }

    public void setWanType(final String wanType) {
        if (!this.isSent()) {
            this.wanType = wanType;
        } else {
            TransactionState.log.warning("setWanType(...) called on TransactionState in " + this.state.toString() + " state");
        }
    }

    public void setAppData(final String appData) {
        if (!this.isComplete()) {
            this.appData = appData;
        } else {
            TransactionState.log.warning("setAppData(...) called on TransactionState in " + this.state.toString() + " state");
        }
    }

    public void setUrl(final String urlString) {
        final String url = Util.sanitizeUrl(urlString);
        if (url == null) {
            return;
        }
        if (!this.isSent()) {
            this.url = url;
        } else {
            TransactionState.log.warning("setUrl(...) called on TransactionState in " + this.state.toString() + " state");
        }
    }

    public void setHttpMethod(final String httpMethod) {
        if (!this.isSent()) {
            this.httpMethod = httpMethod;
        } else {
            TransactionState.log.warning("setHttpMethod(...) called on TransactionState in " + this.state.toString() + " state");
        }
    }

    public String getUrl() {
        return this.url;
    }

    public String getHttpMethod() {
        return this.httpMethod;
    }

    public boolean isSent() {
        return this.state.ordinal() >= State.SENT.ordinal();
    }

    public boolean isComplete() {
        return this.state.ordinal() >= State.COMPLETE.ordinal();
    }

    public void setStatusCode(final int statusCode) {
        if (!this.isComplete()) {
            this.statusCode = statusCode;
        } else {
            TransactionState.log.warning("setStatusCode(...) called on TransactionState in " + this.state.toString() + " state");
        }
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setErrorCode(final int errorCode) {
        if (!this.isComplete()) {
            this.errorCode = errorCode;
        } else {
            if (this.transactionData != null) {
                this.transactionData.setErrorCode(errorCode);
            }
            TransactionState.log.warning("setErrorCode(...) called on TransactionState in " + this.state.toString() + " state");
        }
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setBytesSent(final long bytesSent) {
        if (!this.isComplete()) {
            this.bytesSent = bytesSent;
            this.state = State.SENT;
        } else {
            TransactionState.log.warning("setBytesSent(...) called on TransactionState in " + this.state.toString() + " state");
        }
    }

    public void setBytesReceived(final long bytesReceived) {
        if (!this.isComplete()) {
            this.bytesReceived = bytesReceived;
        } else {
            TransactionState.log.warning("setBytesReceived(...) called on TransactionState in " + this.state.toString() + " state");
        }
    }

    public long getBytesReceived() {
        return this.bytesReceived;
    }

    public TransactionData end() {
        if (!this.isComplete()) {
            this.state = State.COMPLETE;
            this.endTime = System.currentTimeMillis();
        }
        return this.toTransactionData();
    }

    private TransactionData toTransactionData() {
        if (!this.isComplete()) {
            TransactionState.log.warning("toTransactionData() called on incomplete TransactionState");
        }
        if (this.url == null) {
            TransactionState.log.error("Attempted to convert a TransactionState instance with no URL into a TransactionData");
            return null;
        }
        if (this.transactionData == null) {
            this.transactionData = new TransactionData(this.url, this.httpMethod, this.carrier, (this.endTime - this.startTime) / 1000.0f, this.statusCode, this.errorCode, this.bytesSent, this.bytesReceived, this.appData, this.wanType);
        }
        return this.transactionData;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "TransactionState{url='" + this.url + '\'' + ", httpMethod='" + this.httpMethod + '\'' + ", statusCode=" + this.statusCode + ", errorCode=" + this.errorCode + ", bytesSent=" + this.bytesSent + ", bytesReceived=" + this.bytesReceived + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ", appData='" + this.appData + '\'' + ", carrier='" + this.carrier + '\'' + ", wanType='" + this.wanType + '\'' + ", state=" + this.state + ", contentType='" + this.contentType + '\'' + ", transactionData=" + this.transactionData + '}';
    }

    static {
        log = LogManager.getAgentLog();
    }

    private enum State {
        READY,
        SENT,
        COMPLETE;
    }
}
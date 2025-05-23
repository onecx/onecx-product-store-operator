package org.tkit.onecx.product.store.operator;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class MsAndMfeStatus extends CustomResourceStatus {

    @JsonProperty("productName")
    private String requestProductName;

    @JsonProperty("appId")
    private String requestAppId;

    @JsonProperty("appVersion")
    private String requestAppVersion;

    @JsonProperty("appName")
    private String requestAppName;

    public String getRequestProductName() {
        return requestProductName;
    }

    public void setRequestProductName(String requestProductName) {
        this.requestProductName = requestProductName;
    }

    public String getRequestAppId() {
        return requestAppId;
    }

    public void setRequestAppId(String requestAppId) {
        this.requestAppId = requestAppId;
    }

    public String getRequestAppVersion() {
        return requestAppVersion;
    }

    public void setRequestAppVersion(String requestAppVersion) {
        this.requestAppVersion = requestAppVersion;
    }

    public String getRequestAppName() {
        return requestAppName;
    }

    public void setRequestAppName(String requestAppName) {
        this.requestAppName = requestAppName;
    }
}

package org.tkit.onecx.product.store.operator.microservice;

import org.tkit.onecx.product.store.operator.CustomResourceStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MicroserviceStatus extends CustomResourceStatus {

    @JsonProperty("productName")
    private String requestProductName;

    @JsonProperty("appId")
    private String requestAppId;

    @JsonProperty("appVersion")
    private String requestAppVersion;

    @JsonProperty("appName")
    private String requestAppName;

    @JsonProperty("responseCode")
    private int responseCode;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

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

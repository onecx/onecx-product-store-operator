package org.tkit.onecx.product.store.operator.microservice;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.javaoperatorsdk.operator.api.ObservedGenerationAwareStatus;

public class MicroserviceStatus extends ObservedGenerationAwareStatus {

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

    @JsonProperty("status")
    private Status status;

    @JsonProperty("message")
    private String message;

    public enum Status {

        ERROR,

        CREATED,

        UPDATED,

        UNDEFINED;
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

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

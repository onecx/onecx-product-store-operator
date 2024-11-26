package org.tkit.onecx.product.store.operator;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.javaoperatorsdk.operator.api.ObservedGenerationAwareStatus;

public abstract class CustomResourceStatus extends ObservedGenerationAwareStatus {

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

    @JsonAlias({ "responseCode", "response-code" })
    private int responseCode;

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

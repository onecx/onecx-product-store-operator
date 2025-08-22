package org.tkit.onecx.product.store.operator;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractResourceStatus {

    @JsonProperty("observedGeneration")
    private Long observedGeneration;

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

    public Long getObservedGeneration() {
        return observedGeneration;
    }
}

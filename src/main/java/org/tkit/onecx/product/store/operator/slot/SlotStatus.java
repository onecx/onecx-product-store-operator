package org.tkit.onecx.product.store.operator.slot;

import org.tkit.onecx.product.store.operator.CustomResourceStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SlotStatus extends CustomResourceStatus {

    @JsonProperty("productName")
    private String requestProductName;

    @JsonProperty("appId")
    private String requestAppId;

    @JsonProperty("name")
    private String requestName;

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

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

}

package org.tkit.onecx.product.store.operator;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class CustomResourceStatus extends AbstractResourceStatus {

    @JsonProperty("responseCode")
    private int responseCode;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

}

package org.tkit.onecx.product.store.operator.product;

import org.tkit.onecx.product.store.operator.AbstractResourceStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductStatus extends AbstractResourceStatus {

    @JsonProperty("response-code")
    private int responseCode;

    @JsonProperty("product-name")
    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}

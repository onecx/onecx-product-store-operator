package org.tkit.onecx.product.store.operator.product;

import org.tkit.onecx.product.store.operator.CustomResourceStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductStatus extends CustomResourceStatus {

    @JsonProperty("product-name")
    private String productName;

    @JsonProperty("response-code")
    private int responseCode;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}

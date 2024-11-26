package org.tkit.onecx.product.store.operator.product;

import org.tkit.onecx.product.store.operator.CustomResourceStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductStatus extends CustomResourceStatus {

    @JsonProperty("product-name")
    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}

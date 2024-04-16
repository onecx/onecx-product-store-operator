package org.tkit.onecx.product.store.operator.client.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.tkit.onecx.product.store.operator.ProductSpec;

import gen.org.tkit.onecx.product.store.product.v1.model.UpdateProductRequest;

@Mapper
public interface ProductStoreMapper {

    @Mapping(target = "undeployed", constant = "false")
    UpdateProductRequest map(ProductSpec spec);
}

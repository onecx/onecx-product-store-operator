package org.tkit.onecx.product.store.operator.client.mappers;

import org.mapstruct.Mapper;
import org.tkit.onecx.product.store.operator.ProductSpec;

import gen.org.tkit.onecx.product.store.product.v1.model.UpdateProductRequest;

@Mapper
public interface ProductStoreMapper {

    UpdateProductRequest map(ProductSpec spec);
}

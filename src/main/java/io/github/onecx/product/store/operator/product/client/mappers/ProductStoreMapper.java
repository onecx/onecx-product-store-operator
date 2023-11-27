package io.github.onecx.product.store.operator.product.client.mappers;

import org.mapstruct.Mapper;

import gen.io.github.onecx.product.store.product.v1.model.UpdateProductRequest;
import io.github.onecx.product.store.operator.product.ProductSpec;

@Mapper
public interface ProductStoreMapper {

    UpdateProductRequest map(ProductSpec spec);
}

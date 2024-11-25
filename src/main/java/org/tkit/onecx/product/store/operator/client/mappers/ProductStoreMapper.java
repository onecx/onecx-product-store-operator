package org.tkit.onecx.product.store.operator.client.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.tkit.onecx.product.store.operator.Product.ProductSpec;
import org.tkit.onecx.product.store.operator.Slot.SlotSpec;

import gen.org.tkit.onecx.product.store.product.v1.model.UpdateProductRequest;
import gen.org.tkit.onecx.product.store.slot.v1.model.UpdateSlotRequest;

@Mapper
public interface ProductStoreMapper {

    @Mapping(target = "undeployed", constant = "false")
    UpdateProductRequest map(ProductSpec spec);

    @Mapping(target = "undeployed", constant = "false")
    UpdateSlotRequest map(SlotSpec spec);
}

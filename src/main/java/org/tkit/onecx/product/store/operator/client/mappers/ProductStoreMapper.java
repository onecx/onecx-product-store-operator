package org.tkit.onecx.product.store.operator.client.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.tkit.onecx.product.store.operator.microfrontend.MicrofrontendSpec;
import org.tkit.onecx.product.store.operator.microservice.MicroserviceSpec;
import org.tkit.onecx.product.store.operator.product.ProductSpec;
import org.tkit.onecx.product.store.operator.slot.SlotSpec;

import gen.org.tkit.onecx.product.store.mfe.v1.model.UpdateMfeRequest;
import gen.org.tkit.onecx.product.store.ms.v1.model.UpdateMsRequest;
import gen.org.tkit.onecx.product.store.product.v1.model.UpdateProductRequest;
import gen.org.tkit.onecx.product.store.slot.v1.model.UpdateSlotRequest;

@Mapper
public interface ProductStoreMapper {

    @Mapping(target = "undeployed", constant = "false")
    UpdateProductRequest map(ProductSpec spec);

    @Mapping(target = "undeployed", constant = "false")
    UpdateSlotRequest map(SlotSpec spec);

    @Mapping(target = "undeployed", constant = "false")
    UpdateMsRequest map(MicroserviceSpec spec);

    @Mapping(target = "undeployed", constant = "false")
    UpdateMfeRequest map(MicrofrontendSpec spec);
}

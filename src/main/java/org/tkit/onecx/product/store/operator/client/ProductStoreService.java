package org.tkit.onecx.product.store.operator.client;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tkit.onecx.product.store.operator.client.mappers.ProductStoreMapper;
import org.tkit.onecx.product.store.operator.product.Product;
import org.tkit.onecx.product.store.operator.product.ProductSpec;
import org.tkit.onecx.product.store.operator.slot.Slot;
import org.tkit.onecx.product.store.operator.slot.SlotSpec;

import gen.org.tkit.onecx.product.store.product.v1.api.OperatorProductApi;
import gen.org.tkit.onecx.product.store.product.v1.model.UpdateProductRequest;
import gen.org.tkit.onecx.product.store.slot.v1.api.OperatorSlotApi;
import gen.org.tkit.onecx.product.store.slot.v1.model.UpdateSlotRequest;

@ApplicationScoped
public class ProductStoreService {

    private static final Logger log = LoggerFactory.getLogger(ProductStoreService.class);

    @Inject
    ProductStoreMapper mapper;

    @Inject
    @RestClient
    OperatorProductApi client;

    @Inject
    @RestClient
    OperatorSlotApi slotClient;

    public int updateProduct(Product product) {
        ProductSpec spec = product.getSpec();
        UpdateProductRequest dto = mapper.map(spec);
        try (var response = client.createOrUpdateProduct(spec.getName(), dto)) {
            log.info("Update product response {}", response.getStatus());
            return response.getStatus();
        }
    }

    public int updateSlot(Slot slot) {
        SlotSpec spec = slot.getSpec();
        UpdateSlotRequest dto = mapper.map(spec);
        try (var response = slotClient.createOrUpdateSlot(spec.getProductName(), spec.getAppId(), dto)) {
            log.info("Update micro-fronted response {}", response.getStatus());
            return response.getStatus();
        }
    }

}

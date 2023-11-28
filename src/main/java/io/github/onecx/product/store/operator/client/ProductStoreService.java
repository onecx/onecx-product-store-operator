package io.github.onecx.product.store.operator.client;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gen.io.github.onecx.product.store.product.v1.api.OperatorProductApi;
import gen.io.github.onecx.product.store.product.v1.model.UpdateProductRequest;
import io.github.onecx.product.store.operator.Product;
import io.github.onecx.product.store.operator.ProductSpec;
import io.github.onecx.product.store.operator.client.mappers.ProductStoreMapper;

@ApplicationScoped
public class ProductStoreService {

    private static final Logger log = LoggerFactory.getLogger(ProductStoreService.class);

    @Inject
    ProductStoreMapper mapper;

    @Inject
    @RestClient
    OperatorProductApi client;

    public int updateProduct(Product product) {
        ProductSpec spec = product.getSpec();
        UpdateProductRequest dto = mapper.map(spec);
        try (var response = client.createOrUpdateProduct(spec.getName(), dto)) {
            log.info("Update product response {}", response.getStatus());
            return response.getStatus();
        }
    }

}

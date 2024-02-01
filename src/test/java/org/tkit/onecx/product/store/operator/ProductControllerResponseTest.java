package org.tkit.onecx.product.store.operator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import jakarta.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.tkit.onecx.product.store.operator.client.ProductStoreService;
import org.tkit.onecx.product.store.test.AbstractTest;

import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class ProductControllerResponseTest extends AbstractTest {

    @InjectMock
    ProductStoreService productStoreService;

    @Inject
    ProductController reconciler;

    @BeforeEach
    void beforeAll() {
        Mockito.when(productStoreService.updateProduct(any())).thenReturn(404);
    }

    @Test
    void testWrongResponse() throws Exception {

        var s = new ProductSpec();
        s.setName("product");
        s.setDescription("description");
        s.setBasePath("basePath");
        s.setImageUrl("imageUrl");

        Product m = new Product();
        m.setSpec(s);

        UpdateControl<Product> result = reconciler.reconcile(m, null);
        assertThat(result).isNotNull();
        assertThat(result.getResource()).isNotNull();
        assertThat(result.getResource().getStatus()).isNotNull();
        assertThat(result.getResource().getStatus().getStatus()).isNotNull().isEqualTo(ProductStatus.Status.UNDEFINED);

    }
}

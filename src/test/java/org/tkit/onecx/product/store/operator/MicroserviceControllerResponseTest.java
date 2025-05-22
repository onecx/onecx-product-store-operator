package org.tkit.onecx.product.store.operator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import jakarta.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.tkit.onecx.product.store.operator.client.ProductStoreService;
import org.tkit.onecx.product.store.operator.microservice.Microservice;
import org.tkit.onecx.product.store.operator.microservice.MicroserviceController;
import org.tkit.onecx.product.store.operator.microservice.MicroserviceSpec;
import org.tkit.onecx.product.store.operator.microservice.MicroserviceStatus;
import org.tkit.onecx.product.store.test.AbstractTest;

import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class MicroserviceControllerResponseTest extends AbstractTest {

    @InjectMock
    ProductStoreService productStoreService;

    @Inject
    MicroserviceController reconciler;

    @BeforeEach
    void beforeAll() {
        Mockito.when(productStoreService.updateMicroservice(any())).thenReturn(404);
    }

    @Test
    void testWrongResponse() throws Exception {

        MicroserviceSpec s = new MicroserviceSpec();
        s.setProductName("product");
        s.setAppId("m1");
        s.setVersion("m1");
        s.setName("m1");

        Microservice m = new Microservice();
        m.setSpec(s);

        UpdateControl<Microservice> result = reconciler.reconcile(m, null);
        assertThat(result).isNotNull();
        assertThat(result.getResource()).isNotNull().isPresent();
        assertThat(result.getResource()).isPresent();
        assertThat(result.getResource().get().getStatus()).isNotNull();
        assertThat(result.getResource().get().getStatus().getStatus()).isNotNull()
                .isEqualTo(MicroserviceStatus.Status.UNDEFINED);

    }
}

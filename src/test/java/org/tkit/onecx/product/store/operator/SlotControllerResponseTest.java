package org.tkit.onecx.product.store.operator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import jakarta.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.tkit.onecx.product.store.operator.client.ProductStoreService;
import org.tkit.onecx.product.store.operator.microservice.MicroserviceStatus;
import org.tkit.onecx.product.store.operator.slot.Slot;
import org.tkit.onecx.product.store.operator.slot.SlotController;
import org.tkit.onecx.product.store.operator.slot.SlotSpec;
import org.tkit.onecx.product.store.test.AbstractTest;

import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class SlotControllerResponseTest extends AbstractTest {

    @InjectMock
    ProductStoreService productStoreService;

    @Inject
    SlotController controller;

    @BeforeEach
    void beforeAll() {
        Mockito.when(productStoreService.updateSlot(any())).thenReturn(404);
    }

    @Test
    void testWrongResponse() throws Exception {

        var s = new SlotSpec();
        s.setProductName("product");
        s.setAppId("m1");
        s.setName("m1");

        var m = new Slot();
        m.setSpec(s);

        UpdateControl<Slot> result = controller.reconcile(m, null);
        assertThat(result).isNotNull();
        assertThat(result.getResource()).isNotNull().isPresent();
        assertThat(result.getResource()).isPresent();
        assertThat(result.getResource().get().getStatus()).isNotNull();
        assertThat(result.getResource().get().getStatus().getStatus()).isNotNull()
                .isEqualTo(MicroserviceStatus.Status.UNDEFINED);

    }
}

package org.tkit.onecx.product.store.operator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import jakarta.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.tkit.onecx.product.store.operator.client.ProductStoreService;
import org.tkit.onecx.product.store.operator.microfrontend.Microfrontend;
import org.tkit.onecx.product.store.operator.microfrontend.MicrofrontendController;
import org.tkit.onecx.product.store.operator.microfrontend.MicrofrontendSpec;
import org.tkit.onecx.product.store.operator.microfrontend.MicrofrontendStatus;
import org.tkit.onecx.product.store.test.AbstractTest;

import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class MicrofrontendControllerResponseTest extends AbstractTest {

    @InjectMock
    ProductStoreService productStoreService;

    @Inject
    MicrofrontendController reconciler;

    @BeforeEach
    void beforeAll() {
        Mockito.when(productStoreService.updateMicrofrontend(any())).thenReturn(404);
    }

    @Test
    void testWrongResponse() throws Exception {

        MicrofrontendSpec s = new MicrofrontendSpec();
        s.setProductName("product");
        s.setAppId("m1");
        s.setAppVersion("m1");
        s.setAppName("m1");

        Microfrontend m = new Microfrontend();
        m.setSpec(s);

        UpdateControl<Microfrontend> result = reconciler.reconcile(m, null);
        assertThat(result).isNotNull();
        assertThat(result.getResource()).isNotNull().isPresent();
        assertThat(result.getResource()).isPresent();
        assertThat(result.getResource().get().getStatus()).isNotNull();
        assertThat(result.getResource().get().getStatus().getStatus()).isNotNull()
                .isEqualTo(MicrofrontendStatus.Status.UNDEFINED);

    }
}

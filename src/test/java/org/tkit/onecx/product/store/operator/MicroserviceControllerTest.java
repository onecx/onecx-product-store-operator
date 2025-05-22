package org.tkit.onecx.product.store.operator;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.stream.Stream;

import jakarta.inject.Inject;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tkit.onecx.product.store.operator.microservice.Microservice;
import org.tkit.onecx.product.store.operator.microservice.MicroserviceSpec;
import org.tkit.onecx.product.store.operator.microservice.MicroserviceStatus;
import org.tkit.onecx.product.store.test.AbstractTest;

import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.Operator;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class MicroserviceControllerTest extends AbstractTest {

    static final Logger log = LoggerFactory.getLogger(MicroserviceControllerTest.class);

    @Inject
    Operator operator;

    @Inject
    KubernetesClient client;

    @BeforeAll
    static void init() {
        Awaitility.setDefaultPollDelay(2, SECONDS);
        Awaitility.setDefaultPollInterval(2, SECONDS);
        Awaitility.setDefaultTimeout(10, SECONDS);
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void microserviceTest(String name, MicroserviceSpec spec, MicroserviceStatus.Status status) {

        operator.start();

        Microservice microservice = new Microservice();
        microservice.setMetadata(new ObjectMetaBuilder().withName(name).withNamespace(client.getNamespace()).build());
        microservice.setSpec(spec);

        log.info("Creating test microservice object: {}", microservice);
        client.resource(microservice).serverSideApply();

        log.info("Waiting 4 seconds and status muss be still null");

        await().pollDelay(2, SECONDS).untilAsserted(() -> {
            MicroserviceStatus mfeStatus = client.resource(microservice).get().getStatus();
            assertThat(mfeStatus).isNotNull();
            assertThat(mfeStatus.getStatus()).isNotNull().isEqualTo(status);
        });
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of("test-1", createSpec("test-1", "product-test"), MicroserviceStatus.Status.CREATED),
                Arguments.of("test-2", createSpec("test-2", "product-test-2"), MicroserviceStatus.Status.CREATED),
                Arguments.of("test-3", createSpec("test-3", "product-test-2"), MicroserviceStatus.Status.UPDATED),
                Arguments.of("test-error-1", createSpec("test-error-1", "product-test-2"),
                        MicroserviceStatus.Status.ERROR),
                Arguments.of("test-error-2", createSpec("test-error-2", "product-test-2"),
                        MicroserviceStatus.Status.ERROR));
    }

    private static MicroserviceSpec createSpec(String appId, String productName) {
        MicroserviceSpec spec = new MicroserviceSpec();
        spec.setAppId(appId);
        spec.setProductName(productName);
        spec.setVersion("app-version");
        spec.setName("app-version");
        spec.setType("note");
        spec.setDescription("description");
        return spec;
    }

    @Test
    void microserviceEmptySpecTest() {

        operator.start();

        Microservice microservice = new Microservice();
        microservice.setMetadata(new ObjectMetaBuilder().withName("empty-spec").withNamespace(client.getNamespace()).build());
        microservice.setSpec(new MicroserviceSpec());

        client.resource(microservice).serverSideApply();

        await().pollDelay(2, SECONDS).untilAsserted(() -> {
            MicroserviceStatus mfeStatus = client.resource(microservice).get().getStatus();
            assertThat(mfeStatus).isNotNull();
            assertThat(mfeStatus.getStatus()).isNotNull().isEqualTo(MicroserviceStatus.Status.ERROR);
        });
    }

    @Test
    void microserviceNullSpecTest() {

        operator.start();

        Microservice microservice = new Microservice();
        microservice.setMetadata(new ObjectMetaBuilder().withName("null-spec").withNamespace(client.getNamespace()).build());
        microservice.setSpec(null);

        client.resource(microservice).serverSideApply();

        await().pollDelay(4, SECONDS).untilAsserted(() -> {
            MicroserviceStatus mfeStatus = client.resource(microservice).get().getStatus();
            assertThat(mfeStatus).isNull();
        });

    }

    @Test
    void microserviceUpdateEmptySpecTest() {

        operator.start();

        var m = new MicroserviceSpec();
        m.setAppId("test-1");
        m.setProductName("product-test");

        Microservice microservice = new Microservice();
        microservice
                .setMetadata(new ObjectMetaBuilder().withName("to-update-spec").withNamespace(client.getNamespace()).build());
        microservice.setSpec(m);

        client.resource(microservice).serverSideApply();

        await().pollDelay(2, SECONDS).untilAsserted(() -> {
            MicroserviceStatus mfeStatus = client.resource(microservice).get().getStatus();
            assertThat(mfeStatus).isNotNull();
            assertThat(mfeStatus.getStatus()).isNotNull().isEqualTo(MicroserviceStatus.Status.CREATED);
        });

        client.resource(microservice).inNamespace(client.getNamespace())
                .edit(s -> {
                    s.setSpec(null);
                    return s;
                });

        await().pollDelay(4, SECONDS).untilAsserted(() -> {
            MicroserviceStatus mfeStatus = client.resource(microservice).get().getStatus();
            assertThat(mfeStatus).isNotNull();
            assertThat(mfeStatus.getStatus()).isNotNull().isEqualTo(MicroserviceStatus.Status.CREATED);
        });
    }
}

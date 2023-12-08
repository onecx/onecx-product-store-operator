package io.github.onecx.product.store.operator;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.Set;
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

import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.github.onecx.product.store.test.AbstractTest;
import io.javaoperatorsdk.operator.Operator;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class ProductControllerTest extends AbstractTest {

    final static Logger log = LoggerFactory.getLogger(ProductControllerTest.class);

    @Inject
    Operator operator;

    @Inject
    KubernetesClient client;

    @BeforeAll
    public static void init() {
        Awaitility.setDefaultPollDelay(2, SECONDS);
        Awaitility.setDefaultPollInterval(2, SECONDS);
        Awaitility.setDefaultTimeout(10, SECONDS);
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void productTest(String name, ProductSpec spec, ProductStatus.Status status) {

        operator.start();

        Product data = new Product();
        data.setMetadata(new ObjectMetaBuilder().withName(name).withNamespace(client.getNamespace()).build());
        data.setSpec(spec);

        log.info("Creating test product object: {}", data);
        client.resource(data).serverSideApply();

        log.info("Waiting 4 seconds and status muss be still null");

        await().pollDelay(2, SECONDS).untilAsserted(() -> {
            ProductStatus mfeStatus = client.resource(data).get().getStatus();
            assertThat(mfeStatus).isNotNull();
            assertThat(mfeStatus.getStatus()).isNotNull().isEqualTo(status);
        });
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of("test-1", createSpec("test-1", "/test1"), ProductStatus.Status.CREATED),
                Arguments.of("test-2", createSpec("test-2", "/test2"), ProductStatus.Status.CREATED),
                Arguments.of("test-3", createSpec("test-3", "/test3"), ProductStatus.Status.UPDATED),
                Arguments.of("test-error-1", createSpec("test-error-1", "/test2"), ProductStatus.Status.ERROR),
                Arguments.of("test-error-2", createSpec("test-error-2", "/test2"), ProductStatus.Status.ERROR));
    }

    private static ProductSpec createSpec(String name, String basePath) {
        ProductSpec spec = new ProductSpec();
        spec.setName(name);
        spec.setBasePath(basePath);
        spec.setVersion("0.0.0");
        spec.setClassifications(Set.of("a", "b"));
        spec.setIconName("icon1");
        spec.setDisplayName("dp");
        spec.setDescription("description");
        spec.setImageUrl("imageUrl");
        return spec;
    }

    @Test
    void productEmptySpecTest() {

        operator.start();

        Product data = new Product();
        data.setMetadata(new ObjectMetaBuilder().withName("empty-spec").withNamespace(client.getNamespace()).build());
        data.setSpec(new ProductSpec());

        log.info("Creating test product object: {}", data);
        client.resource(data).serverSideApply();

        log.info("Waiting 4 seconds and status muss be still null");

        await().pollDelay(2, SECONDS).untilAsserted(() -> {
            ProductStatus mfeStatus = client.resource(data).get().getStatus();
            assertThat(mfeStatus).isNotNull();
            assertThat(mfeStatus.getStatus()).isNotNull().isEqualTo(ProductStatus.Status.ERROR);
        });
    }

    @Test
    void productNullSpecTest() {

        operator.start();

        Product data = new Product();
        data.setMetadata(new ObjectMetaBuilder().withName("null-spec").withNamespace(client.getNamespace()).build());
        data.setSpec(null);

        log.info("Creating test product object: {}", data);
        client.resource(data).serverSideApply();

        log.info("Waiting 4 seconds and status muss be still null");

        await().pollDelay(4, SECONDS).untilAsserted(() -> {
            ProductStatus mfeStatus = client.resource(data).get().getStatus();
            assertThat(mfeStatus).isNull();
        });

    }

    @Test
    void productUpdateEmptySpecTest() {

        operator.start();

        var m = new ProductSpec();
        m.setName("test-1");
        m.setBasePath("product-test");

        var data = new Product();
        data
                .setMetadata(new ObjectMetaBuilder().withName("to-update-spec").withNamespace(client.getNamespace()).build());
        data.setSpec(m);

        log.info("Creating test product object: {}", data);
        client.resource(data).serverSideApply();

        log.info("Waiting 4 seconds and status muss be still null");

        await().pollDelay(2, SECONDS).untilAsserted(() -> {
            ProductStatus mfeStatus = client.resource(data).get().getStatus();
            assertThat(mfeStatus).isNotNull();
            assertThat(mfeStatus.getStatus()).isNotNull().isEqualTo(ProductStatus.Status.CREATED);
        });

        client.resource(data).inNamespace(client.getNamespace())
                .edit(s -> {
                    s.setSpec(null);
                    return s;
                });

        await().pollDelay(4, SECONDS).untilAsserted(() -> {
            ProductStatus mfeStatus = client.resource(data).get().getStatus();
            assertThat(mfeStatus).isNotNull();
            assertThat(mfeStatus.getStatus()).isNotNull().isEqualTo(ProductStatus.Status.CREATED);
        });
    }
}

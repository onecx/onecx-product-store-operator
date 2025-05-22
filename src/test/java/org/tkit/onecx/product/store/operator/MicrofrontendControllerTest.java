package org.tkit.onecx.product.store.operator;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.List;
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
import org.tkit.onecx.product.store.operator.microfrontend.Microfrontend;
import org.tkit.onecx.product.store.operator.microfrontend.MicrofrontendEndpointSpec;
import org.tkit.onecx.product.store.operator.microfrontend.MicrofrontendSpec;
import org.tkit.onecx.product.store.operator.microfrontend.MicrofrontendStatus;
import org.tkit.onecx.product.store.test.AbstractTest;

import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.Operator;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class MicrofrontendControllerTest extends AbstractTest {

    static final Logger log = LoggerFactory.getLogger(MicrofrontendControllerTest.class);

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
    void microfrontendTest(String name, MicrofrontendSpec spec, MicrofrontendStatus.Status status) {

        operator.start();

        Microfrontend microfrontend = new Microfrontend();
        microfrontend.setMetadata(new ObjectMetaBuilder().withName(name).withNamespace(client.getNamespace()).build());
        microfrontend.setSpec(spec);

        log.info("Creating test microfrontend object: {}", microfrontend);
        client.resource(microfrontend).serverSideApply();

        log.info("Waiting 4 seconds and status muss be still null");

        await().pollDelay(2, SECONDS).untilAsserted(() -> {
            MicrofrontendStatus mfeStatus = client.resource(microfrontend).get().getStatus();
            assertThat(mfeStatus).isNotNull();
            assertThat(mfeStatus.getStatus()).isNotNull().isEqualTo(status);
        });
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of("test-1", createSpec("test-1", "product-test", "/test1"), MicrofrontendStatus.Status.CREATED),
                Arguments.of("test-2", createSpec("test-2", "product-test", "/test2"), MicrofrontendStatus.Status.CREATED),
                Arguments.of("test-3", createSpec("test-3", "product-test", "/test3"), MicrofrontendStatus.Status.UPDATED),
                Arguments.of("test-error-1", createSpec("test-error-1", "product-test2", "/test2"),
                        MicrofrontendStatus.Status.ERROR),
                Arguments.of("test-error-2", createSpec("test-error-2", "product-test", "/test2"),
                        MicrofrontendStatus.Status.ERROR));
    }

    private static MicrofrontendSpec createSpec(String appId, String productName, String basePath) {
        MicrofrontendEndpointSpec e = new MicrofrontendEndpointSpec();
        e.setName("n1");
        e.setPath("p");

        MicrofrontendSpec spec = new MicrofrontendSpec();
        spec.setAppId(appId);
        spec.setProductName(productName);
        spec.setRemoteBaseUrl("test");
        spec.setExposedModule("test");
        spec.setTechnology("wc");
        spec.setAppVersion("app-version");
        spec.setAppName("app-version");
        spec.setNote("note");
        spec.setTagName("tagName");
        spec.setRemoteName("remoteName");
        spec.setContact("contact");
        spec.setRemoteBaseUrl(basePath);
        spec.setIconName("icon1");
        spec.setDescription("description");
        spec.setRemoteEntry("re");
        spec.setClassifications(Set.of("c1", "c2"));
        spec.setEndpoints(List.of(e));
        spec.setDeprecated(false);
        spec.setType(MicrofrontendSpec.Microfrontend.COMPONENT);
        return spec;
    }

    @Test
    void microfrontendEmptySpecTest() {

        operator.start();

        Microfrontend microfrontend = new Microfrontend();
        microfrontend.setMetadata(new ObjectMetaBuilder().withName("empty-spec").withNamespace(client.getNamespace()).build());
        microfrontend.setSpec(new MicrofrontendSpec());

        log.info("Creating test microfrontend object: {}", microfrontend);
        client.resource(microfrontend).serverSideApply();

        log.info("Waiting 4 seconds and status muss be still null");

        await().pollDelay(2, SECONDS).untilAsserted(() -> {
            MicrofrontendStatus mfeStatus = client.resource(microfrontend).get().getStatus();
            assertThat(mfeStatus).isNotNull();
            assertThat(mfeStatus.getStatus()).isNotNull().isEqualTo(MicrofrontendStatus.Status.ERROR);
        });
    }

    @Test
    void microfrontendNullSpecTest() {

        operator.start();

        Microfrontend microfrontend = new Microfrontend();
        microfrontend.setMetadata(new ObjectMetaBuilder().withName("null-spec").withNamespace(client.getNamespace()).build());
        microfrontend.setSpec(null);

        log.info("Creating test microfrontend object: {}", microfrontend);
        client.resource(microfrontend).serverSideApply();

        log.info("Waiting 4 seconds and status muss be still null");

        await().pollDelay(4, SECONDS).untilAsserted(() -> {
            MicrofrontendStatus mfeStatus = client.resource(microfrontend).get().getStatus();
            assertThat(mfeStatus).isNull();
        });

    }

    @Test
    void microfrontendUpdateEmptySpecTest() {

        operator.start();

        var m = new MicrofrontendSpec();
        m.setAppId("test-1");
        m.setProductName("product-test");

        Microfrontend microfrontend = new Microfrontend();
        microfrontend
                .setMetadata(new ObjectMetaBuilder().withName("to-update-spec").withNamespace(client.getNamespace()).build());
        microfrontend.setSpec(m);

        log.info("Creating test microfrontend object: {}", microfrontend);
        client.resource(microfrontend).serverSideApply();

        log.info("Waiting 4 seconds and status muss be still null");

        await().pollDelay(2, SECONDS).untilAsserted(() -> {
            MicrofrontendStatus mfeStatus = client.resource(microfrontend).get().getStatus();
            assertThat(mfeStatus).isNotNull();
            assertThat(mfeStatus.getStatus()).isNotNull().isEqualTo(MicrofrontendStatus.Status.CREATED);
        });

        client.resource(microfrontend).inNamespace(client.getNamespace())
                .edit(s -> {
                    s.setSpec(null);
                    return s;
                });

        await().pollDelay(4, SECONDS).untilAsserted(() -> {
            MicrofrontendStatus mfeStatus = client.resource(microfrontend).get().getStatus();
            assertThat(mfeStatus).isNotNull();
            assertThat(mfeStatus.getStatus()).isNotNull().isEqualTo(MicrofrontendStatus.Status.CREATED);
        });
    }
}

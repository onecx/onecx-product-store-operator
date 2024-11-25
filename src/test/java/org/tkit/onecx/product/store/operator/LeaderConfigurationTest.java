package org.tkit.onecx.product.store.operator;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;
import org.tkit.onecx.product.store.test.AbstractTest;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class LeaderConfigurationTest extends AbstractTest {

    @Inject
    OperatorConfig dataConfig;

    @Inject
    LeaderConfiguration leaderConfiguration;

    @Test
    void testLeaderConfiguration() {
        assertThat(dataConfig).isNotNull();
        assertThat(dataConfig.leaderElectionConfig()).isNotNull();
        assertThat(leaderConfiguration).isNotNull();
        assertThat(leaderConfiguration.getLeaseName()).isNotNull()
                .isEqualTo(dataConfig.leaderElectionConfig().leaseName());
    }
}

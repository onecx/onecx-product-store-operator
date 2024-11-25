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

    LeaderConfiguration leaderConfiguration;

    @Inject
    LeaderConfigurationFactory factory;

    @Test
    void testProductLeaderConfiguration() {
        this.leaderConfiguration = factory.createForProduct();
        assertThat(dataConfig).isNotNull();
        assertThat(dataConfig.productConfig().leaderElectionConfig()).isNotNull();
        assertThat(leaderConfiguration).isNotNull();
        assertThat(leaderConfiguration.getLeaseName()).isNotNull()
                .isEqualTo(dataConfig.productConfig().leaderElectionConfig().leaseName());
    }

    @Test
    void testSlotLeaderConfiguration() {
        this.leaderConfiguration = factory.createForSlot();
        assertThat(dataConfig).isNotNull();
        assertThat(dataConfig.slotConfig().leaderElectionConfig()).isNotNull();
        assertThat(leaderConfiguration).isNotNull();
        assertThat(leaderConfiguration.getLeaseName()).isNotNull()
                .isEqualTo(dataConfig.slotConfig().leaderElectionConfig().leaseName());
    }
}

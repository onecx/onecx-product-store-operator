package org.tkit.onecx.product.store.operator;

import jakarta.inject.Singleton;

import io.javaoperatorsdk.operator.api.config.LeaderElectionConfiguration;

@Singleton
public class LeaderConfiguration extends LeaderElectionConfiguration {

    public LeaderConfiguration(OperatorConfig.ProductConfig productConfig) {
        super(productConfig.leaderElectionConfig().leaseName());
    }

    public LeaderConfiguration(OperatorConfig.SlotConfig slotConfig) {
        super(slotConfig.leaderElectionConfig().leaseName());
    }
}
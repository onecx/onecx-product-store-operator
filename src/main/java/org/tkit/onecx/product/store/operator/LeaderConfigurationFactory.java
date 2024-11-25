package org.tkit.onecx.product.store.operator;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class LeaderConfigurationFactory {

    private final OperatorConfig operatorConfig;

    @Inject
    public LeaderConfigurationFactory(OperatorConfig operatorConfig) {
        this.operatorConfig = operatorConfig;
    }

    public LeaderConfiguration createForProduct() {
        return new LeaderConfiguration(operatorConfig.productConfig());
    }

    public LeaderConfiguration createForSlot() {
        return new LeaderConfiguration(operatorConfig.slotConfig());
    }
}
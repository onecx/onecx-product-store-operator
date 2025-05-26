package org.tkit.onecx.product.store.operator.slot;

import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tkit.onecx.product.store.operator.CustomResourceStatus;
import org.tkit.onecx.product.store.operator.client.ProductStoreService;
import org.tkit.onecx.quarkus.operator.OperatorUtils;

import io.javaoperatorsdk.operator.api.config.informer.Informer;
import io.javaoperatorsdk.operator.api.reconciler.*;
import io.javaoperatorsdk.operator.processing.event.source.filter.OnAddFilter;
import io.javaoperatorsdk.operator.processing.event.source.filter.OnUpdateFilter;

@ControllerConfiguration(name = "slot", informer = @Informer(name = "parameter", namespaces = Constants.WATCH_CURRENT_NAMESPACE, onAddFilter = SlotController.SlotAddFilter.class, onUpdateFilter = SlotController.SlotUpdateFilter.class))
public class SlotController implements Reconciler<Slot> {

    private static final Logger log = LoggerFactory.getLogger(SlotController.class);

    @Inject
    ProductStoreService service;

    @Override
    public UpdateControl<Slot> reconcile(Slot slot, Context<Slot> context)
            throws Exception {

        String appId = slot.getSpec().getAppId();
        String productName = slot.getSpec().getProductName();
        String name = slot.getSpec().getName();

        log.info("Reconcile microservice: {} for product: {}, name: {}", appId, productName, name);
        int responseCode = service.updateSlot(slot);

        updateStatusPojo(slot, responseCode);
        log.info("Microservice '{}' reconciled - updating status", slot.getMetadata().getName());
        return UpdateControl.patchStatus(slot);

    }

    @Override
    public ErrorStatusUpdateControl<Slot> updateErrorStatus(Slot slot,
            Context<Slot> context, Exception e) {

        int responseCode = -1;
        if (e.getCause() instanceof WebApplicationException re) {
            responseCode = re.getResponse().getStatus();
        }

        log.error("Error reconcile resource", e);
        var status = new SlotStatus();
        status.setRequestProductName(null);
        status.setRequestAppId(null);
        status.setRequestName(null);
        status.setResponseCode(responseCode);
        status.setStatus(CustomResourceStatus.Status.ERROR);
        status.setMessage(e.getMessage());
        slot.setStatus(status);
        return ErrorStatusUpdateControl.patchStatus(slot);
    }

    private void updateStatusPojo(Slot slot, int responseCode) {
        var result = new SlotStatus();
        var spec = slot.getSpec();
        result.setRequestProductName(spec.getProductName());
        result.setRequestAppId(spec.getAppId());
        result.setRequestName(spec.getName());
        result.setResponseCode(responseCode);
        var status = switch (responseCode) {
            case 201:
                yield CustomResourceStatus.Status.CREATED;
            case 200:
                yield CustomResourceStatus.Status.UPDATED;
            default:
                yield CustomResourceStatus.Status.UNDEFINED;
        };
        result.setStatus(status);
        slot.setStatus(result);
    }

    public static class SlotAddFilter implements OnAddFilter<Slot> {

        @Override
        public boolean accept(Slot resource) {
            return OperatorUtils.shouldProcessAdd(resource);
        }
    }

    public static class SlotUpdateFilter implements OnUpdateFilter<Slot> {

        @Override
        public boolean accept(Slot newResource, Slot oldResource) {
            return OperatorUtils.shouldProcessUpdate(newResource, oldResource);
        }
    }
}

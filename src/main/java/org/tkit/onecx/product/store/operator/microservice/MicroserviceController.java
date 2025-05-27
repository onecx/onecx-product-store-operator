package org.tkit.onecx.product.store.operator.microservice;

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

@ControllerConfiguration(name = "microservice", informer = @Informer(name = "parameter", namespaces = Constants.WATCH_CURRENT_NAMESPACE, onAddFilter = MicroserviceController.MicrofrontendAddFilter.class, onUpdateFilter = MicroserviceController.MicrofrontendUpdateFilter.class))
public class MicroserviceController implements Reconciler<Microservice> {

    private static final Logger log = LoggerFactory.getLogger(MicroserviceController.class);

    @Inject
    ProductStoreService service;

    @Override
    public UpdateControl<Microservice> reconcile(Microservice microservice, Context<Microservice> context)
            throws Exception {

        String appId = microservice.getSpec().getAppId();
        String productName = microservice.getSpec().getProductName();

        log.info("Reconcile microservice: {} for product: {}", appId, productName);
        int responseCode = service.updateMicroservice(microservice);

        updateStatusPojo(microservice, responseCode);
        log.info("Microservice '{}' reconciled - updating status", microservice.getMetadata().getName());
        return UpdateControl.patchStatus(microservice);

    }

    @Override
    public ErrorStatusUpdateControl<Microservice> updateErrorStatus(Microservice microfrontend,
            Context<Microservice> context, Exception e) {

        int responseCode = -1;
        if (e.getCause() instanceof WebApplicationException re) {
            responseCode = re.getResponse().getStatus();
        }

        log.error("Error reconcile resource", e);
        MicroserviceStatus status = new MicroserviceStatus();
        status.setRequestProductName(null);
        status.setRequestAppId(null);
        status.setRequestAppName(null);
        status.setRequestAppVersion(null);
        status.setResponseCode(responseCode);
        status.setStatus(CustomResourceStatus.Status.ERROR);
        status.setMessage(e.getMessage());
        microfrontend.setStatus(status);
        return ErrorStatusUpdateControl.patchStatus(microfrontend);
    }

    private void updateStatusPojo(Microservice microfrontend, int responseCode) {
        MicroserviceStatus result = new MicroserviceStatus();
        MicroserviceSpec spec = microfrontend.getSpec();
        result.setRequestProductName(spec.getProductName());
        result.setRequestAppId(spec.getAppId());
        result.setRequestAppName(spec.getName());
        result.setRequestAppVersion(spec.getVersion());
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
        microfrontend.setStatus(result);
    }

    public static class MicrofrontendAddFilter implements OnAddFilter<Microservice> {

        @Override
        public boolean accept(Microservice resource) {
            return OperatorUtils.shouldProcessAdd(resource);
        }
    }

    public static class MicrofrontendUpdateFilter implements OnUpdateFilter<Microservice> {

        @Override
        public boolean accept(Microservice newResource, Microservice oldResource) {
            return OperatorUtils.shouldProcessUpdate(newResource, oldResource);
        }
    }

}

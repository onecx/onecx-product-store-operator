package org.tkit.onecx.product.store.operator.microservice;

import static io.javaoperatorsdk.operator.api.reconciler.Constants.WATCH_CURRENT_NAMESPACE;

import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tkit.onecx.product.store.operator.client.ProductStoreService;

import io.javaoperatorsdk.operator.api.reconciler.*;
import io.javaoperatorsdk.operator.processing.event.source.filter.OnAddFilter;
import io.javaoperatorsdk.operator.processing.event.source.filter.OnUpdateFilter;

@ControllerConfiguration(name = "microservice", namespaces = WATCH_CURRENT_NAMESPACE, onAddFilter = MicroserviceController.MicrofrontendAddFilter.class, onUpdateFilter = MicroserviceController.MicrofrontendUpdateFilter.class)
public class MicroserviceController implements Reconciler<Microservice>, ErrorStatusHandler<Microservice> {

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
        return UpdateControl.updateStatus(microservice);

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
        status.setStatus(MicroserviceStatus.Status.ERROR);
        status.setMessage(e.getMessage());
        microfrontend.setStatus(status);
        return ErrorStatusUpdateControl.updateStatus(microfrontend);
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
                yield MicroserviceStatus.Status.CREATED;
            case 200:
                yield MicroserviceStatus.Status.UPDATED;
            default:
                yield MicroserviceStatus.Status.UNDEFINED;
        };
        result.setStatus(status);
        microfrontend.setStatus(result);
    }

    public static class MicrofrontendAddFilter implements OnAddFilter<Microservice> {

        @Override
        public boolean accept(Microservice resource) {
            return resource.getSpec() != null;
        }
    }

    public static class MicrofrontendUpdateFilter implements OnUpdateFilter<Microservice> {

        @Override
        public boolean accept(Microservice newResource, Microservice oldResource) {
            return newResource.getSpec() != null;
        }
    }

}

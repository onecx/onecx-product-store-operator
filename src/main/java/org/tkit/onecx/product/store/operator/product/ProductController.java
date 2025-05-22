package org.tkit.onecx.product.store.operator.product;

import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tkit.onecx.product.store.operator.CustomResourceStatus;
import org.tkit.onecx.product.store.operator.client.ProductStoreService;

import io.javaoperatorsdk.operator.api.config.informer.Informer;
import io.javaoperatorsdk.operator.api.reconciler.*;
import io.javaoperatorsdk.operator.processing.event.source.filter.OnAddFilter;
import io.javaoperatorsdk.operator.processing.event.source.filter.OnUpdateFilter;

@ControllerConfiguration(name = "product", informer = @Informer(name = "parameter", namespaces = Constants.WATCH_CURRENT_NAMESPACE, onAddFilter = ProductController.AddFilter.class, onUpdateFilter = ProductController.UpdateFilter.class))
public class ProductController implements Reconciler<Product> {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Inject
    ProductStoreService service;

    @Override
    public UpdateControl<Product> reconcile(Product product, Context<Product> context)
            throws Exception {

        log.info("Reconcile resource: {} product-name: {}", product.getMetadata().getName(), product.getSpec().getName());

        int responseCode = service.updateProduct(product);

        updateStatusPojo(product, responseCode);
        log.info("Product '{}' reconciled - updating status", product.getMetadata().getName());
        return UpdateControl.patchStatus(product);

    }

    @Override
    public ErrorStatusUpdateControl<Product> updateErrorStatus(Product product,
            Context<Product> context, Exception e) {
        int responseCode = -1;
        if (e.getCause() instanceof WebApplicationException re) {
            responseCode = re.getResponse().getStatus();
        }

        log.error("Error reconcile resource", e);
        var status = new ProductStatus();
        status.setProductName(null);
        status.setResponseCode(responseCode);
        status.setStatus(CustomResourceStatus.Status.ERROR);
        status.setMessage(e.getMessage());
        product.setStatus(status);
        return ErrorStatusUpdateControl.patchStatus(product);
    }

    private void updateStatusPojo(Product product, int responseCode) {
        ProductStatus result = new ProductStatus();
        ProductSpec spec = product.getSpec();
        result.setProductName(spec.getName());
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
        product.setStatus(result);
    }

    public static class AddFilter implements OnAddFilter<Product> {

        @Override
        public boolean accept(Product resource) {
            return resource.getSpec() != null;
        }
    }

    public static class UpdateFilter implements OnUpdateFilter<Product> {

        @Override
        public boolean accept(Product newResource, Product oldResource) {
            return newResource.getSpec() != null;
        }
    }
}

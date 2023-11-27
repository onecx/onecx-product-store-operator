package io.github.onecx.product.store.operator.product;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Version("v1")
@Group("io.github.onecx.product.store")
public class Product extends CustomResource<ProductSpec, ProductStatus> implements Namespaced {

}

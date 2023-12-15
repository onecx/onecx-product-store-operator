package io.github.onecx.product.store.operator;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Version("v1")
@Group("onecx.github.io")
public class Product extends CustomResource<ProductSpec, ProductStatus> implements Namespaced {

}

package org.tkit.onecx.product.store.operator.product;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductSpec {

    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonProperty(value = "basePath", required = true)
    private String basePath;

    @JsonProperty("description")
    private String description;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty(value = "version", required = true)
    private String version;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("iconName")
    private String iconName;

    @JsonProperty("classifications")
    private Set<String> classifications;

    @JsonProperty("provider")
    private String provider;

    @JsonProperty("multitenancy")
    private Boolean multitenancy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<String> getClassifications() {
        return classifications;
    }

    public void setClassifications(Set<String> classifications) {
        this.classifications = classifications;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Boolean getMultitenancy() {
        return multitenancy;
    }

    public void setMultitenancy(Boolean multitenancy) {
        this.multitenancy = multitenancy;
    }
}

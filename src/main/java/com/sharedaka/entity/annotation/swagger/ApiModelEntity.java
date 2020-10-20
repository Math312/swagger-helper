package com.sharedaka.entity.annotation.swagger;

public class ApiModelEntity {

    private String value;

    private String description;

    private String parent;

    private String discriminator;

    private String[] subTypes;

    private String reference;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public String[] getSubTypes() {
        return subTypes;
    }

    public void setSubTypes(String[] subTypes) {
        this.subTypes = subTypes;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}

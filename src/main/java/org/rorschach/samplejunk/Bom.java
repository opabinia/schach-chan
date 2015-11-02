package org.rorschach.samplejunk;

public class Bom {

    private String productId;

    private String productGroup;

    private String productName;

    public Bom(String productId, String productGroup, String productName) {
        this.productId = productId;
        this.productGroup = productGroup;
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public String getProductName() {
        return productName;
    }
}
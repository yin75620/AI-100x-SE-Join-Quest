package io.ordrex.order.model;

public class OrderItem {
    private final String productName;
    private final String category; // optional, can be null
    private final int quantity;
    private final int unitPrice;

    public OrderItem(String productName, String category, int quantity, int unitPrice) {
        this.productName = productName;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getProductName() { return productName; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }
    public int getUnitPrice() { return unitPrice; }
}


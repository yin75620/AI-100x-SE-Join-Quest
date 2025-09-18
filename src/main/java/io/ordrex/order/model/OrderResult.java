package io.ordrex.order.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class OrderResult {
    private final int originalAmount;
    private final int discount;
    private final int totalAmount;
    private final Map<String, Integer> receivedQuantitiesByProduct = new LinkedHashMap<>();

    public OrderResult(int originalAmount, int discount, int totalAmount) {
        this.originalAmount = originalAmount;
        this.discount = discount;
        this.totalAmount = totalAmount;
    }

    public int getOriginalAmount() { return originalAmount; }
    public int getDiscount() { return discount; }
    public int getTotalAmount() { return totalAmount; }

    public Map<String, Integer> getReceivedQuantitiesByProduct() {
        return receivedQuantitiesByProduct;
    }

    public void setReceivedQuantity(String productName, int quantity) {
        receivedQuantitiesByProduct.put(productName, quantity);
    }
}


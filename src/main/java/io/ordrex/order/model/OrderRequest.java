package io.ordrex.order.model;

import java.util.List;

public class OrderRequest {
    private final List<OrderItem> items;

    public OrderRequest(List<OrderItem> items) {
        this.items = items;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}


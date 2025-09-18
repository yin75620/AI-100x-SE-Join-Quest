package io.ordrex.order.service;

import io.ordrex.order.model.OrderItem;
import io.ordrex.order.model.OrderRequest;
import io.ordrex.order.model.OrderResult;
import io.ordrex.order.model.PromotionsConfig;

public class OrderService {

    public OrderResult price(OrderRequest request, PromotionsConfig promotions) {
        int original = 0;
        for (OrderItem item : request.getItems()) {
            original += item.getQuantity() * item.getUnitPrice();
        }

        int discount = 0;
        boolean double11 = promotions != null && promotions.isDouble11Active();
        if (double11) {
            for (OrderItem item : request.getItems()) {
                int groups = item.getQuantity() / 10;
                if (groups > 0) {
                    // 20% off for each group of 10 of the same product
                    int groupDiscount = groups * 10 * item.getUnitPrice() * 20 / 100;
                    discount += groupDiscount;
                }
            }
        }
        if (promotions != null && promotions.getThresholdPromotion() != null) {
            PromotionsConfig.ThresholdPromotion tp = promotions.getThresholdPromotion();
            if (original >= tp.threshold) {
                discount += tp.discount;
            }
        }

        int total = original - discount;
        OrderResult result = new OrderResult(original, discount, total);

        boolean cosmeticsBogo = promotions != null && promotions.isCosmeticsBogoActive();

        for (OrderItem item : request.getItems()) {
            int freebies = 0;
            if (cosmeticsBogo) {
                String cat = item.getCategory();
                if (cat != null && "cosmetics".equalsIgnoreCase(cat) && item.getQuantity() > 0) {
                    // Rule: 1 extra per distinct cosmetics product (not per quantity)
                    freebies = 1;
                }
            }
            result.setReceivedQuantity(item.getProductName(), item.getQuantity() + freebies);
        }
        return result;
    }
}

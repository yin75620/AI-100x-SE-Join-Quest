package io.ordrex.order.model;

public class PromotionsConfig {
    public static class ThresholdPromotion {
        public final int threshold;
        public final int discount;

        public ThresholdPromotion(int threshold, int discount) {
            this.threshold = threshold;
            this.discount = discount;
        }
    }

    private final ThresholdPromotion thresholdPromotion; // nullable
    private final boolean cosmeticsBogoActive;

    public PromotionsConfig(ThresholdPromotion thresholdPromotion, boolean cosmeticsBogoActive) {
        this.thresholdPromotion = thresholdPromotion;
        this.cosmeticsBogoActive = cosmeticsBogoActive;
    }

    public ThresholdPromotion getThresholdPromotion() {
        return thresholdPromotion;
    }

    public boolean isCosmeticsBogoActive() {
        return cosmeticsBogoActive;
    }
}


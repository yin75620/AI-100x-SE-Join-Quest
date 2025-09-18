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
    private final boolean double11Active;

    public PromotionsConfig(ThresholdPromotion thresholdPromotion, boolean cosmeticsBogoActive) {
        this(thresholdPromotion, cosmeticsBogoActive, false);
    }

    public PromotionsConfig(ThresholdPromotion thresholdPromotion, boolean cosmeticsBogoActive, boolean double11Active) {
        this.thresholdPromotion = thresholdPromotion;
        this.cosmeticsBogoActive = cosmeticsBogoActive;
        this.double11Active = double11Active;
    }

    public ThresholdPromotion getThresholdPromotion() {
        return thresholdPromotion;
    }

    public boolean isCosmeticsBogoActive() {
        return cosmeticsBogoActive;
    }

    public boolean isDouble11Active() {
        return double11Active;
    }
}

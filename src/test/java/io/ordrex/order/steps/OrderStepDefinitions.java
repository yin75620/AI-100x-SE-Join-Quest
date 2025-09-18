package io.ordrex.order.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.ordrex.order.model.OrderItem;
import io.ordrex.order.model.OrderRequest;
import io.ordrex.order.model.OrderResult;
import io.ordrex.order.model.PromotionsConfig;
import io.ordrex.order.service.OrderService;
import org.junit.jupiter.api.Assertions;

import java.util.*;

public class OrderStepDefinitions {

    private PromotionsConfig promotionsConfig;
    private OrderRequest orderRequest;
    private OrderResult orderResult;
    private final OrderService orderService = new OrderService();

    @Given("no promotions are applied")
    public void no_promotions_are_applied() {
        promotionsConfig = new PromotionsConfig(null, false);
    }

    @Given("the threshold discount promotion is configured:")
    public void the_threshold_discount_promotion_is_configured(DataTable table) {
        Map<String, String> row = table.asMaps().get(0);
        int threshold = Integer.parseInt(row.get("threshold"));
        int discount = Integer.parseInt(row.get("discount"));
        PromotionsConfig.ThresholdPromotion tp = new PromotionsConfig.ThresholdPromotion(threshold, discount);
        // keep current cosmetics flag if previously set
        boolean cosmetics = promotionsConfig != null && promotionsConfig.isCosmeticsBogoActive();
        promotionsConfig = new PromotionsConfig(tp, cosmetics);
    }

    @Given("the buy one get one promotion for cosmetics is active")
    public void the_buy_one_get_one_promotion_for_cosmetics_is_active() {
        PromotionsConfig.ThresholdPromotion tp = promotionsConfig != null ? promotionsConfig.getThresholdPromotion() : null;
        promotionsConfig = new PromotionsConfig(tp, true);
    }

    @When("a customer places an order with:")
    public void a_customer_places_an_order_with(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        List<OrderItem> items = new ArrayList<>();
        for (Map<String, String> row : rows) {
            String productName = row.get("productName");
            String category = row.getOrDefault("category", null);
            int quantity = Integer.parseInt(row.get("quantity"));
            int unitPrice = Integer.parseInt(row.get("unitPrice"));
            items.add(new OrderItem(productName, category, quantity, unitPrice));
        }
        orderRequest = new OrderRequest(items);
        orderResult = orderService.price(orderRequest, promotionsConfig);
    }

    @Then("the order summary should be:")
    public void the_order_summary_should_be(DataTable table) {
        Map<String, String> row = table.asMaps().get(0);
        if (row.containsKey("originalAmount")) {
            int expectedOriginal = Integer.parseInt(row.get("originalAmount"));
            Assertions.assertEquals(expectedOriginal, orderResult.getOriginalAmount(), "originalAmount mismatch");
        }
        if (row.containsKey("discount")) {
            int expectedDiscount = Integer.parseInt(row.get("discount"));
            Assertions.assertEquals(expectedDiscount, orderResult.getDiscount(), "discount mismatch");
        }
        if (row.containsKey("totalAmount")) {
            int expectedTotal = Integer.parseInt(row.get("totalAmount"));
            Assertions.assertEquals(expectedTotal, orderResult.getTotalAmount(), "totalAmount mismatch");
        }
    }

    @Then("the customer should receive:")
    public void the_customer_should_receive(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        Map<String, Integer> expected = new LinkedHashMap<>();
        for (Map<String, String> row : rows) {
            expected.put(row.get("productName"), Integer.parseInt(row.get("quantity")));
        }

        for (Map.Entry<String, Integer> entry : expected.entrySet()) {
            String productName = entry.getKey();
            int expectedQty = entry.getValue();
            Integer actualQty = orderResult.getReceivedQuantitiesByProduct().get(productName);
            Assertions.assertNotNull(actualQty, "Missing product in received items: " + productName);
            Assertions.assertEquals(expectedQty, actualQty, "Quantity mismatch for product: " + productName);
        }
    }
}


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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Double11StepDefinitions {
    private final OrderService orderService = new OrderService();
    private PromotionsConfig promotionsConfig;
    private final List<OrderItem> cartItems = new ArrayList<>();
    private OrderResult orderResult;

    @Given("雙十一優惠活動已啟動")
    public void double11_active() {
        // Preserve any other promotions if needed (none by default here)
        PromotionsConfig.ThresholdPromotion tp = promotionsConfig != null ? promotionsConfig.getThresholdPromotion() : null;
        boolean cosmetics = promotionsConfig != null && promotionsConfig.isCosmeticsBogoActive();
        promotionsConfig = new PromotionsConfig(tp, cosmetics, true);
    }

    @Given("有以下商品加入購物車:")
    public void items_in_cart(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String product = row.get("productName");
            int qty = Integer.parseInt(row.get("quantity"));
            int unitPrice = Integer.parseInt(row.get("unitPrice"));
            cartItems.add(new OrderItem(product, null, qty, unitPrice));
        }
    }

    @When("系統計算訂單金額")
    public void calculate_total() {
        OrderRequest request = new OrderRequest(cartItems);
        orderResult = orderService.price(request, promotionsConfig);
    }

    @Then("訂單金額應為:")
    public void total_should_be(DataTable table) {
        int expected = Integer.parseInt(table.asMaps().get(0).get("totalAmount"));
        Assertions.assertEquals(expected, orderResult.getTotalAmount(), "totalAmount mismatch");
    }
}


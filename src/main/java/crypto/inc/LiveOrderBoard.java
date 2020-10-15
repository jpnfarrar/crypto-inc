package crypto.inc;


import crypto.inc.domain.OrderRequest;
import crypto.inc.domain.OrderSummary;
import crypto.inc.domain.OrderType;

import java.util.List;
import java.util.UUID;

public interface LiveOrderBoard {

    UUID placeOrder(OrderRequest request);

    void cancelOrder(UUID orderId);

    List<OrderSummary> top10Orders(OrderType orderType);
}

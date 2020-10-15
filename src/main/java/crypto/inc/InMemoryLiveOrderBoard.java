package crypto.inc;

import crypto.inc.domain.Order;
import crypto.inc.domain.OrderRequest;
import crypto.inc.domain.OrderSummary;
import crypto.inc.domain.OrderType;
import crypto.inc.utility.OrderMerger;

import java.util.*;

import static crypto.inc.domain.OrderType.BUY;
import static java.lang.Math.min;
import static java.util.Comparator.comparing;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

public class InMemoryLiveOrderBoard implements LiveOrderBoard {
    private final Map<UUID, Order> orders = new HashMap<>();
    private final OrderMerger orderMerger;

    public InMemoryLiveOrderBoard(OrderMerger orderMerger) {
        this.orderMerger = orderMerger;
    }

    @Override
    public UUID placeOrder(OrderRequest request) {
        Order order = request.toOrder();
        order.setOrderId(randomUUID());
        this.orders.put(order.getOrderId(), order);
        return order.getOrderId();
    }

    @Override
    public void cancelOrder(UUID orderId) {
        orders.remove(orderId);
    }

    @Override
    public List<OrderSummary> top10Orders(OrderType orderType) {
        List<Order> sortedOrders = orders.values().stream()
                .filter(order -> orderType.equals(order.getType()))
                .collect(toList());
        List<OrderSummary> summaries = orderMerger.merge(sortedOrders);

        Comparator<OrderSummary> comparing = comparing(OrderSummary::getPrice);
        if (BUY.equals(orderType)) {
            comparing = comparing.reversed();
        }
        summaries.sort(comparing.thenComparing(o -> o.getCoinType().name()));
        return summaries.subList(0, min(10, summaries.size()));
    }

    public Collection<Order> orders() {
        return orders.values();
    }
}

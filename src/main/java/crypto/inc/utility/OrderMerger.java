package crypto.inc.utility;

import crypto.inc.domain.MergeKey;
import crypto.inc.domain.Order;
import crypto.inc.domain.OrderSummary;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.*;

public class OrderMerger {

    public List<OrderSummary> merge(Collection<Order> orders) {
        if (orders.stream().map(Order::getType).collect(toSet()).size() != 1) {
            throw new UnsupportedOperationException("Multiple order types not supported");
        }
        return orders.stream()
                .map(OrderSummary::orderSummaryFrom)
                .collect(groupingBy(MergeKey::from, collectingAndThen(toList(), this::mergeOrders)))
                .values()
                .stream()
                .map(collection -> collection.iterator().next())
                .collect(toList());
    }

    private List<OrderSummary> mergeOrders(List<OrderSummary> matchedByPrice) {
        OrderSummary summary = matchedByPrice.iterator().next();
        return singletonList(OrderSummary.builder()
                .coinType(summary.getCoinType())
                .price(summary.getPrice())
                .total(calculateTotalOrZero(matchedByPrice))
                .build());
    }

    private BigDecimal calculateTotalOrZero(List<OrderSummary> matchedByPrice) {
        return matchedByPrice.stream().map(OrderSummary::getTotal).reduce(BigDecimal::add).orElse(ZERO);
    }
}

package crypto.inc.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderRequest {
    private OrderType type;
    private String userId;
    private CoinType coinType;
    private BigDecimal orderQuantity;
    private BigDecimal pricePerCoin;

    public Order toOrder() {
        return Order.builder()
                .type(type)
                .userId(userId)
                .coinType(coinType)
                .orderQuantity(orderQuantity)
                .pricePerCoin(pricePerCoin)
                .build();
    }
}

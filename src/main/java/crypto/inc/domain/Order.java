package crypto.inc.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class Order {
    private UUID orderId;
    private OrderType type;
    private String userId;
    private CoinType coinType;
    private BigDecimal orderQuantity;
    private BigDecimal pricePerCoin;
}

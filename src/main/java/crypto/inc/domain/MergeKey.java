package crypto.inc.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MergeKey {
    private BigDecimal price;
    private CoinType coinType;

    public static MergeKey from(OrderSummary summary) {
        return MergeKey.builder().coinType(summary.getCoinType()).price(summary.getPrice()).build();
    }
}

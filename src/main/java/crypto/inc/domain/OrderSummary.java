package crypto.inc.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

import static java.lang.String.format;

@Data
@Builder
public class OrderSummary {
    private CoinType coinType;
    private BigDecimal total;
    private BigDecimal price;

    public static OrderSummary orderSummaryFrom(Order order) {
        return OrderSummary.builder()
                .coinType(order.getCoinType())
                .total(order.getOrderQuantity())
                .price(order.getPricePerCoin())
                .build();
    }

    public static OrderSummary orderSummaryFrom(CoinType coinType, String total, String price) {
        return OrderSummary.builder()
                .coinType(coinType)
                .total(new BigDecimal(total))
                .price(new BigDecimal(price))
                .build();
    }

    @Override
    public String toString() {
        String formattedTotal = total.stripTrailingZeros().toPlainString();
        String formattedPrice = price.stripTrailingZeros().toPlainString();
        return format("%s for £%s", formattedTotal, formattedPrice);
    }
}

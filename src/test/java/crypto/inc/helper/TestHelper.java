package crypto.inc.helper;

import crypto.inc.domain.CoinType;
import crypto.inc.domain.OrderRequest;
import crypto.inc.domain.OrderType;

import java.math.BigDecimal;
import java.util.List;

import static crypto.inc.domain.CoinType.Ethereum;
import static crypto.inc.domain.CoinType.Litecoin;
import static crypto.inc.domain.OrderType.BUY;
import static crypto.inc.domain.OrderType.SELL;
import static java.util.Arrays.asList;

public class TestHelper {
    public static final String USER1 = "user1";
    public static final String USER2 = "user2";
    public static final String USER3 = "user3";
    public static final String USER4 = "user4";

    public OrderRequest mockOrderRequest(OrderType type, String amount, String price, String user, CoinType coinType) {
        return OrderRequest.builder()
                .type(type)
                .coinType(coinType)
                .orderQuantity(new BigDecimal(amount))
                .pricePerCoin(new BigDecimal(price))
                .userId(user)
                .build();
    }

    public List<OrderRequest> mockOrderRequests(OrderType orderType) {
        return asList(
                mockOrderRequest(orderType, "350.1", "13.6", USER1, Ethereum),
                mockOrderRequest(orderType, "50.5", "14", USER2, Ethereum),
                mockOrderRequest(orderType, "441.8", "13.9", USER3, Ethereum),
                mockOrderRequest(orderType, "3.5", "13.6", USER4, Ethereum)
        );
    }

    public List<OrderRequest> mixOfBuyingAndSellingDifferentCoins() {
        return asList(
                mockOrderRequest(BUY, "350.1", "13.6", USER1, Ethereum),
                mockOrderRequest(SELL, "50.5", "14", USER2, Ethereum),
                mockOrderRequest(BUY, "1.1", "14", USER3, Litecoin),
                mockOrderRequest(SELL, "441.8", "13.9", USER3, Ethereum),
                mockOrderRequest(BUY, "3.5", "13.6", USER4, Ethereum),
                mockOrderRequest(BUY, "8.9", "14", USER4, Litecoin),
                mockOrderRequest(SELL, "56.1", "14", USER2, Ethereum),
                mockOrderRequest(BUY, "12.1", "14", USER1, Ethereum),
                mockOrderRequest(SELL, "1.1", "13.1", USER2, Litecoin)
        );
    }

    public List<OrderRequest> overTenOrders(OrderType orderType) {
        return asList(
                mockOrderRequest(orderType, "350.1", "1", USER1, Ethereum),
                mockOrderRequest(orderType, "50.5", "2", USER2, Ethereum),
                mockOrderRequest(orderType, "441.8", "3", USER3, Ethereum),
                mockOrderRequest(orderType, "7.51", "4", USER4, Ethereum),
                mockOrderRequest(orderType, "3.5", "5", USER1, Ethereum),
                mockOrderRequest(orderType, "45.1", "6", USER2, Ethereum),
                mockOrderRequest(orderType, "5.7", "7", USER4, Ethereum),
                mockOrderRequest(orderType, "9.5", "8", USER3, Ethereum),
                mockOrderRequest(orderType, "3.4", "9", USER4, Ethereum),
                mockOrderRequest(orderType, "66.1", "10", USER2, Ethereum),
                mockOrderRequest(orderType, "3.4", "11", USER4, Ethereum),
                mockOrderRequest(orderType, "30.3", "12", USER3, Ethereum),
                mockOrderRequest(orderType, "3.89", "13", USER1, Ethereum)
        );
    }
}

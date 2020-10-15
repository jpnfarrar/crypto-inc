package crypto.inc.utility;

import crypto.inc.domain.Order;
import crypto.inc.domain.OrderSummary;
import crypto.inc.domain.OrderType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static crypto.inc.domain.CoinType.Ethereum;
import static crypto.inc.domain.OrderType.BUY;
import static crypto.inc.domain.OrderType.SELL;
import static crypto.inc.helper.TestHelper.USER1;
import static java.math.BigDecimal.ONE;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@RunWith(MockitoJUnitRunner.class)
public class OrderMergerTest {
    private OrderMerger underTest;

    @Before
    public void setup() {
        underTest = new OrderMerger();
    }

    @Test
    public void mergeOrdersWithTheSamePrice() {
        List<OrderSummary> merged = underTest.merge(asList(mockOrder("1", SELL), mockOrder("1", SELL)));
        assertThat(merged, hasSize(1));
        assertThat(merged.iterator().next().getTotal(), is(new BigDecimal(2)));
    }

    @Test
    public void doesNotMergeOrdersWithDifferentPrices() {
        List<OrderSummary> merged = underTest.merge(asList(mockOrder("1", SELL), mockOrder("2", SELL)));
        assertThat(merged, hasSize(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void doesNotSupportMergingDifferentOrderTypes() {
        underTest.merge(asList(mockOrder("1", SELL), mockOrder("2", BUY)));
    }

    private Order mockOrder(String price, OrderType orderType) {
        return Order.builder().orderId(randomUUID())
                .coinType(Ethereum)
                .type(orderType)
                .orderQuantity(ONE)
                .pricePerCoin(new BigDecimal(price))
                .userId(USER1)
                .build();
    }
}
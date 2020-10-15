package crypto.inc;

import crypto.inc.domain.Order;
import crypto.inc.domain.OrderRequest;
import crypto.inc.domain.OrderSummary;
import crypto.inc.helper.TestHelper;
import crypto.inc.utility.OrderMerger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static crypto.inc.domain.CoinType.Ethereum;
import static crypto.inc.domain.CoinType.Litecoin;
import static crypto.inc.domain.OrderSummary.orderSummaryFrom;
import static crypto.inc.domain.OrderType.BUY;
import static crypto.inc.domain.OrderType.SELL;
import static crypto.inc.helper.TestHelper.USER1;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

@RunWith(MockitoJUnitRunner.class)
public class LiveOrderBoardTest {
    private final TestHelper testHelper = new TestHelper();
    private InMemoryLiveOrderBoard underTest;

    @Before
    public void setup() {
        underTest = new InMemoryLiveOrderBoard(new OrderMerger());
    }

    @Test
    public void placingAnOrderAddsToListOfOrders() {
        underTest.placeOrder(testHelper.mockOrderRequest(SELL, "350.1", "13.6", USER1, Ethereum));
        Order order = underTest.orders().iterator().next();

        assertThat(order.getType(), is(SELL));
        assertThat(order.getCoinType(), is(Ethereum));
        assertThat(order.getOrderQuantity(), is(new BigDecimal("350.1")));
        assertThat(order.getPricePerCoin(), is(new BigDecimal("13.6")));
        assertThat(order.getUserId(), is(USER1));
    }

    @Test
    public void cancellingAnOrderRemovesIt() {
        UUID orderId = underTest.placeOrder(testHelper.mockOrderRequest(SELL, "350.1", "13.6", USER1, Ethereum));
        underTest.cancelOrder(orderId);
        assertThat(underTest.orders(), empty());
    }

    @Test
    public void sellPricesAreMergedAndOrderedByLowestPriceFirst() {
        for (OrderRequest order : testHelper.mockOrderRequests(SELL)) {
            underTest.placeOrder(order);
        }

        List<OrderSummary> expected = new ArrayList<>();
        expected.add(orderSummaryFrom(Ethereum, "353.6", "13.6"));
        expected.add(orderSummaryFrom(Ethereum, "441.8", "13.9"));
        expected.add(orderSummaryFrom(Ethereum, "50.5", "14"));
        assertThat(underTest.top10Orders(SELL), is(expected));
    }

    @Test
    public void buyPricesAreMergedAndOrderedByHighestPriceFirst() {
        for (OrderRequest order : testHelper.mockOrderRequests(BUY)) {
            underTest.placeOrder(order);
        }

        List<OrderSummary> expected = new ArrayList<>();
        expected.add(orderSummaryFrom(Ethereum, "50.5", "14"));
        expected.add(orderSummaryFrom(Ethereum, "441.8", "13.9"));
        expected.add(orderSummaryFrom(Ethereum, "353.6", "13.6"));
        assertThat(underTest.top10Orders(BUY), is(expected));
    }

    @Test
    public void handleMixOfSellAndBuyOrders() {
        for (OrderRequest order : testHelper.mixOfBuyingAndSellingDifferentCoins()) {
            underTest.placeOrder(order);
        }
        List<OrderSummary> expectedBuys = new ArrayList<>();
        expectedBuys.add(orderSummaryFrom(Ethereum, "12.1", "14"));
        expectedBuys.add(orderSummaryFrom(Litecoin, "10.0", "14"));
        expectedBuys.add(orderSummaryFrom(Ethereum, "353.6", "13.6"));
        assertThat(underTest.top10Orders(BUY), is(expectedBuys));

        List<OrderSummary> expectedSells = new ArrayList<>();
        expectedSells.add(orderSummaryFrom(Litecoin, "1.1", "13.1"));
        expectedSells.add(orderSummaryFrom(Ethereum, "441.8", "13.9"));
        expectedSells.add(orderSummaryFrom(Ethereum, "106.6", "14"));
        assertThat(underTest.top10Orders(SELL), is(expectedSells));
    }

    @Test
    public void onlyReturnTopTen() {
        for (OrderRequest order : testHelper.overTenOrders(BUY)) {
            underTest.placeOrder(order);
        }
        assertThat(underTest.top10Orders(BUY), hasSize(10));
    }
}
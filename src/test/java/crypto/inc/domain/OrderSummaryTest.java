package crypto.inc.domain;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class OrderSummaryTest {

    @Test
    public void printCorrectFormat() {
        BigDecimal total = new BigDecimal("1.0001000");
        BigDecimal price = new BigDecimal("1.00");
        OrderSummary summary = OrderSummary.builder().total(total).price(price).build();

        assertThat(summary.toString(), is("1.0001 for £1"));
    }
}
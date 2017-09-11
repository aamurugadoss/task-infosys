package com.src.cashman.util;

import com.src.cashman.util.Denomination;

import java.math.BigDecimal;

/**
 * @author Murugadoss
 * It holds the currency denomination
 */
public enum CurrencyDenomination implements Denomination {
    HUNDRED(100),
    FIFTY(50),
    TWENTY(20),
    TEN(10),
    FIVE(5);

    private BigDecimal value;

    CurrencyDenomination(int value) {
        this.value = new BigDecimal(value);
    }

    public BigDecimal getValue() {
        return value;
    }
}

package com.src.cashman.util;

import com.src.cashman.util.Denomination;

import java.math.BigDecimal;

/**
 * @author Murugadoss
 * It holds the coin denomination
 */
public enum CoinDenomination implements Denomination {
    ONE(1),
    TWO(2),    
    TWENTYCENT(0.20),
    FIFTYCENT(0.50),
    TENCENT(0.10),
	FIVECENT(0.05);

    private BigDecimal value;

    CoinDenomination(double value) {
        this.value = BigDecimal.valueOf(value);
    }

    public BigDecimal getValue() {
        return value;
    }
}

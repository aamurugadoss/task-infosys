package com.src.cashman;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Assert;
import org.junit.Test;

import com.src.cashman.util.CoinDenomination;

public class CoinDenominationTest {

	    @Test
	    public void getValueTest() {
	        Assert.assertTrue(new BigDecimal(0.1).setScale(1, RoundingMode.DOWN).compareTo(CoinDenomination.TENCENT.getValue())==0);
	        Assert.assertEquals(new BigDecimal(0.1).setScale(1, RoundingMode.DOWN), CoinDenomination.TENCENT.getValue());
	    }
}

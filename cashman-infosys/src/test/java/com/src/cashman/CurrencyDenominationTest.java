package com.src.cashman;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import com.src.cashman.util.CurrencyDenomination;

public class CurrencyDenominationTest {

	  @Test
	    public void getValueTest() {
	        Assert.assertEquals(new BigDecimal(20), CurrencyDenomination.TWENTY.getValue());
	        Assert.assertEquals(new BigDecimal(50), CurrencyDenomination.FIFTY.getValue());
	    }
}

package com.src.cashman;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.src.cashman.exception.CurrencyCombinationException;
import com.src.cashman.exception.InsufficientFundsException;
import com.src.cashman.service.CashmanServiceImpl;
import com.src.cashman.util.CurrencyDenomination;
import com.src.cashman.util.CoinDenomination;

public class CashmanServiceTest {
	private CashmanServiceImpl cashmanService;

	@Test
	public void addDenominationTest() {
		CashmanServiceImpl cashmanService = new CashmanServiceImpl();
		cashmanService.addDenomination(CurrencyDenomination.FIVE, 4);
		Assert.assertEquals(4, cashmanService.getCount(CurrencyDenomination.FIVE));
		cashmanService.addDenomination(CurrencyDenomination.HUNDRED, 10);
		Assert.assertEquals(10, cashmanService.getCount(CurrencyDenomination.HUNDRED));
	}

	@Test
	public void cashWithDrawalTest() throws CurrencyCombinationException, InsufficientFundsException {
		CashmanServiceImpl cashmanService = new CashmanServiceImpl();
		cashmanService.addDenomination(CurrencyDenomination.FIFTY, 2);
		cashmanService.addDenomination(CoinDenomination.FIVECENT, 2);
		cashmanService.cashWithdrawal(BigDecimal.valueOf(100));
		Assert.assertEquals(0, cashmanService.getCount(CurrencyDenomination.FIFTY));
		cashmanService.cashWithdrawal(BigDecimal.valueOf(0.05));
		Assert.assertEquals(1, cashmanService.getCount(CoinDenomination.FIVECENT));
		// multiple coin denomination withdrawal
		cashmanService.addDenomination(CoinDenomination.FIFTYCENT, 2);
		cashmanService.addDenomination(CoinDenomination.TWENTYCENT, 2);
		cashmanService.addDenomination(CoinDenomination.TENCENT, 2);
		cashmanService.addDenomination(CoinDenomination.FIVECENT, 3);
		cashmanService.addDenomination(CoinDenomination.ONE, 2);
		cashmanService.addDenomination(CoinDenomination.TWO, 2);
		cashmanService.cashWithdrawal(BigDecimal.valueOf(0.05));
		cashmanService.cashWithdrawal(BigDecimal.valueOf(0.25));
		Assert.assertEquals(2, cashmanService.getCount(CoinDenomination.FIVECENT));
		Assert.assertEquals(1, cashmanService.getCount(CoinDenomination.TWENTYCENT));
		cashmanService.cashWithdrawal(BigDecimal.valueOf(1.10));
		cashmanService.cashWithdrawal(BigDecimal.valueOf(3.0));
		Assert.assertEquals(1, cashmanService.getCount(CoinDenomination.TENCENT));
		Assert.assertEquals(0, cashmanService.getCount(CoinDenomination.ONE));
		Assert.assertEquals(1, cashmanService.getCount(CoinDenomination.TWO));

		// multiple currency denomination withdrawal
		cashmanService.addDenomination(CurrencyDenomination.FIFTY, 2);
		cashmanService.addDenomination(CurrencyDenomination.HUNDRED, 2);
		cashmanService.addDenomination(CurrencyDenomination.TEN, 2);
		cashmanService.addDenomination(CurrencyDenomination.TWENTY, 2);
		cashmanService.addDenomination(CurrencyDenomination.FIVE, 2);
		cashmanService.cashWithdrawal(BigDecimal.valueOf(150));
		cashmanService.cashWithdrawal(BigDecimal.valueOf(30));
		Assert.assertEquals(1, cashmanService.getCount(CurrencyDenomination.HUNDRED));
		Assert.assertEquals(1, cashmanService.getCount(CurrencyDenomination.FIFTY));
		Assert.assertEquals(1, cashmanService.getCount(CurrencyDenomination.TEN));
		Assert.assertEquals(1, cashmanService.getCount(CurrencyDenomination.TWENTY));
		cashmanService.cashWithdrawal(BigDecimal.valueOf(25));
		Assert.assertEquals(0, cashmanService.getCount(CurrencyDenomination.TWENTY));
		Assert.assertEquals(1, cashmanService.getCount(CurrencyDenomination.FIVE));
	}
	
	public void getTotalTest() throws Exception {
		CashmanServiceImpl cashmanService = new CashmanServiceImpl();
		cashmanService.addDenomination(CurrencyDenomination.HUNDRED, 2);
		cashmanService.addDenomination(CurrencyDenomination.FIFTY, 2);
		cashmanService.addDenomination(CoinDenomination.ONE, 1);
		cashmanService.addDenomination(CoinDenomination.TWO, 1);
		Assert.assertEquals(153, cashmanService.getTotal());
	}

	@Test(expected = InsufficientFundsException.class)
	public void insufficientFundsExceptionTest() throws Exception {
		CashmanServiceImpl cashmanService = new CashmanServiceImpl();
		cashmanService.addDenomination(CurrencyDenomination.HUNDRED, 1);
		cashmanService.cashWithdrawal(BigDecimal.valueOf(200));
	}

	@Test(expected = CurrencyCombinationException.class)
	public void currencyCombinationExceptionTest() throws Exception {
		CashmanServiceImpl cashmanService = new CashmanServiceImpl();
		cashmanService.addDenomination(CoinDenomination.FIFTYCENT, 1);
		cashmanService.addDenomination(CoinDenomination.TWENTYCENT, 1);
		cashmanService.addDenomination(CoinDenomination.TENCENT, 1);
		cashmanService.addDenomination(CoinDenomination.FIVECENT, 1);
		cashmanService.addDenomination(CoinDenomination.ONE, 1);
		cashmanService.addDenomination(CoinDenomination.TWO, 1);
		cashmanService.addDenomination(CurrencyDenomination.FIFTY, 1);
		cashmanService.addDenomination(CurrencyDenomination.HUNDRED, 1);
		cashmanService.addDenomination(CurrencyDenomination.TEN, 1);
		cashmanService.addDenomination(CurrencyDenomination.TWENTY, 1);
		cashmanService.addDenomination(CurrencyDenomination.FIVE, 1);
		cashmanService.cashWithdrawal(BigDecimal.valueOf(50));
		cashmanService.cashWithdrawal(BigDecimal.valueOf(70));
	}
}

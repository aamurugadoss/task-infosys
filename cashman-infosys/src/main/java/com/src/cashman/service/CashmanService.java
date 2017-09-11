package com.src.cashman.service;

import java.math.BigDecimal;
import java.util.Map;

import com.src.cashman.exception.CurrencyCombinationException;
import com.src.cashman.exception.InsufficientFundsException;
import com.src.cashman.util.Denomination;

public interface CashmanService {
	void addDenomination(Denomination denomination, int amount);

	void cashWithdrawal(BigDecimal withdrawAmount) throws CurrencyCombinationException, InsufficientFundsException;

	int getCount(Denomination denomination);

	void getReport();

	BigDecimal getTotal(Map<Denomination, Integer> count);

	BigDecimal getTotal();
	
	public Map<Denomination, Integer> getDenominationCount();
}

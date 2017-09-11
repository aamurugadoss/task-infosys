package com.src.cashman.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.src.cashman.exception.CurrencyCombinationException;
import com.src.cashman.exception.InsufficientFundsException;
import com.src.cashman.util.Denomination;

/**
 * @author Murugadoss
 * CashmanService is responsible to add currency and withdrawal
 */
public class CashmanServiceImpl implements CashmanService{

	private Map<Denomination, Integer> denominationCount;

	public Map<Denomination, Integer> getDenominationCount() {
		return denominationCount;
	}

	public CashmanServiceImpl() {
		this.denominationCount = new HashMap<>();
	}

	public void addDenomination(Denomination denomination, int amount) {
		if (denominationCount.containsKey(denomination)) {
			Integer total = denominationCount.get(denomination) + amount;
			denominationCount.put(denomination, total);
		} else {
			denominationCount.put(denomination, amount);
		}
	}

	public void cashWithdrawal(BigDecimal withdrawAmount)
			throws CurrencyCombinationException, InsufficientFundsException {
		Map<Denomination, Integer> withDrawMap = new HashMap<>();
		List<Denomination> denominations = denominationCount.entrySet().stream().filter(entry -> entry.getValue() > 0)
				.map(Map.Entry::getKey).collect(Collectors.toList());
		denominations.sort(Comparator.comparing(Denomination::getValue).reversed());
		for (int place = 0; place < denominations.size(); place++) {
			withDrawMap = manipulateDenominations(place, denominations, withdrawAmount, withDrawMap);
			if (hasSufficientFund(withDrawMap)) {
				break;
			}
		}
		if (withdrawAmount.compareTo(getTotal(denominationCount)) > 0) {
			throw new InsufficientFundsException("Insufficient funds to withdraw...");
		}
		if (!hasSufficientFund(withDrawMap)) {
			throw new CurrencyCombinationException("Insufficient denominations to withdraw...");
		}
		deduct(withDrawMap);
	}

	private Map<Denomination, Integer> manipulateDenominations(int place, List<Denomination> denominations,
			BigDecimal withdrawAmount, Map<Denomination, Integer> withDrawMap) {
		int totalCount = 0;
		for (Map.Entry<Denomination, Integer> entry : denominationCount.entrySet()) {
			totalCount += getCount(entry.getKey());
		}
		for (int version = 0; version < totalCount; version++) {
			withDrawMap.clear();
			double amount = withdrawAmount.doubleValue();
			int vesionTemp = version;
			for (int i = place; i < denominations.size(); i++) {
				double value = denominations.get(i).getValue().doubleValue();
				if (value <= amount) {
					double count = (amount / value) - vesionTemp;
					int c = (int) Math.floor(count);
					withDrawMap.put(denominations.get(i), c);
					amount = BigDecimal.valueOf(amount)
							.subtract(BigDecimal.valueOf(c).multiply(BigDecimal.valueOf(value))).doubleValue();
					vesionTemp = 0;
					if (getTotal(withDrawMap).compareTo(withdrawAmount) == 0) {
						return withDrawMap;
					}
				}
			}
		}
		return withDrawMap;
	}

	private boolean hasSufficientFund(Map<Denomination, Integer> withDrawMap) {
		for (Map.Entry<Denomination, Integer> entry : withDrawMap.entrySet()) {
			if (!denominationCount.containsKey(entry.getKey()))
				return false;
			if (denominationCount.get(entry.getKey()) < entry.getValue())
				return false;
		}
		return true;
	}

	private void deduct(Map<Denomination, Integer> withDrawMap) {
		withDrawMap.entrySet().forEach(entry -> {
			Integer deduction = denominationCount.get(entry.getKey()) - entry.getValue();
			denominationCount.put(entry.getKey(), deduction);
		});
	}

	public int getCount(Denomination denomination) {
		int count = 0;
		if (denominationCount.containsKey(denomination)) {
			count = denominationCount.get(denomination);
		}
		return count;
	}

	public void getReport() {
		denominationCount.forEach((k, v) -> System.out.printf("%s : %d\n", k.toString(), getCount(k)));
	}

	public BigDecimal getTotal(Map<Denomination, Integer> count) {
		BigDecimal sum = BigDecimal.ZERO;
		for (Map.Entry<Denomination, Integer> entry : count.entrySet()) {
			sum = sum.add(entry.getKey().getValue().multiply(new BigDecimal(entry.getValue().intValue())));
		}
		return sum;
	}

	public BigDecimal getTotal() {
		return getTotal(denominationCount);
	}
}

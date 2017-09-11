package com.src.cashman;

import static com.src.cashman.util.AppConstant.AUD;
import static com.src.cashman.util.AppConstant.BALANCE;
import static com.src.cashman.util.AppConstant.HELP;
import static com.src.cashman.util.AppConstant.REPORT;
import static com.src.cashman.util.AppConstant.WITHDRAW;
import static com.src.cashman.util.AppConstant.ZERO;

import java.math.BigDecimal;
import java.util.Scanner;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.src.cashman.exception.CurrencyCombinationException;
import com.src.cashman.exception.InsufficientFundsException;
import com.src.cashman.exception.InvalidExpressionException;
import com.src.cashman.service.CashmanService;
import com.src.cashman.service.CashmanServiceImpl;
import com.src.cashman.util.CoinDenomination;
import com.src.cashman.util.CurrencyDenomination;

@SpringBootApplication
public class CashmanInfosysApplication {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		// CashmanController cashController = new CashmanController();
		CashmanService cashmanService = new CashmanServiceImpl();
		System.out.println("**********Start Cashman App************");
		int count;
		for (CurrencyDenomination denomination : CurrencyDenomination.values()) {
			System.out.printf("Enter the number of %s :", AUD + " " + denomination.getValue());
			count = input.nextInt();
			cashmanService.addDenomination(denomination, count);
		}
		for (CoinDenomination denomination : CoinDenomination.values()) {
			System.out.printf("Enter the number of %s :", AUD + " " + denomination.getValue());
			count = input.nextInt();
			cashmanService.addDenomination(denomination, count);
		}
		System.out
				.println("To operate, please enter 'withdraw [amount]' or 'help' or 'report' or 'balance'");
		// System.out.println(service.isReadyToProcess());
		System.out.print("> ");

		while (input.hasNext()) {

			String expression = input.nextLine().trim();

			try {

				if (ZERO == expression.length()) {

					continue;
				} else if (cashmanService.getDenominationCount().isEmpty()) {

					throw new IllegalStateException("There is no cash to withdraw. Must be initialized");
				} else if (expression.startsWith(WITHDRAW)) {

					expression = expression.replace("withdraw ", "");
					BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(expression));
					if (amount.compareTo(BigDecimal.ZERO) <= 0) {
						throw new InvalidExpressionException("Cannot withdraw a negative or zero amount");
					} else {
						cashmanService.cashWithdrawal(amount);
						System.out.printf("You have withdrawn %s \n", AUD + " " + amount);
					}
				} else if (expression.equals(REPORT)) {

					cashmanService.getReport();
				} else if (expression.equals(BALANCE)) {

					System.out.printf("Total available balance is %s \n",
							AUD + " " + cashmanService.getTotal(cashmanService.getDenominationCount()));
				} else if (expression.equals(HELP)) {

					System.out.println("please enter 'withdraw [amount]' or 'help' or 'report'\n");
				} else {
					throw new InvalidExpressionException("Please enter valid expression");
				}
			} catch (NumberFormatException | InsufficientFundsException | CurrencyCombinationException
					| InvalidExpressionException e) {
				System.out.println(e.getMessage());

			}
		}
	}
}

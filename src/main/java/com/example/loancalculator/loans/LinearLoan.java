package com.example.loancalculator.loans;

import java.util.List;

public class LinearLoan extends Loan {

    public LinearLoan(double amount, int years, int months, double interestRate) {
        super(amount, years, months, interestRate);
    }

    @Override
    public List<LoanPayment> GetMonthlyPayments() {
        return List.of();
    }
}

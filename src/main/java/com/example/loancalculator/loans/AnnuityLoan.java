package com.example.loancalculator.loans;

import java.util.List;

public class AnnuityLoan extends Loan {
    public AnnuityLoan(double amount, int years, int months, double interestRate) {
        super(amount, years, months, interestRate);
    }

    @Override
    public List<LoanPayment> GetMonthlyPayments() {
        return List.of();
    }

}

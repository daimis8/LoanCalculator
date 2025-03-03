package com.example.loancalculator.loans;

import java.util.List;

public abstract class Loan {
    protected double loanAmount;
    protected int years;
    protected int months;
    protected double interestRate;

    public Loan(double amount, int years, int months, double interestRate) {
        this.loanAmount = amount;
        this.years = years;
        this.months = months;
        this.interestRate = interestRate;
    }

    public abstract List<LoanPayment> GetMonthlyPayments();

}

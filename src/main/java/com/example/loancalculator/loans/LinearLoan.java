package com.example.loancalculator.loans;

import java.util.ArrayList;
import java.util.List;

public class LinearLoan extends Loan {

    public LinearLoan(double loanAmount, int years, int months, double interestRate) {
        super(loanAmount, years, months, interestRate);
    }

    @Override
    public List<LoanPayment> GetMonthlyPayments() {
        double remainingAmount = loanAmount;
        List<LoanPayment> payments = new ArrayList<>();
        int totalMonths = months + (years * 12);
        double monthlyInterestRate = (interestRate / 100) / 12;
        double fixedPrincipal = remainingAmount / totalMonths;

        int currentYear = 1;
        int currentMonth = 1;

        for (int i = 1; i <= totalMonths; i++) {
            double interest = remainingAmount * monthlyInterestRate;
            double monthlyPayment = fixedPrincipal + interest;
            remainingAmount -= fixedPrincipal;

            payments.add(new LoanPayment(currentYear, currentMonth, monthlyPayment, interest, fixedPrincipal, remainingAmount));

            currentMonth++;
            if (currentMonth > 12) {
                currentMonth = 1;
                currentYear++;
            }
        }

        return payments;
    }
}

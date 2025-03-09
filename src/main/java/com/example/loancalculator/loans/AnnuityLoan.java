package com.example.loancalculator.loans;

import java.util.ArrayList;
import java.util.List;

public class AnnuityLoan extends Loan {
    public AnnuityLoan(double amount, int years, int months, double interestRate) {
        super(amount, years, months, interestRate);
    }

    // In AnnuityLoan.java
    @Override
    public List<LoanPayment> GetMonthlyPayments() {
        List<LoanPayment> payments = new ArrayList<>();

        // Calculate total months
        int totalMonths = (years * 12) + months;

        // Monthly interest rate (annual rate / 12)
        double monthlyRate = interestRate / 100.0 / 12.0;

        // Calculate monthly payment amount using the annuity formula
        double monthlyPayment;
        if (monthlyRate == 0) {
            // Simple division if there's no interest
            monthlyPayment = loanAmount / totalMonths;
        } else {
            // Standard formula for calculating monthly loan payment
            monthlyPayment = loanAmount *
                    (monthlyRate * Math.pow(1 + monthlyRate, totalMonths)) /
                    (Math.pow(1 + monthlyRate, totalMonths) - 1);
        }

        // Generate payments schedule
        double remainingBalance = loanAmount;

        for (int i = 0; i < totalMonths; i++) {
            // Calculate interest for this payment
            double interestPayment = remainingBalance * monthlyRate;

            // Calculate principal for this payment
            double principalPayment = monthlyPayment - interestPayment;

            // For the last payment, adjust to clear any remaining balance due to rounding
            if (i == totalMonths - 1) {
                principalPayment = remainingBalance;
                monthlyPayment = principalPayment + interestPayment;
            }

            // Update remaining balance
            remainingBalance -= principalPayment;
            if (remainingBalance < 0.01) {
                remainingBalance = 0;  // Fix potential floating-point precision issues
            }

            // Determine year and month
            int currentYear = (i / 12) + 1;
            int currentMonth = (i % 12) + 1;

            // Create payment object and add to list
            payments.add(new LoanPayment(
                    currentYear,
                    currentMonth,
                    monthlyPayment,
                    interestPayment,
                    principalPayment,
                    remainingBalance
            ));
        }

        return payments;
    }
}
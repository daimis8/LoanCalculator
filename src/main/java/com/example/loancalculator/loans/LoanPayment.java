package com.example.loancalculator.loans;

public class LoanPayment {
    int year;
    int month;
    double monthlyPayment;
    double interest;
    double credit;
    double balance;

    public LoanPayment(int year, int month, double monthlyPayment, double interest, double credit, double balance) {
        this.year = year;
        this.month = month;
        this.monthlyPayment = monthlyPayment;
        this.interest = interest;
        this.credit = credit;
        this.balance = balance;
    }

    public LoanPayment(LoanPayment payment) {
        this.year = payment.year;
        this.month = payment.month;
        this.monthlyPayment = payment.monthlyPayment;
        this.interest = payment.interest;
        this.credit = payment.credit;
        this.balance = payment.balance;
    }

    public double getLoanBalance() {
        return Math.round(balance * 100.0) / 100.0;
    }

    public double getMonthlyPayment() {
        return Math.round(monthlyPayment * 100.0) / 100.0;
    }

    public double getInterest() {
        return Math.round(interest * 100.0) / 100.0;
    }

    public double getCredit() {
        return Math.round(credit * 100.0) / 100.0;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public void setLoanBalance(double loanBalance) {
        this.balance = loanBalance;
    }
}

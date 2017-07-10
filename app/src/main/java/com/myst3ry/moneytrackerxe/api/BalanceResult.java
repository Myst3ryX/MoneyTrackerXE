package com.myst3ry.moneytrackerxe.api;


public class BalanceResult extends Result { //result for checking balance operation

    private long totalExpenses;
    private long totalIncome;

    public long getTotalExpenses() {
        return totalExpenses;
    }

    public long getTotalIncomes() {
        return totalIncome;
    }
}

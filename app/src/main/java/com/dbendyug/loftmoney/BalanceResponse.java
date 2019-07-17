package com.dbendyug.loftmoney;

import com.google.gson.annotations.SerializedName;

class BalanceResponse {

    @SerializedName("total_expenses")
    private int totalExpense;

    @SerializedName("total_income")
    private int totalIncome;


    public int getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(int totalExpense) {
        this.totalExpense = totalExpense;
    }

    public int getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(int totalIncome) {
        this.totalIncome = totalIncome;
    }
}

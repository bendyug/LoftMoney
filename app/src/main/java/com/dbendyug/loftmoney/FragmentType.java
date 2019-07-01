package com.dbendyug.loftmoney;

public enum FragmentType {

    expense(R.color.expense_price_color), income(R.color.income_price_color);

    private int priceColor;

    FragmentType(int priceColor) {
        this.priceColor = priceColor;
    }

    public int getPriceColor() {
        return priceColor;
    }
}

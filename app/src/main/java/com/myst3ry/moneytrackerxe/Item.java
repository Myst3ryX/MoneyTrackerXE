package com.myst3ry.moneytrackerxe;

class Item {

    static final String EXP_TYPE = "expenses";
    static final String INC_TYPE = "incomes";
    private final String article;
    private final int amount;

    Item(final String article, final int amount) {
        this.article = article;
        this.amount = amount;
    }

    String getArticle() {
        return article;
    }

    int getAmount() {
        return amount;
    }
}

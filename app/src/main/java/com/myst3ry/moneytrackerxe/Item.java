package com.myst3ry.moneytrackerxe;

class Item {

    private final String article;
    private final int amount;

    Item(final String name, final int amount) {
        this.article = name;
        this.amount = amount;
    }

    String getArticle() {
        return article;
    }

    int getAmount() {
        return amount;
    }
}

package com.myst3ry.moneytrackerxe;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Item implements Serializable {

    static final String EXP_TYPE = "expense";
    static final String INC_TYPE = "income";

    private int id;
    private Date date;

    @SerializedName("name")
    private final String article;

    @SerializedName("price")
    private final int amount;

    private final String type;

    Item(final String article, final int amount, final String type) {
        this.article = article;
        this.amount = amount;
        this.type = type;
    }

    String getArticle() {
        return article;
    }

    int getAmount() {
        return amount;
    }

    String getType() {
        return type;
    }

    int getId() {
        return id;
    }

    Date getDate() {
        return date;
    }

    void setId(int id) {
        this.id = id;
    }
}

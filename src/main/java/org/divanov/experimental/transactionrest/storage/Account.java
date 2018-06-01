package org.divanov.experimental.transactionrest.storage;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Simplest representation of account (suggest it have currency set somewhere outside etc.)
 *
 * User: Denis_Ivanov
 * Date: 15.05.2018
 * Time: 11:39
 */
public class Account {
    public String id;
    public Double amount;

    public Account() {
    }

    public Account(String id, Double amount) {
        this.id = id;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

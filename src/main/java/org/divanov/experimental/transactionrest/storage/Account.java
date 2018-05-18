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
    public AtomicLong atomicAmountVariant;
}

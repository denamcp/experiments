package org.divanov.experimental.transactionrest.storage;

/**
 * User: Denis_Ivanov
 * Date: 16.05.2018
 * Time: 12:06
 */
public interface AccountStorage {
    Account getAccount(String id);
    Account putAccount(final Account account);
    Account createAccount(String id, double initialAmount);
}

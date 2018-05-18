package org.divanov.experimental.transactionrest.storage;

import java.util.concurrent.ConcurrentHashMap;

/**
 * User: Denis_Ivanov
 * Date: 15.05.2018
 * Time: 11:42
 */
public class AccountsMemoryStorage implements AccountStorage {

    private ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Account getAccount(final String id) {
        return accounts.get(id);
    }
}

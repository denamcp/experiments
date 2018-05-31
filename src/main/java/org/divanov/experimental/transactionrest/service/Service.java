package org.divanov.experimental.transactionrest.service;

import org.divanov.experimental.transactionrest.storage.Account;
import org.divanov.experimental.transactionrest.storage.AccountStorage;

import java.util.Comparator;

/**
 * User: Denis_Ivanov
 * Date: 16.05.2018
 * Time: 12:04
 */
public class Service {

    final AccountStorage accountsStorage;

    public Service(AccountStorage accountsStorage) {
        this.accountsStorage = accountsStorage;
    }

    public Account getAccount(final String id) {
        return accountsStorage.getAccount(id);
    }

    /**
     * Variant with same-ordered synchronization by sorted IDs.
     * Should be stable, but have extra blocking that we could possibly avoid in other approaches.
     *
     * @param idFrom         id of account to withdraw money
     * @param idTo           id of account to put money
     * @param transferAmount amount in single currency
     * @return is ok
     */
    public TransferResult transferSyncAccount(final String idFrom, final String idTo, final Double transferAmount) {
        TransferResult ok = TransferResult.FAIL;
        final Account accountFrom = accountsStorage.getAccount(idFrom);
        final Account accountTo = accountsStorage.getAccount(idTo);
        if (null != accountFrom && null != accountTo) {
            final Comparator<String> idOrderComparator = Comparator.naturalOrder();
            final int compare = idOrderComparator.compare(idFrom, idTo);
            if (compare > 0) {
                synchronized (accountFrom) {
                    synchronized (accountTo) {
                        ok = transfer(accountFrom, accountTo, transferAmount);
                    }
                }
            } else if (compare < 0) {
                synchronized (accountTo) {
                    synchronized (accountFrom) {
                        ok = transfer(accountFrom, accountTo, transferAmount);
                    }
                }
            }
        }
        return ok;
    }

    private TransferResult transfer(final Account accountFrom, final Account accountTo, final Double transferAmount) {
        TransferResult result = TransferResult.FAIL;
        if (accountFrom.amount > transferAmount) {
            accountFrom.amount -= transferAmount;
            accountTo.amount += transferAmount;
            result = TransferResult.OK;
        } else {
            result = TransferResult.NOT_ENOUGH;
        }
        return result;
    }

}

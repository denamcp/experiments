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
    public boolean transferSyncAccount(final String idFrom, final String idTo, final Double transferAmount) {
        boolean ok = false;
        final Account accountFrom = accountsStorage.getAccount(idFrom);
        final Account accountTo = accountsStorage.getAccount(idTo);
        if (null != accountFrom && null != accountTo) {
            final Comparator<String> idOrderComparator = Comparator.naturalOrder();
            final int compare = idOrderComparator.compare(idFrom, idTo);
            if (compare > 0) {
                synchronized (accountFrom) {
                    synchronized (accountTo) {
                        transfer(accountFrom, accountTo, transferAmount);
                        ok = true;
                    }
                }
            } else if (compare < 0) {
                synchronized (accountTo) {
                    synchronized (accountFrom) {
                        transfer(accountFrom, accountTo, transferAmount);
                        ok = true;
                    }
                }
            }
        }
        return ok;
    }

    private void transfer(final Account accountFrom, final Account accountTo, final Double transferAmount) {
        if (accountFrom.amount > transferAmount) {
            accountFrom.amount -= transferAmount;
            accountTo.amount += transferAmount;
        }

    }

    //TODO implement

    /**
     * Variant with atomic numbers
     *
     * @param idFrom         id of account to withdraw money
     * @param idTo           id of account to put money
     * @param transferAmount amount in single currency
     * @return is ok
     */
    public boolean transferUsingAtomic(final String idFrom, final String idTo, final Double transferAmount) {
        boolean ok = false;
        final Account accountFrom = accountsStorage.getAccount(idFrom);
        final Account accountTo = accountsStorage.getAccount(idTo);
        //TODO concurrency - make more thin synchronization (Atomic)
        if (null != accountFrom && null != accountTo) {
            //accountFrom.atomicAmountVariant.compareAndSet(); //TODO what to use
            ok = true;
        }
        return ok;
    }

}

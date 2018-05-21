package org.divanov.experimental.transactionrest;

import org.divanov.experimental.transactionrest.controller.TransactionRestApi;
import org.divanov.experimental.transactionrest.service.Service;
import org.divanov.experimental.transactionrest.storage.AccountStorage;
import org.divanov.experimental.transactionrest.storage.AccountsMemoryStorage;
import ro.pippo.core.Pippo;

/**
 * User: Denis_Ivanov
 * Date: 14.05.2018
 * Time: 11:58
 */
public class Main {
    public static void main(String[] args) {
        final AccountStorage accountsMemoryStorage = new AccountsMemoryStorage();
        final Service service = new Service(accountsMemoryStorage);
        final TransactionRestApi restApi = new TransactionRestApi(service);
        final Pippo pippo = new Pippo(restApi);
        pippo.start();
    }
}

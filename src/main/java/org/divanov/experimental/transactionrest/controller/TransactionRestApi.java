package org.divanov.experimental.transactionrest.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.divanov.experimental.transactionrest.service.Service;
import org.divanov.experimental.transactionrest.storage.Account;
import org.divanov.experimental.transactionrest.storage.AccountsMemoryStorage;
import ro.pippo.core.Application;
import ro.pippo.core.HttpConstants;
import ro.pippo.gson.GsonEngine;

/**
 * User: Denis_Ivanov
 * Date: 14.05.2018
 * Time: 11:59
 */
public class TransactionRestApi extends Application {
    private static final Logger logger = LogManager.getLogger("TransactionRestApi");
    final Service service;
    final AccountsMemoryStorage memoryStorage = new AccountsMemoryStorage();

    public TransactionRestApi(Service service) {
        this.service = service;
    }

    @Override
    protected void onInit() {

        registerContentTypeEngine(GsonEngine.class);

        GET("/", routeContext -> {
            routeContext.send("Account transfer experimental demo. \n Use /account to operate with accounts," +
                    " /transaction to transfer money.");
        });

        //TODO argument
        GET("/account", routeContext -> {
            final String id = routeContext.getParameter("id").toString();
            if (null != id) {
                final Account account = service.getAccount(id);
                routeContext.json().send(account);
            }
        });

        PUT("/account", routeContext -> {
            final String id = routeContext.getParameter("id").toString();
            if (null != id) {
                final Account account = service.getAccount(id);
                routeContext.json().send(account);
            }
        });

        PUT("/transaction", routeContext -> {
            final String idFrom = routeContext.getParameter("idfrom").toString();
            final String idTo = routeContext.getParameter("idto").toString();
            Double transferAmount = null;
            try {
                transferAmount = routeContext.getParameter("transferamount").toDouble();
            } catch (NumberFormatException nfe) {
                logger.warn("Wrong number format for transfer. {}", nfe);
                //TODO logging
            }
            if (null != idFrom && null != idTo && null != transferAmount) {
                boolean ok = service.transferSyncAccount(idFrom, idTo, transferAmount);
                if (ok) {
                    routeContext.getResponse().ok().send(null);
                }
            }
        });
    }


}

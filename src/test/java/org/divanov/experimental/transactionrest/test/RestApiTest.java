package org.divanov.experimental.transactionrest.test;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.divanov.experimental.transactionrest.controller.TransactionRestApi;
import org.divanov.experimental.transactionrest.service.Service;
import org.divanov.experimental.transactionrest.storage.AccountStorage;
import org.divanov.experimental.transactionrest.storage.AccountsMemoryStorage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ro.pippo.core.Pippo;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * User: Denis_Ivanov
 * Date: 18.05.2018
 * Time: 11:49
 */
public class RestApiTest {

    private static TransactionRestApi restApi;
    private static Pippo pippo;
    private static HttpClient client;
    private static final AccountStorage accountsMemoryStorage = new AccountsMemoryStorage();

    @BeforeAll
    public static void beforeAll() throws Exception {
        client = HttpClientBuilder.create().build();
        accountsMemoryStorage.createAccount("Ivanov", 1000);
        accountsMemoryStorage.createAccount("Petrov", 2000);
        final Service service = new Service(accountsMemoryStorage);
        restApi = new TransactionRestApi(service);
        pippo = new Pippo(restApi);
        pippo.start();
    }

    @Test
    public void testConnection() throws IOException {
        final HttpUriRequest request = new HttpGet("http://localhost:8338/");
        final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGet() throws IOException, URISyntaxException {
        final String endpointName = "account";
        final URIBuilder builder = new URIBuilder("http://localhost:8338/" + endpointName);
        builder.setParameter("id", "Petrov");
        final HttpUriRequest request = new HttpGet(builder.build());
        final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void transfer_singleTransaction_transfersOk() throws IOException, URISyntaxException {
        final String endpointName = TransactionRestApi.TRANSACTION;
        final URIBuilder builder = new URIBuilder("http://localhost:8338/" + endpointName);
        builder.setParameter(TransactionRestApi.IDFROM, "Petrov");
        builder.setParameter(TransactionRestApi.IDTO, "Ivanov");
        builder.setParameter(TransactionRestApi.TRANSFERAMOUNT, "100");
        final HttpUriRequest request = new HttpPost(builder.build());
        final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
        assertEquals(Double.valueOf(1900), accountsMemoryStorage.getAccount("Petrov").amount);
        assertEquals(Double.valueOf(1100), accountsMemoryStorage.getAccount("Ivanov").amount);
    }

    @Test
    public void transfer_toHimself_doNothing() throws IOException, URISyntaxException {
        final String endpointName = TransactionRestApi.TRANSACTION;
        final URIBuilder builder = new URIBuilder("http://localhost:8338/" + endpointName);
        builder.setParameter(TransactionRestApi.IDFROM, "Petrov");
        builder.setParameter(TransactionRestApi.IDTO, "Petrov");
        builder.setParameter(TransactionRestApi.TRANSFERAMOUNT, "100");
        final HttpUriRequest request = new HttpPost(builder.build());
        final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        assertEquals(HttpStatus.SC_ACCEPTED, httpResponse.getStatusLine().getStatusCode());
        assertEquals(Double.valueOf(2000), accountsMemoryStorage.getAccount("Petrov").amount);
        assertEquals(Double.valueOf(1000), accountsMemoryStorage.getAccount("Ivanov").amount);
    }

    @AfterAll
    public static void afterAll() {
        pippo.stop();
    }

}

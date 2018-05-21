package org.divanov.experimental.transactionrest.test;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
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

    @BeforeAll
    public static void beforeAll() throws Exception {
        client = HttpClientBuilder.create().build();
        final AccountStorage accountsMemoryStorage = new AccountsMemoryStorage();
        final Service service = new Service(accountsMemoryStorage);
        restApi = new TransactionRestApi(service);
        pippo = new Pippo(restApi);
        pippo.start();
    }

    @Test
    public void testConnection() throws IOException {
        // Given
        String name = "";
        HttpUriRequest request = new HttpGet("http://localhost:8338/" + name);

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

    }

    @AfterAll
    public static void afterAll() {
        pippo.stop();
    }

}

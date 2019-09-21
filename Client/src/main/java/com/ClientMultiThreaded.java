package com;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;

public class ClientMultiThreaded extends Thread {

    private static final Logger LOGGER = Logger.getLogger(ClientMultiThreaded.class.getName());
    private static final String CLIENT_LOG = "CLIENT LOG:  ";
    private static final int TIME_TO_SLEEP = 2000;
    private final HttpGet httpGet;
    private final CloseableHttpClient httpClient;
    private int clientId;
    private final Random randomGenerator;


    public ClientMultiThreaded(CloseableHttpClient httpClient, HttpGet httpGet, int clientId) {
        this.httpClient = httpClient;
        this.clientId = clientId;
        this.httpGet = httpGet;
        randomGenerator = new Random();
    }

    @Override
    public void run() {
        try {
            LOGGER.info(String.format("%s Thread id %d - Send first GET request.", CLIENT_LOG, clientId));
            executeRequest(httpGet);

            while (true) {
                putThreadToSleep();
                executeRequest(httpGet);
            }

        } catch (Exception e) {
            LOGGER.info(String.format("%s Thread id %d - Exception occurred. Server might be down.", CLIENT_LOG, clientId));
        }
    }

    private void putThreadToSleep() throws InterruptedException {
        int timeToSleep = randomGenerator.nextInt(TIME_TO_SLEEP) + 1;

        LOGGER.info(String.format("%s Thread id %d - Thread went to sleep for %d seconds.", CLIENT_LOG, clientId, timeToSleep / 1000));
        sleep(timeToSleep);
    }

    private void executeRequest(HttpGet httpGet) throws IOException {

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            LOGGER.info(String.format("%s Thread id %d - Sent GET request URL %s..", CLIENT_LOG, clientId, httpGet.getURI()));

            int statusCode = response.getStatusLine().getStatusCode();
            LOGGER.info(String.format("%s Thread id %d - Status received: %d", CLIENT_LOG, clientId, statusCode));
        }
    }
}

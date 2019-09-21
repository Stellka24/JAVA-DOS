package com.services;

import com.ClientMultiThreaded;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.stereotype.Service;

import java.util.Scanner;
import java.util.logging.Logger;

@Service
public class ClientService {

    private static final String CLIENT_LOG = "CLIENT LOG:  ";
    private static final String URL = "http://localhost:8080/?clientId=";
    private static final Logger LOGGER = Logger.getLogger(ClientService.class.getName());

    public ClientService() {

        LOGGER.info(String.format("%s Init Client Service..", CLIENT_LOG));

        int numberOfClientsToRun = getNumberOfClientsToRun();

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        HttpClientBuilder clientBuilder = HttpClients.custom().setConnectionManager(connManager);

        connManager.setMaxTotal(numberOfClientsToRun);
        CloseableHttpClient httpClient = clientBuilder.build();

        generateMultiThreadedClient(httpClient, numberOfClientsToRun);
    }


    private int getNumberOfClientsToRun() {
        LOGGER.info(String.format("%s Get number of clients to run from user..", CLIENT_LOG));

        System.out.println("Please enter the number of clients you would like to run..");

        int numberOfClients = getValidInput();

        LOGGER.info(String.format("%s User entered %d clients to run simultaneously.", CLIENT_LOG, numberOfClients));
        return numberOfClients;
    }

    private int getValidInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        boolean invalidInput = true;
        int numberOfClients = 1;

        while (invalidInput) {
            LOGGER.info(String.format("%s Checking input..", CLIENT_LOG));

            if (isANumber(input)) {
                numberOfClients = Integer.parseInt(input);
                invalidInput = false;

                if (numberOfClients <= 0) {
                    System.out.println("Input may not be negative or zero. Please try again..");
                    input = scanner.next();
                }
            } else {
                System.out.println("Input Should be only a number. Please try again..");
                input = scanner.next();
            }
        }

        return numberOfClients;
    }

    private boolean isANumber(String input) {
        try {
            Integer.parseInt(input);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void generateMultiThreadedClient(CloseableHttpClient httpClient, int numberOfClients) {

        for (int i = 0; i < numberOfClients; i++) {
            String clientUrl = URL + i;
            HttpGet httpGet = new HttpGet(clientUrl);

            ClientMultiThreaded clientMultiThreaded = new ClientMultiThreaded(httpClient, httpGet, i);
            LOGGER.info(String.format("%s Thread Id: %d - A new thread was created.", CLIENT_LOG, i));

            clientMultiThreaded.start();
            LOGGER.info(String.format("%s Thread Id: %d - The thread started..", CLIENT_LOG, i));
        }
    }
}

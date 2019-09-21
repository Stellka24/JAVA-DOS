package com.services;

import com.components.ClientRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.logging.Logger;

@Service
public class ServerService {

    private static final Logger LOGGER = Logger.getLogger(ServerService.class.getName());
    private static final String SERVER_LOG = "SERVER LOG:   ";
    private static final int MAX_NUMBER_OF_ALLOWED_REQUESTS = 5;
    private static final int TIME_FRAME = 5;
    private HashMap<Integer, ClientRequest> clientRequestsMap = new HashMap<>();


    public ResponseEntity handelRequest(int clientId) {
        long currentTimeInSeconds = System.currentTimeMillis() / 1000; // Time in seconds
        ClientRequest clientReq = clientRequestsMap.get(clientId);

        if (clientReq == null) {
            // This is the first request for the client

            LOGGER.info(String.format("%s Client Id: %d - Client sent request for the first time..", SERVER_LOG, clientId));
            return allowClientRequest(clientId, currentTimeInSeconds);
        }

        // Init ClientReq

        clientReq.setLastRequest(currentTimeInSeconds);
        clientReq.setNumOfRequests(clientReq.getNumOfRequests() + 1);
        clientRequestsMap.put(clientId, clientReq);

        boolean numOfRequestIsMoreThanAllowed = clientReq.getNumOfRequests() > MAX_NUMBER_OF_ALLOWED_REQUESTS;
        boolean newRequestWasReceivedWithinTimeFrame = clientReq.getLastRequest() - clientReq.getFirstRequest() < TIME_FRAME;

        if (!numOfRequestIsMoreThanAllowed && newRequestWasReceivedWithinTimeFrame) {

            printOutLog(clientId, HttpStatus.OK, clientReq.getNumOfRequests());
            return new ResponseEntity(HttpStatus.OK);
        }

        if (!newRequestWasReceivedWithinTimeFrame) {
            // The new request was received within new time frame
            LOGGER.info(String.format("%s Client Id: %d - A new request was received within a new time frame - request allowed.",
                    SERVER_LOG,clientId));

            printOutLog(clientId, HttpStatus.OK, clientReq.getNumOfRequests());
            return allowClientRequest(clientId, currentTimeInSeconds);

        }
        if (numOfRequestIsMoreThanAllowed) {
            // Number of received requests is higher than allowed
            LOGGER.info(String.format("%s Client Id: %d - The received request is higher than allowed - request denied.",
                    SERVER_LOG, clientId));

            printOutLog(clientId, HttpStatus.SERVICE_UNAVAILABLE, clientReq.getNumOfRequests());
            return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
        }

        // The default response
        return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
    }

    private void printOutLog(int clientId, HttpStatus httpStatus, int receivedNumberOfRequests) {
        LOGGER.info(String.format("%s Client Id: %d - A new request was received.", SERVER_LOG, clientId));
        LOGGER.info(String.format("%s Client Id: %d - The request status is: %s", SERVER_LOG,clientId, httpStatus));
        LOGGER.info(String.format("%s Client id: %d - Received number of requests %d till now.. ", SERVER_LOG, clientId, receivedNumberOfRequests));
    }

    private ResponseEntity allowClientRequest(int clientId, long currentTimeInSeconds) {
        ClientRequest clientReq;
        clientReq = new ClientRequest(currentTimeInSeconds, currentTimeInSeconds, 1);
        clientRequestsMap.put(clientId, clientReq);

        printOutLog(clientId,HttpStatus.OK, clientReq.getNumOfRequests());
        return new ResponseEntity(HttpStatus.OK);
    }
}

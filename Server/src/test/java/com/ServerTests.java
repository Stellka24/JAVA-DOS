package com;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServerTests {

    @LocalServerPort
    private int port;

    private static final String URI_PARAMETER = "/?clientId=";
    private static final int ALLOWED_NUMBER_OF_REQUESTS = 5;
    private static final int NEW_TIME_FRAME = 5;
    private HttpEntity<String> entity;
    private TestRestTemplate restTemplate;
    private ResponseEntity<String> response;
    private int clientId;


    @Before
    public void init(){
        restTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        entity = new HttpEntity<>(null, headers);
    }

    @Test
    public void receiveFirstRequestFromClient() {
        clientId = 0;

        ResponseEntity<String> response =
                restTemplate.exchange(createURLWithPort(URI_PARAMETER + clientId), HttpMethod.GET, entity, String.class);

        MatcherAssert.assertThat(response.getStatusCode(), IsEqual.equalTo(HttpStatus.OK));
    }

    @Test
    public void receiveAllowedNumberOfRequestsWithinTimeFrame() {
        clientId = 1;
        for (int i = 0; i < ALLOWED_NUMBER_OF_REQUESTS - 1; i++) {
            response = restTemplate.exchange(createURLWithPort(URI_PARAMETER + clientId), HttpMethod.GET, entity, String.class);
        }
        MatcherAssert.assertThat(response.getStatusCode(), IsEqual.equalTo(HttpStatus.OK));
    }

    @Test
    public void receiveMoreThanAllowedNumberOfRequestsWithinTimeFrame() {
        clientId = 2;
        for (int i = 0; i < ALLOWED_NUMBER_OF_REQUESTS + 1; i++) {
            response = restTemplate.exchange(createURLWithPort(URI_PARAMETER + clientId), HttpMethod.GET, entity, String.class);
        }
        MatcherAssert.assertThat(response.getStatusCode(), IsEqual.equalTo(HttpStatus.SERVICE_UNAVAILABLE));
    }

    @Test
    public void resetTimeFrameDuringRequestReceiving() throws InterruptedException {
        clientId = 3;

        for (int i = 0; i < ALLOWED_NUMBER_OF_REQUESTS * 10; i++) {
            restTemplate.exchange(createURLWithPort(URI_PARAMETER + clientId), HttpMethod.GET, entity, String.class);
        }
        TimeUnit.SECONDS.sleep(NEW_TIME_FRAME);

        response = restTemplate.exchange(createURLWithPort(URI_PARAMETER + clientId), HttpMethod.GET, entity, String.class);
        MatcherAssert.assertThat(response.getStatusCode(), IsEqual.equalTo(HttpStatus.OK));
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}

package com.project.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    public void setup() {
        this.baseUrl = "http://localhost:" + port;
    }

    @Test
    public void healthCheckTest() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/healthz", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

}

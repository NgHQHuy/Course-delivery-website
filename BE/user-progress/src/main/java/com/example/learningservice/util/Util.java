package com.example.learningservice.util;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@AllArgsConstructor
public class Util {
    private WebClient webClient;
    private static Logger logger;

    public boolean checkForResponse(String uri) {
        AtomicBoolean isValid = new AtomicBoolean(false);

        try {
            //https://stackoverflow.com/a/68168585
            webClient.get()
                    .uri(uri)
                    .retrieve()
                    .toBodilessEntity().block();
            isValid.set(true);
        } catch (Exception ex) {
            logger = LoggerFactory.getLogger(ex.getClass());
            logger.error("Response from remote server: " + ex.getMessage());
        }

        return isValid.get();
    }

    public boolean checkPostRequest(String uri, Map<String, String> body) {
        AtomicBoolean isValid = new AtomicBoolean(false);

        try {
            //https://stackoverflow.com/a/68168585
            webClient.post()
                    .uri(uri)
                    .body(BodyInserters.fromValue(body))
                    .retrieve()
                    .toBodilessEntity().block();
            isValid.set(true);
        } catch (Exception ex) {
            logger = LoggerFactory.getLogger(ex.getClass());
            logger.error("Response from remote server: " + ex.getMessage());
        }

        return isValid.get();
    }

    public WebClient.RequestHeadersSpec<?> createGETRequest(String uri) {
        return webClient.get().uri(uri);
    }
}

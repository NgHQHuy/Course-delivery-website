package com.example.ratingservice.util;

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

    public WebClient.RequestHeadersSpec<?> createPOSTRequest(String uri, Map<String, Object> body) {
        return webClient.post().uri(uri).body(BodyInserters.fromValue(body));
    }
}

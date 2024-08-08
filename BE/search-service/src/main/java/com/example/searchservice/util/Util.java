package com.example.searchservice.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@AllArgsConstructor
public class Util {
    private WebClient webClient;

    public WebClient.RequestHeadersSpec<?> createGETRequest(String uri) {
        return webClient.get()
                .uri(uri);
    }

    public WebClient.RequestHeadersSpec<?> createPOSTRequest(String uri, Map<String, String> body) {
        return webClient.post()
                .uri(uri)
                .body(BodyInserters.fromValue(body));
    }

    public String encodeValue(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}

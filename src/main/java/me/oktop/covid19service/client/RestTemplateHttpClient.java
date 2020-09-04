package me.oktop.covid19service.client;

import lombok.extern.slf4j.Slf4j;
import me.oktop.covid19service.dto.request.AttachmentsRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
public class RestTemplateHttpClient {

    private static final RestTemplate restTemplate = new RestTemplate();

    public RestTemplateHttpClient() {
        restTemplate
                .getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    public static <T> ResponseEntity<T> requestHttpGet(URI uri,
                                                       HttpMethod httpMethod,
                                                       Class<T> responseType) {
        ResponseEntity<T> entity = restTemplate.exchange(uri, httpMethod, null, responseType);
        if (!entity.getStatusCode().is2xxSuccessful()) {
            log.error("RestTemplate request fail");
        }
        return entity;
    }

    public static <T> HttpStatus requestHttpPost(String uri,
                                                 AttachmentsRequest attachmentsRequest,
                                                 Class<T> responseType) {
        ResponseEntity<T> entity = restTemplate.postForEntity(uri, attachmentsRequest, responseType);
        return entity.getStatusCode();
    }

}

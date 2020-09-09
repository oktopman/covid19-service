package me.oktop.covid19service.client;

import me.oktop.covid19service.web.dto.request.DailyPatientRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Covid19BoardClient {

    public static ResponseEntity<DailyPatientRequest> send(String sidoInfStateUrl, String openapiAuthKey) throws URISyntaxException {
        return RestTemplateHttpClient.requestHttpGet(getPublicDataUri(sidoInfStateUrl, openapiAuthKey), HttpMethod.GET, DailyPatientRequest.class);
    }

    private static URI getPublicDataUri(String url, String authKey) throws URISyntaxException {
        int today = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        return new URI(String.format(url +
                        "?serviceKey=%s&pageNo=%s&numOfRows=%s&startCreateDt=%s&endCreateDt=%s",
                authKey, 1, 10, today, today));
    }
}
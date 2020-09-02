package me.oktop.covid19service.client;

import me.oktop.covid19service.dto.DailyBoardResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Covid19BoardClient {

    private static final String sidoInfStateUrl = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson";
    private static final String authKey = "L54KA%2BbfldcX5GKUxG6cB16LffsXzBZ8IuZEfgx9cWKuiApTFC%2B3GOsGusJZv1FZq7fpQqUmhtUh0VK9eWHD3g%3D%3D";

    public static ResponseEntity<DailyBoardResponse> send() throws URISyntaxException {
        return RestTemplateHttpClient.requestHttpGet(getPublicDataUri(), HttpMethod.GET, DailyBoardResponse.class);
    }

    private static URI getPublicDataUri() throws URISyntaxException {
        int today = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        return new URI(String.format(sidoInfStateUrl +
                        "?serviceKey=%s&pageNo=%s&numOfRows=%s&startCreateDt=%s&endCreateDt=%s",
                authKey, 1, 10, today, today));
    }
}
package me.oktop.covid19service.service;

import lombok.RequiredArgsConstructor;
import me.oktop.covid19service.dto.DailyBoardResponse;
import me.oktop.covid19service.dto.WebhookRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyBatchService {
    private static final String sidoInfStateUrl = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson";
    private static final String authKey = "L54KA%2BbfldcX5GKUxG6cB16LffsXzBZ8IuZEfgx9cWKuiApTFC%2B3GOsGusJZv1FZq7fpQqUmhtUh0VK9eWHD3g%3D%3D";
    private static final String covidChannelUrl = "https://hooks.slack.com/services/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";

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(cron = "0 1 10 * * *", zone = "Asia/Seoul")
    public void covid19BoardSchedule() throws URISyntaxException {
        int today = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        URI url = new URI(String.format(sidoInfStateUrl +
                        "?serviceKey=%s&pageNo=%s&numOfRows=%s&startCreateDt=%s&endCreateDt=%s",
                authKey, 1, 10, today, today));

        ResponseEntity<DailyBoardResponse> entity = restTemplate.exchange(url, HttpMethod.GET, null, DailyBoardResponse.class);
        if (!entity.getStatusCode().is2xxSuccessful()) {
            System.out.println("fail");
            return;
        }
        List<DailyBoardResponse.Item> item = entity.getBody().getBody().getItems().getItem();
        StringBuilder sb = new StringBuilder();
        for (DailyBoardResponse.Item element : item) {
            if (!"서울".equals(element.getGubun())) {
                continue;
            }
            sb.append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .append(" 코로나보드 ")
                    .append(element.getGubun())
                    .append("지역 현황입니다.\n");
            sb.append("누적 확진자 :").append(element.getDefCnt()).append("\n");
            sb.append("오늘 확진자 :").append(element.getIncDec()).append("\n");
            sb.append("격리중 :").append(element.getIsolIngCnt()).append("\n");
            sb.append("누적 격리 해제 :").append(element.getIsolClearCnt()).append("\n");
            sb.append("사망자 :").append(element.getDeathCnt()).append("\n");
        }

        this.webhookToSlack(new WebhookRequest(sb.toString()));
    }

    public HttpStatus webhookToSlack(WebhookRequest request) {
        ResponseEntity<String> entity = restTemplate.postForEntity(covidChannelUrl, request, String.class);
        return entity.getStatusCode();
    }

}


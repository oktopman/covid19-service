package me.oktop.covid19service.service;

import lombok.RequiredArgsConstructor;
import me.oktop.covid19service.client.Covid19BoardClient;
import me.oktop.covid19service.client.SlackWebhookClient;
import me.oktop.covid19service.dto.request.DailyPatientRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyBatchService {

    private final PatientService patientService;

    @Value("${openapi.auth.url}")
    private String sidoInfStateUrl;

    @Value("${openapi.auth.key}")
    private String openapiAuthKey;

    @Value("${slack.channel.url}")
    private String covidChannelUrl;

    @Scheduled(cron = "0 1 10 * * *", zone = "Asia/Seoul")
    public void patientSchedule() throws URISyntaxException {
        ResponseEntity<DailyPatientRequest> entity = Covid19BoardClient.send(sidoInfStateUrl, openapiAuthKey);
        List<DailyPatientRequest.Item> itemList = entity.getBody().getBody().getItems().getItem();
        patientService.saveDailyPatient(itemList);
        SlackWebhookClient.send(itemList, covidChannelUrl);
    }

}


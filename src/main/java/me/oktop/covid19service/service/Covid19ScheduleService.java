package me.oktop.covid19service.service;

import lombok.RequiredArgsConstructor;
import me.oktop.covid19service.client.Covid19BoardClient;
import me.oktop.covid19service.client.SlackWebhookClient;
import me.oktop.covid19service.web.dto.request.DailyPatientRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class Covid19ScheduleService {

    private final PatientService patientService;

    @Value("${openapi.auth.url}")
    private String sidoInfStateUrl;

    @Value("${openapi.auth.key}")
    private String openapiAuthKey;

    @Value("${slack.channel.url}")
    private String covidChannelUrl;

    @Scheduled(cron = "0 1 10 * * *", zone = "Asia/Seoul")
    public void patientDailySchedule() throws URISyntaxException {
        int today = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        ResponseEntity<DailyPatientRequest> entity = Covid19BoardClient.send(sidoInfStateUrl, openapiAuthKey, today, today);
        List<DailyPatientRequest.Item> itemList = entity.getBody().getBody().getItems().getItem();
        if (itemList == null) {
            SlackWebhookClient.send(null, covidChannelUrl);
            return;
        }
        patientService.saveDailyPatients(itemList);
        patientService.cacheDailyPatients(itemList);
        SlackWebhookClient.send(itemList, covidChannelUrl);
    }

    public void patientManualSchedule(String start, String end) throws URISyntaxException {
        int[] startArr = Arrays.stream(start.split("-")).mapToInt(Integer::parseInt).toArray();
        int[] endArr = Arrays.stream(end.split("-")).mapToInt(Integer::parseInt).toArray();
        int startDate = Integer.parseInt(LocalDate.of(startArr[0], startArr[1], startArr[2]).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        int endDate = Integer.parseInt(LocalDate.of(endArr[0], endArr[1], endArr[2]).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        ResponseEntity<DailyPatientRequest> entity = Covid19BoardClient.send(sidoInfStateUrl, openapiAuthKey, startDate, endDate);
        List<DailyPatientRequest.Item> itemList = entity.getBody().getBody().getItems().getItem();

        patientService.saveDailyPatients(itemList);
    }

}


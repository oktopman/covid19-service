package me.oktop.covid19service.service;

import lombok.RequiredArgsConstructor;
import me.oktop.covid19service.client.Covid19BoardClient;
import me.oktop.covid19service.client.SlackWebhookClient;
import me.oktop.covid19service.dto.DailyBoardResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
public class DailyBatchService {

    @Scheduled(cron = "0 1 10 * * *", zone = "Asia/Seoul")
    public void covid19BoardSchedule() throws URISyntaxException {
        ResponseEntity<DailyBoardResponse> entity = Covid19BoardClient.send();
        SlackWebhookClient.send(entity.getBody());
    }

}


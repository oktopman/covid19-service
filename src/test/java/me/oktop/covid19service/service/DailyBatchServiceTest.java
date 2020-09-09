package me.oktop.covid19service.service;

import me.oktop.covid19service.client.SlackWebhookClient;
import me.oktop.covid19service.web.dto.request.DailyPatientRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DailyBatchServiceTest {

    @Autowired
    private DailyBatchService dailyBatchService;

    @Value("${slack.channel.url}")
    private String covidChannelUrl;

    @Test
    @DisplayName("데일리 환자 수동 테스트")
    @Transactional
    void covid19_patient_batch_test() throws URISyntaxException {
        dailyBatchService.patientSchedule();
    }

    @Test
    @DisplayName("코로나소식 지역발생 감염자 호출 성공시 웹훅 테스트")
    void corona_notice_slack_webhook_test() {
        //given
        DailyPatientRequest response = new DailyPatientRequest();
        DailyPatientRequest.Body body = new DailyPatientRequest.Body();
        DailyPatientRequest.Items items = new DailyPatientRequest.Items();
        List<DailyPatientRequest.Item> itemList = new ArrayList<>();
        DailyPatientRequest.Item item = new DailyPatientRequest.Item();
        item.setDefCnt("");
        item.setIncDec("");
        item.setIsolIngCnt("");
        item.setIsolClearCnt("");
        item.setDeathCnt("");
        itemList.add(item);
        items.setItem(itemList);
        body.setItems(items);
        response.setBody(body);
        //when
        HttpStatus status = SlackWebhookClient.send(response.getBody().getItems().getItem(), covidChannelUrl);
        //then
        assertTrue(status.is2xxSuccessful());
    }

    @Test
    @DisplayName("코로나 지역발생 감염자 호출 실패시 웹훅 테스트")
    void corona_notice_fail_slack_webhook_test() {
        //given
        DailyPatientRequest response = new DailyPatientRequest();
        DailyPatientRequest.Body body = new DailyPatientRequest.Body();
        DailyPatientRequest.Items items = new DailyPatientRequest.Items();
        body.setItems(items);
        response.setBody(body);
        //when
        HttpStatus status = SlackWebhookClient.send(response.getBody().getItems().getItem(), covidChannelUrl);
        //then
        assertTrue(status.is2xxSuccessful());
    }

}
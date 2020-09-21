package me.oktop.covid19service.service;

import me.oktop.covid19service.client.SlackWebhookClient;
import me.oktop.covid19service.web.dto.request.DailyPatientRequest;
import org.junit.jupiter.api.Disabled;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
class Covid19ScheduleServiceTest {

    @Autowired
    private Covid19ScheduleService covid19ScheduleService;

    @Value("${slack.channel.url}")
    private String covidChannelUrl;

    @Test
    @DisplayName("데일리 환자 수동 테스트")
    @Transactional
    void covid19_patient_batch_test() throws URISyntaxException {
        covid19ScheduleService.patientDailySchedule();
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

    @Test
    @DisplayName("스트링포맷 날짜를 localDate 형태로 변경 테스트")
    void string_to_localdate_test() {
//        String stringFormatDate = "2020년 09월 03일 00시";
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
//        LocalDate parse = LocalDate.parse(stringFormatDate, format);
//        System.out.println(parse);

        String string = "2019년 01월 10일 00시".substring(0, 13);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        LocalDate date = LocalDate.parse(string, formatter);
        System.out.println(date);
        System.out.println(date.format(formatter));

    }

    @Test
    @DisplayName("LocalDate format 을 - 기준으로 파싱하여 배열에 저장 테스트")
    void localdate_parsing_array_save_test() {
        //given
        LocalDate startDate = LocalDate.of(2020,9,22);
        LocalDate endDate = LocalDate.of(2020,9,23);
        //when
        int start = Integer.parseInt(startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        int end = Integer.parseInt(endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        //then
        assertThat(start, is(20200922));
        assertThat(end, is(20200923));
    }

}
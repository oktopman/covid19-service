package me.oktop.covid19service.service;

import me.oktop.covid19service.client.SlackWebhookClient;
import me.oktop.covid19service.dto.DailyBoardResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DailyBatchServiceTest {

    @Autowired
    private DailyBatchService dailyBatchService;

    @Test
    @DisplayName("지역발생 감염자 데이터 조회후 슬랙 웹훅 테스트")
    void covid19_area_occur_select_after__slack_webhook_test() throws URISyntaxException {
        dailyBatchService.covid19BoardSchedule();
    }

    @Test
    @DisplayName("코로나소식 지역발생 감염자 호출 성공시 웹훅 테스트")
    void corona_notice_slack_webhook_test() {
        //given
        DailyBoardResponse response = new DailyBoardResponse();
        DailyBoardResponse.Body body = new DailyBoardResponse.Body();
        DailyBoardResponse.Items items = new DailyBoardResponse.Items();
        List<DailyBoardResponse.Item> itemList = new ArrayList<>();
        DailyBoardResponse.Item item = new DailyBoardResponse.Item();
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
        HttpStatus status = SlackWebhookClient.send(response);
        //then
        assertTrue(status.is2xxSuccessful());
    }

    @Test
    @DisplayName("코로나 지역발생 감염자 호출 실패시 웹훅 테스트")
    void corona_notice_fail_slack_webhook_test() {
        //given
        DailyBoardResponse response = new DailyBoardResponse();
        DailyBoardResponse.Body body = new DailyBoardResponse.Body();
        DailyBoardResponse.Items items = new DailyBoardResponse.Items();
        body.setItems(items);
        response.setBody(body);
        //when
        HttpStatus status = SlackWebhookClient.send(response);
        //then
        assertTrue(status.is2xxSuccessful());
    }

}
package me.oktop.covid19service.service;

import me.oktop.covid19service.dto.WebhookRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DailyBatchServiceTest {

    @Autowired
    private DailyBatchService dailyBatchService;

    @Test
    @DisplayName("지역발생 감염자 호출 테스트")
    void covid19_area_occur_test() throws URISyntaxException {
        dailyBatchService.covid19BoardSchedule();
    }

    @Test
    @DisplayName("코로나소식 슬랙채널 웹훅 테스트")
    void corona_notice_slack_webhook_test() {
        //given
        String message = "누적 확진자 : 3,961명\n" +
                "전일 대비 증감 : \t(+94)\n" +
                "격리중: \t1,937명\n" +
                "누적 격리해제: \t2,001명\n" +
                "사망자: \t23명\n";
        //when
        HttpStatus httpStatus = dailyBatchService.webhookToSlack(new WebhookRequest(message));
        //then
        assertTrue(httpStatus.is2xxSuccessful());
    }

}
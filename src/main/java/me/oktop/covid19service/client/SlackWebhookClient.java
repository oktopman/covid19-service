package me.oktop.covid19service.client;

import me.oktop.covid19service.dto.Attachments;
import me.oktop.covid19service.dto.DailyBoardResponse;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class SlackWebhookClient {

    private static final String covidChannelUrl = "https://hooks.slack.com/services/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";

    public static HttpStatus send(DailyBoardResponse entity) {
        return RestTemplateHttpClient.requestHttpPost(covidChannelUrl, getAttachments(entity), String.class);
    }

    private static Attachments getAttachments(DailyBoardResponse entity) {
        List<Attachments.Attachment> attachments = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        Attachments.Attachment attachment = new Attachments.Attachment();
        if (entity.getBody().getItems().getItem() == null) {
            sb.append("코로나 지역발생 감염자 데이터 조회에 실패 하였습니다.\n");
            attachments.add(attachment.createFailAttachment(sb));
            return new Attachments(attachments);
        }

        for (DailyBoardResponse.Item element : entity.getBody().getItems().getItem()) {
            if (!"서울".equals(element.getGubun())) {
                continue;
            }
            sb.append("누적 확진자 :").append(element.getDefCnt()).append("\n");
            sb.append("오늘 확진자 :").append(element.getIncDec()).append("\n");
            sb.append("격리 치료 :").append(element.getIsolIngCnt()).append("\n");
            sb.append("퇴원 :").append(element.getIsolClearCnt()).append("\n");
            sb.append("사망자 :").append(element.getDeathCnt()).append("\n");
            break;
        }

        attachments.add(attachment.createSuccessAttachment(sb));

        return new Attachments(attachments);
    }

}

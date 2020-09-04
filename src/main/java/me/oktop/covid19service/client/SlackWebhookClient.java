package me.oktop.covid19service.client;

import me.oktop.covid19service.dto.request.AttachmentsRequest;
import me.oktop.covid19service.dto.request.DailyPatientRequest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class SlackWebhookClient {

    public static HttpStatus send(List<DailyPatientRequest.Item> itemList, String covidChannelUrl) {
        return RestTemplateHttpClient.requestHttpPost(covidChannelUrl, getAttachments(itemList), String.class);
    }

    private static AttachmentsRequest getAttachments(List<DailyPatientRequest.Item> itemList) {
        List<AttachmentsRequest.Attachment> attachments = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        AttachmentsRequest.Attachment attachment = new AttachmentsRequest.Attachment();
        if (itemList == null) {
            sb.append("코로나 지역발생 감염자 데이터 조회에 실패 하였습니다.\n");
            attachments.add(attachment.createFailAttachment(sb));
            return new AttachmentsRequest(attachments);
        }

        for (DailyPatientRequest.Item element : itemList) {
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
        return new AttachmentsRequest(attachments);
    }

}

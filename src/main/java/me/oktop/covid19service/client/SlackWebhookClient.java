package me.oktop.covid19service.client;

import me.oktop.covid19service.web.dto.request.AttachmentsRequest;
import me.oktop.covid19service.web.dto.request.DailyPatientRequest;
import me.oktop.covid19service.formatter.SlackFailFormatter;
import me.oktop.covid19service.formatter.SlackFormatter;
import me.oktop.covid19service.formatter.SlackSeoulFormatter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class SlackWebhookClient {

    public static HttpStatus send(List<DailyPatientRequest.Item> itemList, String covidChannelUrl) {
        return RestTemplateHttpClient.requestHttpPost(covidChannelUrl, getAttachments(itemList), String.class);
    }

    private static AttachmentsRequest getAttachments(List<DailyPatientRequest.Item> itemList) {
        List<AttachmentsRequest.Attachment> attachments = new ArrayList<>();
        AttachmentsRequest.Attachment attachment = new AttachmentsRequest.Attachment();
        if (itemList == null) {
            attachments.add(attachment.createFailAttachment(makeText(null, new SlackFailFormatter())));
            return new AttachmentsRequest(attachments);
        }
        attachments.add(attachment.createSuccessAttachment(makeText(itemList, new SlackSeoulFormatter())));
        return new AttachmentsRequest(attachments);
    }

    private static StringBuilder makeText(List<DailyPatientRequest.Item> itemList, SlackFormatter formatter) {
        return formatter.accept(itemList);
    }

}

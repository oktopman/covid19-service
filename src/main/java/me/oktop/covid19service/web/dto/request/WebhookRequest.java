package me.oktop.covid19service.web.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WebhookRequest {
    private String text;

    public WebhookRequest(String text) {
        this.text = text;
    }
}

package me.oktop.covid19service.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@NoArgsConstructor
public class AttachmentsRequest {
    private List<Attachment> attachments;

    @Builder
    public AttachmentsRequest(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    @Getter
    @NoArgsConstructor
    public static class Attachment {
        private String fallback;
        private String color;
        private String pretext;
        private String author_name;
        private String author_link;
        private String author_icon;
        private String title;
        private String title_link;
        private String text;

        @Builder
        public Attachment(String fallback, String color, String pretext, String author_name, String author_link,
                          String author_icon, String title, String title_link, String text) {
            this.fallback = fallback;
            this.color = color;
            this.pretext = pretext;
            this.author_name = author_name;
            this.author_link = author_link;
            this.author_icon = author_icon;
            this.title = title;
            this.title_link = title_link;
            this.text = text;
        }

        public Attachment createSuccessAttachment(StringBuilder sb) {
            return Attachment.builder()
                    .fallback("fallback message : fail")
                    .color("#36a64f")
                    .pretext(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                            " 서울 코로나보드 현황 입니다.")
                    .title("모든 지역 현황 보기")
                    .title_link("https://api.slack.com/")
                    .text(sb.toString())
                    .build();
        }

        public Attachment createFailAttachment(StringBuilder sb) {
            return AttachmentsRequest.Attachment.builder()
                    .fallback("fallback message : fail")
                    .color("#dc3545")
                    .title("코로나 지역 발생 감염자 데이터 백업 실패")
                    .text(sb.toString())
                    .build();
        }
    }
}

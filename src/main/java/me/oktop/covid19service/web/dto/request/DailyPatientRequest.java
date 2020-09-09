package me.oktop.covid19service.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "response")
public class DailyPatientRequest {
    private Header header;
    private Body body;

    @Getter
    @Setter
    @XmlRootElement(name = "header")
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Getter
    @Setter
    @XmlRootElement(name = "body")
    public static class Body {
        private Items items;
    }

    @Getter
    @Setter
    @XmlRootElement(name = "item")
    public static class Items {
        private List<Item> item;
    }

    @Getter
    @Setter
    @XmlRootElement(name = "item")
    public static class Item {
        private String createDt;
        private String deathCnt;
        private String defCnt;
        private String gubun;
        private String incDec;
        private String isolClearCnt;
        private String isolIngCnt;
        private String localOccCnt;
        private String overFlowCnt;
        private String qurRate;
        private String stdDay;
        private String updateDt;
    }
}

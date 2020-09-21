package me.oktop.covid19service.formatter;

import me.oktop.covid19service.web.dto.request.DailyPatientRequest;

import java.util.List;

public class SlackSeoulFormatter implements SlackFormatter {
    @Override
    public StringBuilder accept(List<DailyPatientRequest.Item> itemList) {
        StringBuilder sb = new StringBuilder();
        itemList.stream()
                .filter(e -> "서울".equals(e.getGubun()))
                .forEach(e -> {
                    sb.append("누적 확진자 :").append(e.getDefCnt()).append("\n");
                    sb.append("오늘 확진자 :").append(e.getIncDec()).append("\n");
                    sb.append("격리 치료 :").append(e.getIsolIngCnt()).append("\n");
                    sb.append("퇴원 :").append(e.getIsolClearCnt()).append("\n");
                    sb.append("사망자 :").append(e.getDeathCnt()).append("\n");
                });
        return sb;
    }
}

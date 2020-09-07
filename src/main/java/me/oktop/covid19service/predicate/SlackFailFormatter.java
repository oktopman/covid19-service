package me.oktop.covid19service.predicate;

import me.oktop.covid19service.dto.request.DailyPatientRequest;

import java.util.List;

public class SlackFailFormatter implements SlackFormatter {
    @Override
    public StringBuilder accept(List<DailyPatientRequest.Item> itemList) {
        return new StringBuilder("코로나 지역발생 감염자 데이터 조회에 실패 하였습니다.\n");
    }
}

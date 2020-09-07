package me.oktop.covid19service.predicate;

import me.oktop.covid19service.dto.request.DailyPatientRequest;

import java.util.List;

public interface SlackFormatter {
    StringBuilder accept(List<DailyPatientRequest.Item> itemList);
}

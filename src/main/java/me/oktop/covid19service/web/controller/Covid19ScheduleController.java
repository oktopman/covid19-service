package me.oktop.covid19service.web.controller;

import lombok.RequiredArgsConstructor;
import me.oktop.covid19service.service.Covid19ScheduleService;
import me.oktop.covid19service.web.dto.request.SearchDateRequest;
import me.oktop.covid19service.web.dto.response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RequiredArgsConstructor
@RequestMapping(value = "/manual")
@RestController
public class Covid19ScheduleController {

    private final Covid19ScheduleService covid19ScheduleService;

    @PostMapping("/save/schedule")
    public ResponseEntity patientManualSchedule(final @RequestBody SearchDateRequest request) throws URISyntaxException {
        covid19ScheduleService.patientManualSchedule(request.getStartDate(), request.getEndDate());
        return ResponseEntity.ok(CommonResponse.success());
    }
}

package me.oktop.covid19service.web.controller;

import lombok.RequiredArgsConstructor;
import me.oktop.covid19service.service.Covid19ScheduleService;
import me.oktop.covid19service.web.dto.response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RequiredArgsConstructor
@RequestMapping(value = "/manual")
@RestController
public class Covid19ScheduleController {

    private final Covid19ScheduleService covid19ScheduleService;

    @GetMapping("/save/schedule")
    public ResponseEntity patientManualSchedule(@RequestParam String start, @RequestParam String end) throws URISyntaxException {
        covid19ScheduleService.patientManualSchedule(start, end);
        return ResponseEntity.ok(CommonResponse.success());
    }
}

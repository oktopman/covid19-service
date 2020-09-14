package me.oktop.covid19service.web.controller;

import lombok.RequiredArgsConstructor;
import me.oktop.covid19service.service.PatientService;
import me.oktop.covid19service.web.dto.response.CommonResponse;
import me.oktop.covid19service.web.dto.response.PatientLatestResponse;
import me.oktop.covid19service.web.dto.response.PatientsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/patients")
@RestController
public class PatientBoardController {
    private final PatientService patientService;

    @GetMapping("/daily")
    public ResponseEntity getDailyPatientsBoard() {
        PatientsResponse patientsResponse = patientService.getDailyPatientsBoard();
        return ResponseEntity.ok(CommonResponse.success(patientsResponse));
    }

    @GetMapping("/latest")
    public ResponseEntity getLatestPatients() {
        PatientsResponse<PatientLatestResponse> latestPatientsResponse = patientService.getLatestPatients();
        return ResponseEntity.ok(CommonResponse.success(latestPatientsResponse));
    }

}

package me.oktop.covid19service.controller;

import lombok.RequiredArgsConstructor;
import me.oktop.covid19service.dto.response.CommonResponse;
import me.oktop.covid19service.dto.response.PatientsResponse;
import me.oktop.covid19service.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/patients")
@RestController
public class PatientBoardController {
    private final PatientService patientService;

    @GetMapping("/board")
    public ResponseEntity getPatientsBoard() {
        PatientsResponse patientsResponse = patientService.getPatientsBoard();
        return ResponseEntity.ok(CommonResponse.success(patientsResponse));
    }
}

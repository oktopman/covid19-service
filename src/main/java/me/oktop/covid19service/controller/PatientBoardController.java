package me.oktop.covid19service.controller;

import lombok.RequiredArgsConstructor;
import me.oktop.covid19service.dto.response.PatientsResponse;
import me.oktop.covid19service.service.PatientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/patient")
@RestController
public class PatientBoardController {
    private final PatientService patientService;

    @GetMapping("/boards")
    public PatientsResponse getPatientsBoard() {
        return patientService.getPatientsBoard();
    }
}

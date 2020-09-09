package me.oktop.covid19service.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PatientsResponse {
    private List<PatientResponse> patientResponses;

    @Builder
    public PatientsResponse(List<PatientResponse> patientResponses) {
        this.patientResponses = patientResponses;
    }
}

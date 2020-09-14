package me.oktop.covid19service.web.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PatientsResponse<T> {
    private List<T> patientResponses;

    @Builder
    public PatientsResponse(List<T> patientResponses) {
        this.patientResponses = patientResponses;
    }
}

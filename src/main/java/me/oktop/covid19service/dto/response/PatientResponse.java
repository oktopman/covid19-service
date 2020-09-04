package me.oktop.covid19service.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientResponse {
    private Long id;
    private String area;
    private Integer totalCount;
    private Integer increaseCount;
    private Integer isolationCount;
    private Integer dischargedCount;
    private Integer deathCount;

}

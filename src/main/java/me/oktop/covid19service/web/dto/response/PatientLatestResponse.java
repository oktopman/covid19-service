package me.oktop.covid19service.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientLatestResponse {
    private String area;
    private Integer totalCount;
    private Integer increaseCount;
    private String occurDate;

    @Builder
    public PatientLatestResponse(String area, Integer totalCount, Integer increaseCount, String occurDate) {
        this.area = area;
        this.totalCount = totalCount;
        this.increaseCount = increaseCount;
        this.occurDate = occurDate;
    }
}

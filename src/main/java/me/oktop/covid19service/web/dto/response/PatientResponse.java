package me.oktop.covid19service.web.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientResponse {
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonIgnore
    private Long id;
    private String area;
    private Integer totalCount;
    private Integer increaseCount;
    private Integer isolationCount;
    private Integer dischargedCount;
    private Integer deathCount;
    private String occurDate;

    @Builder
    public PatientResponse(Long id, String area, Integer totalCount, Integer increaseCount,
                           Integer isolationCount, Integer dischargedCount, Integer deathCount, String occurDate) {
        this.id = id;
        this.area = area;
        this.totalCount = totalCount;
        this.increaseCount = increaseCount;
        this.isolationCount = isolationCount;
        this.dischargedCount = dischargedCount;
        this.deathCount = deathCount;
        this.occurDate = occurDate;
    }

}

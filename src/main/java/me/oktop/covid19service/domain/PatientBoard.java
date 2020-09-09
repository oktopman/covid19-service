package me.oktop.covid19service.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.oktop.covid19service.web.dto.response.PatientResponse;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "patient_board")
public class PatientBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "area")
    private String area;

    @Column(name = "total_count")
    private Integer totalCount;

    @Column(name = "increase_count")
    private Integer increaseCount;

    @Column(name = "isolation_count")
    private Integer isolationCount;

    @Column(name = "discharged_count")
    private Integer dischargedCount;

    @Column(name = "death_count")
    private Integer deathCount;

    @Builder
    public PatientBoard(String area, Integer totalCount, Integer increaseCount, Integer isolationCount,
                        Integer dischargedCount, Integer deathCount) {
        this.area = area;
        this.totalCount = totalCount;
        this.increaseCount = increaseCount;
        this.isolationCount = isolationCount;
        this.dischargedCount = dischargedCount;
        this.deathCount = deathCount;
    }

    public PatientResponse toDto() {
        PatientResponse dto = new PatientResponse();
        BeanUtils.copyProperties(this, dto);
        return dto;
    }
}

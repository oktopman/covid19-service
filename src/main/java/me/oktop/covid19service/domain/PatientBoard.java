package me.oktop.covid19service.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.oktop.covid19service.utils.DateConverter;
import me.oktop.covid19service.web.dto.request.DailyPatientRequest;
import me.oktop.covid19service.web.dto.response.PatientLatestResponse;
import me.oktop.covid19service.web.dto.response.PatientResponse;
import me.oktop.covid19service.web.dto.response.PatientsResponse;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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

    @Column(name = "occur_date")
    private LocalDate occurDate;

    @Builder
    public PatientBoard(String area, Integer totalCount, Integer increaseCount, Integer isolationCount,
                        Integer dischargedCount, Integer deathCount, LocalDate occurDate) {
        this.area = area;
        this.totalCount = totalCount;
        this.increaseCount = increaseCount;
        this.isolationCount = isolationCount;
        this.dischargedCount = dischargedCount;
        this.deathCount = deathCount;
        this.occurDate = occurDate;
    }

    private PatientResponse toPatientResponse() {
        PatientResponse patientResponse = new PatientResponse();
        BeanUtils.copyProperties(this, patientResponse);
        patientResponse.setOccurDate(this.occurDate.toString());
        return patientResponse;
    }

    public static PatientsResponse<PatientResponse> toPatientsResponse(List<PatientBoard> patientBoards) {
        List<PatientResponse> patientResponses = new ArrayList<>();
        Function<List<PatientBoard>, PatientsResponse<PatientResponse>> function = v -> {
            v.forEach(e -> patientResponses.add(e.toPatientResponse()));
            return PatientsResponse.<PatientResponse>builder().patientResponses(patientResponses).build();
        };
        return function.apply(patientBoards);
//        patientBoards.forEach(element -> patientResponses.add(element.toPatientResponse()));
//        return PatientsResponse.<PatientResponse>builder().patientResponses(patientResponses).build();
    }

    public static PatientsResponse<PatientLatestResponse> toPatientsLatestResponse(List<PatientBoard> patientBoards) {
        List<PatientLatestResponse> patientLatestResponses = new ArrayList<>();
        Function<List<PatientBoard>, PatientsResponse<PatientLatestResponse>> function = v -> {
            patientBoards.forEach(element -> patientLatestResponses.add(element.toPatientLatestResponse()));
            return PatientsResponse.<PatientLatestResponse>builder().patientResponses(patientLatestResponses).build();
        };
        return function.apply(patientBoards);
//        patientBoards.forEach(element -> patientLatestResponses.add(element.toPatientLatestResponse()));
//        return PatientsResponse.<PatientLatestResponse>builder().patientResponses(patientLatestResponses).build();
    }

    private PatientLatestResponse toPatientLatestResponse() {
        PatientLatestResponse patientLatestResponse = new PatientLatestResponse();
        BeanUtils.copyProperties(this, patientLatestResponse);
        patientLatestResponse.setOccurDate(this.occurDate.toString());
        return patientLatestResponse;
    }

    public static PatientBoard toPatientBoard(DailyPatientRequest.Item item) {
        return PatientBoard.builder()
                .area(item.getGubun())
                .totalCount(Integer.parseInt(item.getDefCnt()))
                .increaseCount(Integer.parseInt(item.getIncDec()))
                .isolationCount(Integer.parseInt(item.getIsolIngCnt()))
                .dischargedCount(Integer.parseInt(item.getIsolClearCnt()))
                .deathCount(Integer.parseInt(item.getDeathCnt()))
                .occurDate(DateConverter.toLocalDate(item.getStdDay()))
                .build();
    }

    public void updatePatientBoard(DailyPatientRequest.Item item) {
        this.area = item.getGubun();
        this.totalCount = Integer.parseInt(item.getDefCnt());
        this.increaseCount = Integer.parseInt(item.getIncDec());
        this.isolationCount = Integer.parseInt(item.getIsolIngCnt());
        this.dischargedCount = Integer.parseInt(item.getIsolClearCnt());
        this.deathCount = Integer.parseInt(item.getDefCnt());
        this.occurDate = DateConverter.toLocalDate(item.getStdDay());
    }

}

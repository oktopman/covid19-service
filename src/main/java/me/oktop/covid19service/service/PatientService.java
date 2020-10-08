package me.oktop.covid19service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.oktop.covid19service.domain.PatientBoard;
import me.oktop.covid19service.domain.PatientRepository;
import me.oktop.covid19service.utils.DateConverter;
import me.oktop.covid19service.web.dto.request.DailyPatientRequest;
import me.oktop.covid19service.web.dto.response.PatientLatestResponse;
import me.oktop.covid19service.web.dto.response.PatientResponse;
import me.oktop.covid19service.web.dto.response.PatientsResponse;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public static final String MY_KEY = "today";

    public void saveDailyPatients(List<DailyPatientRequest.Item> itemList) {
        List<PatientBoard> patientBoardList = new ArrayList<>();
        itemList.forEach(element -> {
            PatientBoard patientBoard = patientRepository.findByOccurDate(DateConverter.toLocalDate(element.getStdDay()))
                    .orElse(PatientBoard.builder().build());
            patientBoard.updatePatientBoard(element);
            patientBoardList.add(patientBoard);
        });
        patientRepository.saveAll(patientBoardList);
    }

    @Cacheable(value = "corona", key = "#root.target.MY_KEY", cacheManager = "cacheManager")
    public PatientsResponse<PatientResponse> getDailyPatientsBoard() {
        List<PatientBoard> patientBoards = patientRepository.findAllByOccurDate(LocalDate.now());
        if (patientBoards.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return PatientBoard.toPatientsResponse(patientBoards);
    }

    public PatientsResponse<PatientLatestResponse> getLatestPatients() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(4);
        List<PatientBoard> patientBoards = patientRepository
                .findAllByOccurDateGreaterThanEqualAndOccurDateLessThanEqualOrderByOccurDateDesc(startDate, endDate);
        if (patientBoards.isEmpty()) {
            throw new EntityNotFoundException("latest patients entity not found.");
        }
        return PatientBoard.toPatientsLatestResponse(patientBoards);
    }

    @CachePut(value = "corona", key = "#root.target.MY_KEY", cacheManager = "cacheManager")
    public PatientsResponse<PatientResponse> cacheDailyPatients(List<DailyPatientRequest.Item> itemList) {
        List<PatientResponse> patientResponses = itemList.stream()
                .map(e -> PatientResponse.builder()
                        .area(e.getGubun())
                        .totalCount(Integer.parseInt(e.getDefCnt()))
                        .increaseCount(Integer.parseInt(e.getIncDec()))
                        .isolationCount(Integer.parseInt(e.getIsolIngCnt()))
                        .dischargedCount(Integer.parseInt(e.getIsolClearCnt()))
                        .deathCount(Integer.parseInt(e.getDeathCnt()))
                        .occurDate(DateConverter.toStringDate(e.getStdDay()))
                        .build())
                .collect(Collectors.toList());
        return PatientsResponse.<PatientResponse>builder().patientResponses(patientResponses).build();
    }

}

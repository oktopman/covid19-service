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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
    private RedisTemplate<String, PatientsResponse> redisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, PatientsResponse> valueOperations;

    @Value("${redis.patients.today.key}")
    private String todayKey;

    @Value("${redis.patients.latest.key}")
    private String latestKey;

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

    public PatientsResponse<PatientResponse> getDailyPatientsBoard() {
        if (isExists(todayKey)) {
            return valueOperations.get(todayKey);
        }

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
            System.out.println("patientboards is empty. size is "+ patientBoards.size());
            throw new EntityNotFoundException("latest patients entity not found.");
        }
        return PatientBoard.toPatientsLatestResponse(patientBoards);
    }

    public void cacheDailyPatients(List<DailyPatientRequest.Item> itemList) {
        List<PatientResponse> caches = new ArrayList<>();
//        itemList.stream()
//                .map(e -> PatientResponse.builder()
//                        .area(e.getGubun())
//                        .totalCount(Integer.parseInt(e.getDefCnt()))
//                        .increaseCount(Integer.parseInt(e.getIncDec()))
//                        .isolationCount(Integer.parseInt(e.getIsolIngCnt()))
//                        .dischargedCount(Integer.parseInt(e.getIsolClearCnt()))
//                        .deathCount(Integer.parseInt(e.getDeathCnt()))
//                        .occurDate(DateConverter.toStringDate(e.getStdDay()))
//                        .build())
//                .collect(Collectors.toList());
        itemList.forEach(e -> {
            PatientResponse response = PatientResponse.builder()
                    .area(e.getGubun())
                    .totalCount(Integer.parseInt(e.getDefCnt()))
                    .increaseCount(Integer.parseInt(e.getIncDec()))
                    .isolationCount(Integer.parseInt(e.getIsolIngCnt()))
                    .dischargedCount(Integer.parseInt(e.getIsolClearCnt()))
                    .deathCount(Integer.parseInt(e.getDeathCnt()))
                    .occurDate(DateConverter.toStringDate(e.getStdDay()))
                    .build();
            caches.add(response);
        });
        ValueOperations<String, PatientsResponse> valueOperations = redisTemplate.opsForValue();
        PatientsResponse patientsResponse = PatientsResponse.<PatientResponse>builder().patientResponses(caches).build();

        valueOperations.set(todayKey, patientsResponse);
    }

    private boolean isExists(String key) {
        return redisTemplate.hasKey(key);
    }

}

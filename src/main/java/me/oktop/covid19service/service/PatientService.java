package me.oktop.covid19service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.oktop.covid19service.domain.PatientBoard;
import me.oktop.covid19service.domain.PatientRepository;
import me.oktop.covid19service.web.dto.request.DailyPatientRequest;
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

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    @Resource
    private RedisTemplate<String, PatientsResponse> redisRedisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, PatientsResponse> valueOperations;

    @Value("${redis.patients.today.key}")
    private String todayKey;

    public void saveDailyPatients(List<DailyPatientRequest.Item> itemList) {
        if (patientRepository.existsByCreatedDate(LocalDate.now())) {
            return;
        }
        List<PatientBoard> patientBoardList = new ArrayList<>();
        itemList.forEach(element -> {
            PatientBoard patientBoard = PatientBoard.builder()
                    .area(element.getGubun())
                    .totalCount(Integer.parseInt(element.getDefCnt()))
                    .increaseCount(Integer.parseInt(element.getIncDec()))
                    .isolationCount(Integer.parseInt(element.getIsolIngCnt()))
                    .dischargedCount(Integer.parseInt(element.getIsolClearCnt()))
                    .deathCount(Integer.parseInt(element.getDeathCnt()))
                    .build();
            patientBoardList.add(patientBoard);
        });
        patientRepository.saveAll(patientBoardList);
    }

    public PatientsResponse getPatientsBoard() {
        if (isExists()) {
            return valueOperations.get(todayKey);
        }

        List<PatientBoard> patientBoards = patientRepository.findAllByCreatedDate(LocalDate.now());
        if (patientBoards.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return this.toDtos(patientBoards);
    }

    private PatientsResponse toDtos(List<PatientBoard> patientBoards) {
        List<PatientResponse> patientDtos = new ArrayList<>();
        patientBoards.forEach(element -> {
            patientDtos.add(element.toDto());
        });
        return PatientsResponse.builder().patientResponses(patientDtos).build();
    }

    public void cacheDailyPatients(List<DailyPatientRequest.Item> itemList) {
        List<PatientResponse> caches = new ArrayList<>();
        itemList.forEach(e -> {
            PatientResponse response = PatientResponse.builder()
                    .area(e.getGubun())
                    .totalCount(Integer.parseInt(e.getDefCnt()))
                    .increaseCount(Integer.parseInt(e.getIncDec()))
                    .isolationCount(Integer.parseInt(e.getIsolIngCnt()))
                    .dischargedCount(Integer.parseInt(e.getIsolClearCnt()))
                    .deathCount(Integer.parseInt(e.getDeathCnt()))
                    .build();
            caches.add(response);
        });
        ValueOperations<String, PatientsResponse> valueOperations = redisRedisTemplate.opsForValue();
        PatientsResponse patientsResponse = PatientsResponse.builder().patientResponses(caches).build();

        valueOperations.set(todayKey, patientsResponse);
    }

    private boolean isExists() {
        return redisRedisTemplate.hasKey(todayKey);
    }

}

package me.oktop.covid19service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.oktop.covid19service.domain.PatientBoard;
import me.oktop.covid19service.domain.PatientRepository;
import me.oktop.covid19service.dto.request.DailyPatientRequest;
import me.oktop.covid19service.dto.response.PatientResponse;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public void saveDailyPatient(List<DailyPatientRequest.Item> itemList) {
        if (itemList == null) {
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

    public List<PatientResponse> getPatientBoards() {
        LocalDate localDate = LocalDate.now();
        if (timeValid()) {
            localDate = localDate.minusDays(1);
        }
        List<PatientBoard> patientBoards = patientRepository.findAllByCreatedDate(localDate);
        if (patientBoards.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return this.toDtos(patientBoards);
    }

    private List<PatientResponse> toDtos(List<PatientBoard> patientBoards) {
        List<PatientResponse> patientDtos = new ArrayList<>();
        patientBoards.forEach(element -> {
            patientDtos.add(element.toDto());
        });
        return patientDtos;
    }

    private boolean timeValid() {
        LocalTime time = LocalTime.of(10, 0, 0);
        return LocalTime.now().isBefore(time);
    }
}

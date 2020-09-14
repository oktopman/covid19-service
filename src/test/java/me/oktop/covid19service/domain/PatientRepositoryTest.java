package me.oktop.covid19service.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    private PatientRepository repository;

    private List<PatientBoard> list = new ArrayList<>();

    @BeforeEach
    void setUp() {
        for (int i = 1; i <= 5; i++) {
            LocalDate date = LocalDate.of(2020, 9, i);
            PatientBoard patientBoard = PatientBoard.builder()
                    .area("지역" + i)
                    .totalCount(100)
                    .increaseCount(100)
                    .isolationCount(100)
                    .dischargedCount(100)
                    .deathCount(100)
                    .occurDate(date)
                    .build();
            list.add(patientBoard);
        }
    }

    @Test
    @DisplayName("데일리 환자현황 조회 테스트")
    void daily_patient_occurdate_select_test() {
        //given
        repository.saveAll(list);
        LocalDate date = LocalDate.of(2020, 9, 5);
        //when
        List<PatientBoard> patientsBoard = repository.findAllByOccurDate(date);
        //then
        assertThat(patientsBoard.get(0).getId(), is(5L));
        assertThat(patientsBoard.get(0).getArea(), is("지역5"));
    }

    @Test
    @DisplayName("데일리 환자현황 데이터가 있는지 테스트")
    void daily_occurdate_patient_exists_test() {
        //given
        repository.saveAll(list);
        LocalDate date = LocalDate.of(2020, 9, 5);
        //when
        boolean isExists = repository.existsByOccurDate(date);
        //then
        assertTrue(isExists);
    }

    @Test
    @DisplayName("최근 5일 지역별 발생 현황")
    void latest_5_day_patients_board_search_test() {
        //given
        repository.saveAll(list);
        LocalDate startDate = LocalDate.of(2020, 9, 1);
        LocalDate endDate = LocalDate.of(2020, 9, 5);
        //when`
        List<PatientBoard> patientBoardPage = repository
                .findAllByOccurDateGreaterThanEqualAndOccurDateLessThanEqualOrderByOccurDateDesc(startDate, endDate);
        //then
        assertThat(patientBoardPage.get(0).getId(), is(5L));
    }

}
package me.oktop.covid19service.service;

import me.oktop.covid19service.domain.PatientBoard;
import me.oktop.covid19service.domain.PatientRepository;
import me.oktop.covid19service.utils.DateConverter;
import me.oktop.covid19service.web.dto.request.DailyPatientRequest;
import me.oktop.covid19service.web.dto.response.PatientResponse;
import me.oktop.covid19service.web.dto.response.PatientsResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

//todo @Resource 가 달린 빈은 Mocking이 안됨. 추후 해결
@SpringBootTest
class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @Mock
    private PatientRepository patientRepository;

    @MockBean
    private RedisTemplate<String, PatientsResponse> redisTemplate;

    @MockBean
    private ValueOperations<String, PatientsResponse> valueOperations;

    @Value("${value.test:default value}")
    private String valueTest;


//    @Test
//    @DisplayName("데일리 환자 현황 조회 테스트")
//    void daily_patients_board_test() {
//        //given
//        given(redisTemplate.hasKey(anyString())).willReturn(false);
//        List<PatientBoard> mockList = new ArrayList<>();
//        PatientBoard patientBoard = PatientBoard.builder()
//                .area("지역")
//                .totalCount(100)
//                .increaseCount(100)
//                .isolationCount(100)
//                .dischargedCount(100)
//                .deathCount(100)
//                .occurDate(LocalDate.now())
//                .build();
//        mockList.add(patientBoard);
//        given(patientRepository.findAllByOccurDate(any())).willReturn(mockList);
//        //when
//        PatientsResponse<PatientResponse> dailyPatientsBoard = patientService.getDailyPatientsBoard();
//        //then
//        assertThat(dailyPatientsBoard.getPatientResponses().size(), is(1));
//        assertThat(dailyPatientsBoard.getPatientResponses().get(0).getArea(), is("지역"));
//        assertThat(dailyPatientsBoard.getPatientResponses().get(0).getTotalCount(), is(100));;
//    }

    @Test
    @DisplayName("@value default 값 적용 테스트")
    public void default_value_test() {
        //given
        String defaultValue = "default value";
        //when
        String testValue = valueTest;
        //then
        assertThat(testValue, is(defaultValue));

    }

    @Disabled //todo findAllByOccurDate mocking 안됨
    @Test
    @DisplayName("레디스 캐시 테스트")
    void redis_cache_test() {
        //given
        List<PatientBoard> patientBoards = Collections.singletonList(PatientBoard.builder()
                .area("area")
                .totalCount(1)
                .increaseCount(10)
                .isolationCount(100)
                .dischargedCount(1000)
                .deathCount(10000)
                .occurDate(LocalDate.now())
                .build());
        given(patientRepository.findAllByOccurDate(any())).willReturn(patientBoards);
        //when
        PatientsResponse<PatientResponse> dailyPatientsBoard = patientService.getDailyPatientsBoard();
        //then
        assertThat(dailyPatientsBoard.getPatientResponses().get(0).getDeathCount(), is(10000));
    }

    @Disabled
    @Test
    @DisplayName("레디스 캐시 갱신 테스트")
    void redis_cache_put_test() {
        //given
        DailyPatientRequest.Item item = new DailyPatientRequest.Item();
        item.setCreateDt("2020-10-08");
        item.setDeathCnt("2");
        item.setDefCnt("2");
        item.setGubun("gubun");
        item.setIncDec("2");
        item.setIsolClearCnt("2");
        item.setIsolIngCnt("2");
        item.setLocalOccCnt("2");
        item.setOverFlowCnt("2");
        item.setQurRate("2");
        item.setStdDay("2020년 10월 08일 00시");
        item.setUpdateDt("1");
        //when
        PatientsResponse<PatientResponse> patientsResponse = patientService.cacheDailyPatients(Collections.singletonList(item));
        //then
        assertThat(patientsResponse.getPatientResponses().size(), is(1));
        assertThat(patientsResponse.getPatientResponses().get(0).getOccurDate(), is("2020-10-08"));
    }
}
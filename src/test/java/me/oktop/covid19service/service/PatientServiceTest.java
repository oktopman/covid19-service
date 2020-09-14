//package me.oktop.covid19service.service;
//
//import me.oktop.covid19service.domain.PatientBoard;
//import me.oktop.covid19service.domain.PatientRepository;
//import me.oktop.covid19service.web.dto.response.PatientResponse;
//import me.oktop.covid19service.web.dto.response.PatientsResponse;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.given;
//
//todo @Resource 가 달린 빈은 Mocking이 안됨. 추후 해결
//@SpringBootTest
//class PatientServiceTest {
//
//    @Autowired
//    private PatientService patientService;
//
//    @Mock
//    private PatientRepository patientRepository;
//
//    @MockBean
//    private RedisTemplate<String, PatientsResponse> redisTemplate;
//
//    @MockBean
//    private ValueOperations<String, PatientsResponse> valueOperations;
//
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
//}
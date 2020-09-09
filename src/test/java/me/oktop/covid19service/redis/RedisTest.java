package me.oktop.covid19service.redis;

import me.oktop.covid19service.dto.response.PatientResponse;
import me.oktop.covid19service.dto.response.PatientsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RedisTest {

    @Resource
    private RedisTemplate<String, PatientsResponse> redisRedisTemplate;

    private String key = "today";

    @Test
    @DisplayName("레디스_저장_조회_테스트")
    void redis_save_select_test() {
        //given
        PatientResponse response = PatientResponse.builder()
                .area("지역")
                .totalCount(10000)
                .increaseCount(1001)
                .isolationCount(9001)
                .dischargedCount(3001)
                .deathCount(110)
                .build();

        PatientResponse response2 = PatientResponse.builder()
                .area("지역22")
                .totalCount(100002)
                .increaseCount(1002)
                .isolationCount(9002)
                .dischargedCount(3002)
                .deathCount(112)
                .build();

        List<PatientResponse> list = new ArrayList<>();
        list.add(response);
        list.add(response2);

//        listOperations.rightPushAll(key, list);
        PatientsResponse patientsResponse = new PatientsResponse();
        patientsResponse.setPatientResponses(list);
        ValueOperations<String, PatientsResponse> valueOperations = redisRedisTemplate.opsForValue();
        valueOperations.set(key, patientsResponse);
        //when
        PatientsResponse patientsResponse1 = valueOperations.get(key);
//        ZonedDateTime now = Year.of(2020).atMonth(9).atDay(9).atTime(15, 19)
//                .atZone(ZoneId.of("Asia/Seoul"));
//        redisRedisTemplate.expireAt(key, now.toInstant());
        //then
        assertThat(patientsResponse1.getPatientResponses().get(0).getArea(), is("지역"));
        assertThat(patientsResponse1.getPatientResponses().get(1).getArea(), is("지역22"));
    }

    @Test
    void 키_확인_테스트() {
        //when
        Boolean isExists = redisRedisTemplate.hasKey(key);
        //then
        assertTrue(isExists);
    }

}
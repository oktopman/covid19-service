package me.oktop.covid19service.web.controller;

import me.oktop.covid19service.service.PatientService;
import me.oktop.covid19service.web.dto.response.PatientLatestResponse;
import me.oktop.covid19service.web.dto.response.PatientResponse;
import me.oktop.covid19service.web.dto.response.PatientsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static me.oktop.covid19service.utils.ApiDocumentUtils.getDocumentRequest;
import static me.oktop.covid19service.utils.ApiDocumentUtils.getDocumentResponse;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientBoardController.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class PatientBoardControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PatientService patientService;

    @MockBean
    JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation)).build();
    }

    @Test
    @DisplayName("오늘 환자 현황 조회 테스트")
    void daily_patient_board_select_test() throws Exception {
        //given
        given(patientService.getDailyPatientsBoard()).willReturn(createMockPatientResponse());
        //when
        ResultActions result = mockMvc.perform(
                get("/patients/daily")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                .andDo(print())
                .andExpect(jsonPath("$.data.patientResponses[0].area", is("서울")))
                .andExpect(jsonPath("$.code", is("200")));
        //then
        result.andExpect(status().isOk())
                .andDo(document("daily-select",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("data.patientResponses[0].area").type(JsonFieldType.STRING).description("지역명"),
                                fieldWithPath("data.patientResponses[0].totalCount").type(JsonFieldType.NUMBER).description("누적 확진자 수"),
                                fieldWithPath("data.patientResponses[0].increaseCount").type(JsonFieldType.NUMBER).description("신규 확진자 수"),
                                fieldWithPath("data.patientResponses[0].isolationCount").type(JsonFieldType.NUMBER).description("격리 치료 환자 수"),
                                fieldWithPath("data.patientResponses[0].dischargedCount").type(JsonFieldType.NUMBER).description("퇴원한 사람 수"),
                                fieldWithPath("data.patientResponses[0].deathCount").type(JsonFieldType.NUMBER).description("사망자 수"),
                                fieldWithPath("data.patientResponses[0].occurDate").type(JsonFieldType.STRING).description("환자 발생 날짜")
                        )
                ));
    }

    @Test
    @DisplayName("최근 5일 환자 현황 조회 테스트")
    void latest_patient_board_select_test() throws Exception {
        //given
        given(patientService.getLatestPatients()).willReturn(createMockPatientLatestResponse());
        ResultActions result = mockMvc.perform(
                get("/patients/latest")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                .andDo(print())
                .andExpect(jsonPath("$.data.patientResponses[0].area", is("진주")))
                .andExpect(jsonPath("$.code", is("200")));
        //then
        result.andExpect(status().isOk())
                .andDo(document("latest-select",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("data.patientResponses[0].area").type(JsonFieldType.STRING).description("지역명"),
                                fieldWithPath("data.patientResponses[0].totalCount").type(JsonFieldType.NUMBER).description("누적 확진자 수"),
                                fieldWithPath("data.patientResponses[0].increaseCount").type(JsonFieldType.NUMBER).description("신규 확진자 수"),
                                fieldWithPath("data.patientResponses[0].occurDate").type(JsonFieldType.STRING).description("환자 발생 날짜")
                        )
                ));
    }

    PatientsResponse<PatientResponse> createMockPatientResponse() {
        PatientResponse patientResponse = PatientResponse.builder()
                .area("서울")
                .totalCount(100)
                .increaseCount(100)
                .isolationCount(100)
                .dischargedCount(100)
                .deathCount(0)
                .occurDate("2020-09-14")
                .build();

        List<PatientResponse> list = new ArrayList<>();
        list.add(patientResponse);
        PatientsResponse<PatientResponse> response = new PatientsResponse<>();
        response.setPatientResponses(list);
        return response;
    }

    PatientsResponse<PatientLatestResponse> createMockPatientLatestResponse() {
        PatientLatestResponse patientLatestResponse = PatientLatestResponse.builder()
                .area("진주")
                .totalCount(10)
                .increaseCount(1)
                .occurDate("2020-09-14")
                .build();

        PatientsResponse<PatientLatestResponse> response = new PatientsResponse<>();
        List<PatientLatestResponse> list = new ArrayList<>();
        list.add(patientLatestResponse);
        response.setPatientResponses(list);
        return response;
    }

}
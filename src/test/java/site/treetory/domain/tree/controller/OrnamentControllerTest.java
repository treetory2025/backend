package site.treetory.domain.tree.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import site.treetory.global.config.TestContainersConfig;
import site.treetory.global.security.jwt.JwtProperties;
import site.treetory.global.security.jwt.JwtUtils;
import site.treetory.global.util.CookieUtils;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes.*;
import static site.treetory.global.security.jwt.JwtConstants.ACCESS;
import static site.treetory.global.statuscode.ErrorCode.BAD_REQUEST;
import static site.treetory.global.statuscode.ErrorCode.NOT_FOUND;
import static site.treetory.global.statuscode.SuccessCode.*;
import static site.treetory.utils.ResponseFieldUtils.getCommonResponseFields;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@Import(TestContainersConfig.class)
@ActiveProfiles("test")
public class OrnamentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private CookieUtils cookieUtils;

    private Cookie accessCookie;

    @PostConstruct
    public void init() {

        String uuid = "b8a3eb59-b956-4df9-8a55-80784016b8d4";
        String jti = "50fa530e-f778-4e0b-afa9-53643bc9ea18";

        String accessToken = jwtUtils.createAccessToken(uuid, jti);

        accessCookie = cookieUtils.createCookie(ACCESS, accessToken, "/", jwtProperties.getAccessExpiration());
    }

    @Test
    @DisplayName("오너먼트 조회 성공")
    public void get_ornament_success() throws Exception {

        // given
        String word = "name";
        String category = "CHRISTMAS";
        String page = "0";

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/ornaments")
                        .param("word", word)
                        .param("category", category)
                        .param("page", page)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "오너먼트 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Ornament API")
                                .summary("오너먼트 조회 API")
                                .queryParameters(
                                        parameterWithName("word")
                                                .description("검색어"),
                                        parameterWithName("category")
                                                .description("분류"),
                                        parameterWithName("page")
                                                .description("페이지")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.ornaments").type(OBJECT)
                                                        .description("조회 결과"),
                                                fieldWithPath("body.ornaments.content").type(ARRAY)
                                                        .description("오너먼트 정보"),
                                                fieldWithPath("body.ornaments.content[].ornamentId").type(STRING)
                                                        .description("아이디"),
                                                fieldWithPath("body.ornaments.content[].name").type(STRING)
                                                        .description("이름"),
                                                fieldWithPath("body.ornaments.content[].imgUrl").type(STRING)
                                                        .description("이미지 주소"),
                                                fieldWithPath("body.ornaments.pageNum").type(NUMBER)
                                                        .description("페이지 번호"),
                                                fieldWithPath("body.ornaments.pageSize").type(NUMBER)
                                                        .description("페이지 크기"),
                                                fieldWithPath("body.ornaments.totalPage").type(NUMBER)
                                                        .description("전체 페이지"),
                                                fieldWithPath("body.ornaments.totalElements").type(NUMBER)
                                                        .description("전체 개수")
                                        )
                                )
                                .requestSchema(Schema.schema("오너먼트 조회 Request"))
                                .responseSchema(Schema.schema("오너먼트 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("오너먼트 조회 실패 - 잘못된 카테고리")
    public void get_ornament_fail_wrong_category() throws Exception {

        // given
        String word = "name";
        String category = "KHRISTMAS";
        String page = "0";

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/ornaments")
                        .param("word", word)
                        .param("category", category)
                        .param("page", page)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(BAD_REQUEST.getMessage()))
                .andDo(document(
                        "오너먼트 조회 실패 - 잘못된 카테고리",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Ornament API")
                                .summary("오너먼트 조회 API")
                                .queryParameters(
                                        parameterWithName("word")
                                                .description("검색어"),
                                        parameterWithName("category")
                                                .description("분류"),
                                        parameterWithName("page")
                                                .description("페이지")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("오너먼트 추가 성공")
    public void add_ornament_success() throws Exception {

        // given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "장식");
        jsonObject.put("category", "ETC");
        jsonObject.put("imgUrl", "https://treetory.s3.ap-northeast-2.amazonaws.com/members/b8a3eb59-b956-4df9-8a55-80784016b8d4/ornaments/7ae61503-5ad0-4a82-ba37-fb04ad8b4ed6%3Aupload_2025-12-05-17.05.48.png");
        jsonObject.put("isPublic", "true");

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/ornaments")
                        .cookie(accessCookie)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .characterEncoding("UTF-8")
        );

                // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.header.message").value(CREATED.getMessage()))
                .andDo(document(
                        "오너먼트 추가 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Ornament API")
                                .summary("오너먼트 추가 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("즐겨찾기 추가 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("오너먼트 추가 실패 - 잘못된 이름 형식")
    public void add_ornament_fail_bad_name() throws Exception {

        // given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "장식이름이너무너무너무길어요");
        jsonObject.put("category", "ETC");
        jsonObject.put("imgUrl", "https://treetory.s3.ap-northeast-2.amazonaws.com/members/b8a3eb59-b956-4df9-8a55-80784016b8d4/ornaments/7ae61503-5ad0-4a82-ba37-fb04ad8b4ed6%3Aupload_2025-12-05-17.05.48.png");
        jsonObject.put("isPublic", "true");

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/ornaments")
                        .cookie(accessCookie)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .characterEncoding("UTF-8")
        );

                // then
        actions
                .andExpect(status().isBadRequest())
                .andDo(document(
                        "오너먼트 추가 실패 - 잘못된 이름 형식",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Ornament API")
                                .summary("오너먼트 추가 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("오너먼트 추가 실패 - 잘못된 URL 형식")
    public void add_ornament_fail_wrong_url() throws Exception {

        // given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "장식");
        jsonObject.put("category", "ETC");
        jsonObject.put("imgUrl", "https://ohmygod.it.is-wrongurl-2.hehe.com/members/b8a3eb59-b956-4df9-8a55-80784016b8d4/ornaments/7ae61503-5ad0-4a82-ba37-fb04ad8b4ed6%3Aupload_2025-12-05-17.05.48.png");
        jsonObject.put("isPublic", "true");

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/ornaments")
                        .cookie(accessCookie)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andDo(document(
                        "오너먼트 추가 실패 - 잘못된 URL 형식",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Ornament API")
                                .summary("오너먼트 추가 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .build()
                        ))
                );
    }


    @Test
    @DisplayName("오너먼트 추가 실패 - 잘못된 카테고리")
    public void add_ornament_fail_wrong_category() throws Exception {

        // given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "장식");
        jsonObject.put("category", "ANIMATION");
        jsonObject.put("imgUrl", "https://treetory.s3.ap-northeast-2.amazonaws.com/members/b8a3eb59-b956-4df9-8a55-80784016b8d4/ornaments/7ae61503-5ad0-4a82-ba37-fb04ad8b4ed6%3Aupload_2025-12-05-17.05.48.png");
        jsonObject.put("isPublic", "true");

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/ornaments")
                        .cookie(accessCookie)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .characterEncoding("UTF-8")
        );

                // then
        actions
                .andExpect(status().isBadRequest())
                .andDo(document(
                        "오너먼트 추가 실패 - 잘못된 카테고리",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Ornament API")
                                .summary("오너먼트 추가 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("오너먼트 이름 중복 체크 성공")
    public void check_ornament_name_exists() throws Exception {

        // given
        String name = "새로운장식";

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/ornaments/exists")
                        .cookie(accessCookie)
                        .param("name", name)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "장식 이름 중복 체크 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Ornament API")
                                .summary("장식 이름 중복 체크 API")
                                .queryParameters(
                                        parameterWithName("name")
                                                .description("이름")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.exists").type(BOOLEAN)
                                                        .description("중복 여부")
                                        )
                                )
                                .requestSchema(Schema.schema("오너먼트 이름 중복 체크 Request"))
                                .responseSchema(Schema.schema("오너먼트 이름 중복 체크 Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("오너먼트 상세 조회 성공")
    public void get_ornaments_details_success() throws Exception {

        // given
        Long ornamentId = 1L;

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/ornaments/{ornamentId}", ornamentId)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "오너먼트 상세 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Ornament API")
                                .summary("오너먼트 상세 조회 API")
                                .pathParameters(
                                        parameterWithName("ornamentId")
                                                .description("오너먼트 ID")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.name").type(STRING)
                                                        .description("이름"),
                                                fieldWithPath("body.category").type(STRING)
                                                        .description("분류"),
                                                fieldWithPath("body.imgUrl").type(STRING)
                                                        .description("이미지 url"),
                                                fieldWithPath("body.userNickname").type(STRING)
                                                        .description("등록한 회원 닉네임"),
                                                fieldWithPath("body.createdDate").type(STRING)
                                                        .description("등록 날짜")
                                        )
                                )
                                .requestSchema(Schema.schema("오너먼트 상세 조회 Request"))
                                .responseSchema(Schema.schema("오너먼트 상세 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("오너먼트 상세 조회 실패 - 잘못된 오너먼트 id")
    public void get_ornaments_details_fail_wrong_id() throws Exception {

        // given
        Long ornamentId = 100000001L;

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/ornaments/{ornamentId}",  ornamentId)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "오너먼트 상세 조회 실패 - 잘못된 오너먼트 id",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Ornament API")
                                .summary("오너먼트 상세 조회 API")
                                .pathParameters(
                                        parameterWithName("ornamentId")
                                                .description("오너먼트 ID")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .build()
                        ))
                );
    }
}

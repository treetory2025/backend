package site.treetory.domain.member.controller;

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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes.*;
import static site.treetory.global.security.jwt.JwtConstants.ACCESS;
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
public class MemberControllerTest {

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
    @DisplayName("본인 정보 조회 성공")
    public void member_details_success() throws Exception {

        // given

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/members/me")
                        .cookie(accessCookie)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "본인 정보 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("본인 정보 조회 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.uuid").type(STRING)
                                                        .description("UUID"),
                                                fieldWithPath("body.nickname").type(STRING)
                                                        .description("닉네임"),
                                                fieldWithPath("body.email").type(STRING)
                                                        .description("이메일"),
                                                fieldWithPath("body.theme").type(STRING)
                                                        .description("테마"),
                                                fieldWithPath("body.background").type(STRING)
                                                        .description("배경")
                                        )
                                )
                                .requestSchema(Schema.schema("본인 정보 조회 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("회원 검색 성공")
    public void search_members_success() throws Exception {

        // given
        String query = "test";
        String page = "0";
        String size = "5";

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/members")
                        .param("query", query)
                        .param("page", page)
                        .param("size", size)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "회원 검색 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("회원 검색 API")
                                .queryParameters(
                                        parameterWithName("query")
                                                .description("검색어"),
                                        parameterWithName("page")
                                                .description("페이지 번호 (기본값: 0)").optional(),
                                        parameterWithName("size")
                                                .description("페이지 크기 (기본값: 5)").optional()
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.members").type(OBJECT)
                                                        .description("검색 결과"),
                                                fieldWithPath("body.members.content").type(ARRAY)
                                                        .description("회원 정보"),
                                                fieldWithPath("body.members.content[].memberId").type(STRING)
                                                        .description("아이디"),
                                                fieldWithPath("body.members.content[].nickname").type(STRING)
                                                        .description("닉네임"),
                                                fieldWithPath("body.members.content[].email").type(STRING)
                                                        .description("이메일"),
                                                fieldWithPath("body.members.content[].ornamentsCount").type(NUMBER)
                                                        .description("장식 개수"),
                                                fieldWithPath("body.members.pageNum").type(NUMBER)
                                                        .description("페이지 번호"),
                                                fieldWithPath("body.members.pageSize").type(NUMBER)
                                                        .description("페이지 크기"),
                                                fieldWithPath("body.members.totalPage").type(NUMBER)
                                                        .description("전체 페이지"),
                                                fieldWithPath("body.members.totalElements").type(NUMBER)
                                                        .description("전체 개수")
                                        )
                                )
                                .requestSchema(Schema.schema("회원 검색 Request"))
                                .responseSchema(Schema.schema("회원 검색 Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("회원 검색 실패 - 검색어 없음")
    public void search_members_fail_no_query() throws Exception {

        // given

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/members")
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andDo(document(
                        "회원 검색 실패 - 검색어 없음",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("회원 검색 API")
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
    @DisplayName("닉네임 변경 성공")
    public void change_nickname_success() throws Exception {

        // given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nickname", "새로운닉네임");

        // when
        ResultActions actions = mockMvc.perform(
                patch("/api/members/nicknames")
                        .cookie(accessCookie)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "닉네임 변경 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("닉네임 변경 API")
                                .requestFields(
                                        fieldWithPath("nickname").type(STRING)
                                                .description("변경할 닉네임")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("닉네임 변경 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("닉네임 변경 실패 - 잘못된 닉네임")
    public void change_nickname_fail_wrong_nickname() throws Exception {

        // given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nickname", "새로운~~닉네임");

        // when
        ResultActions actions = mockMvc.perform(
                patch("/api/members/nicknames")
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
                        "닉네임 변경 실패 - 잘못된 닉네임",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("닉네임 변경 API")
                                .requestFields(
                                        fieldWithPath("nickname").type(STRING)
                                                .description("변경할 닉네임")
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
    @DisplayName("즐겨찾기 추가 성공")
    public void add_bookmark_success() throws Exception {

        // given
        String targetMemberId = "7dc08288-d5fb-490d-8ece-9e55e3b7dda4";

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/members/bookmarks/{targetMemberId}", targetMemberId)
                        .cookie(accessCookie)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.header.message").value(CREATED.getMessage()))
                .andDo(document(
                        "즐겨찾기 추가 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("즐겨찾기 추가 API")
                                .pathParameters(
                                        parameterWithName("targetMemberId")
                                                .description("상대 회원 ID")
                                )
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
    @DisplayName("즐겨찾기 추가 실패 - 잘못된 회원 아이디")
    public void add_bookmark_fail_wrong_member_id() throws Exception {

        // given
        String targetMemberId = "7dc08288-d5fb-490d-8ece-000000000000";

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/members/bookmarks/{targetMemberId}", targetMemberId)
                        .cookie(accessCookie)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "즐겨찾기 추가 실패 - 잘못된 회원 아이디",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("즐겨찾기 추가 API")
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
    @DisplayName("즐겨찾기 삭제 성공")
    public void delete_bookmark_success() throws Exception {

        // given
        String targetMemberId = "7dc08288-d5fb-490d-8ece-9e55e3b7dda4";

        // when
        ResultActions actions = mockMvc.perform(
                delete("/api/members/bookmarks/{targetMemberId}", targetMemberId)
                        .cookie(accessCookie)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.header.message").value(NO_CONTENT.getMessage()))
                .andDo(document(
                        "즐겨찾기 삭제 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("즐겨찾기 삭제 API")
                                .requestSchema(Schema.schema("즐겨찾기 삭제 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("즐겨찾기 삭제 실패 - 잘못된 회원 아이디")
    public void delete_bookmark_fail_wrong_member_id() throws Exception {

        // given
        String targetMemberId = "7dc08288-d5fb-490d-8ece-000000000000";

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/members/bookmarks/{targetMemberId}", targetMemberId)
                        .cookie(accessCookie)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "즐겨찾기 삭제 실패 - 잘못된 회원 아이디",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("즐겨찾기 삭제 API")
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
    @DisplayName("즐겨찾기 조회 성공")
    public void bookmark_list_success() throws Exception {

        // given
        String query = "test";
        String page = "0";
        String size = "6";

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/members/bookmarks")
                        .cookie(accessCookie)
                        .param("query", query)
                        .param("page", page)
                        .param("size", size)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "즐겨찾기 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("즐겨찾기 조회 API")
                                .queryParameters(
                                        parameterWithName("query")
                                                .description("검색어").optional(),
                                        parameterWithName("page")
                                                .description("페이지 번호 (기본값: 0)").optional(),
                                        parameterWithName("size")
                                                .description("페이지 크기 (기본값: 6)").optional()
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.members").type(OBJECT)
                                                        .description("검색 결과"),
                                                fieldWithPath("body.members.content").type(ARRAY)
                                                        .description("회원 정보"),
                                                fieldWithPath("body.members.content[].memberId").type(STRING)
                                                        .description("아이디"),
                                                fieldWithPath("body.members.content[].nickname").type(STRING)
                                                        .description("닉네임"),
                                                fieldWithPath("body.members.content[].email").type(STRING)
                                                        .description("이메일"),
                                                fieldWithPath("body.members.pageNum").type(NUMBER)
                                                        .description("페이지 번호"),
                                                fieldWithPath("body.members.pageSize").type(NUMBER)
                                                        .description("페이지 크기"),
                                                fieldWithPath("body.members.totalPage").type(NUMBER)
                                                        .description("전체 페이지"),
                                                fieldWithPath("body.members.totalElements").type(NUMBER)
                                                        .description("전체 개수")
                                        )
                                )
                                .requestSchema(Schema.schema("즐겨찾기 조회 Request"))
                                .responseSchema(Schema.schema("즐겨찾기 조회 Response"))
                                .build()
                        ))
                );
    }
}

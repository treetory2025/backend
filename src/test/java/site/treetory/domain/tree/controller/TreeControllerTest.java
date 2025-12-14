package site.treetory.domain.tree.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.time.LocalDateTime;

import static org.mockito.Mockito.mockStatic;
import static org.testcontainers.shaded.com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes.*;
import static site.treetory.global.statuscode.ErrorCode.*;
import static site.treetory.utils.ResponseFieldUtils.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static site.treetory.global.statuscode.SuccessCode.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static site.treetory.global.security.jwt.JwtConstants.ACCESS;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(MockitoExtension.class)
@ExtendWith(RestDocumentationExtension.class)
@Import(TestContainersConfig.class)
@ActiveProfiles("test")
public class TreeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private CookieUtils cookieUtils;

    private Cookie accessCookie, accessCookie2;

    @PostConstruct
    public void init() {

        String uuid = "b8a3eb59-b956-4df9-8a55-80784016b8d4";
        String jti = "50fa530e-f778-4e0b-afa9-53643bc9ea18";

        String accessToken = jwtUtils.createAccessToken(uuid, jti);

        accessCookie = cookieUtils.createCookie(ACCESS, accessToken, "/", jwtProperties.getAccessExpiration());

        String uuid2 = "7dc08288-d5fb-490d-8ece-9e55e3b7dda4";
        String jti2 = "966921cf-655b-4840-a068-4ba9d9bc8e4a";

        String accessToken2 = jwtUtils.createAccessToken(uuid2, jti2);

        accessCookie2 = cookieUtils.createCookie(ACCESS, accessToken2, "/", jwtProperties.getAccessExpiration());

    }

    @Test
    @DisplayName("트리 조회 성공")
    public void tree_details_success() throws Exception {

        // given
        String uuid = "b8a3eb59-b956-4df9-8a55-80784016b8d4";

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/trees/{uuid}", uuid)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "트리 조회",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("트리 조회 API")
                                .pathParameters(
                                        parameterWithName("uuid")
                                                .description("UUID")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.nickname").type(STRING)
                                                        .description("닉네임"),
                                                fieldWithPath("body.isBookmarked").type(BOOLEAN)
                                                        .description("즐겨찾기 여부"),
                                                fieldWithPath("body.treeSize").type(NUMBER)
                                                        .description("트리 크기"),
                                                fieldWithPath("body.treeTheme").type(STRING)
                                                        .description("트리 테마"),
                                                fieldWithPath("body.treeBackground").type(STRING)
                                                        .description("트리 배경"),
                                                fieldWithPath("body.ornamentsRes").type(ARRAY)
                                                        .description("배치된 오너먼트 리스트"),
                                                fieldWithPath("body.ornamentsRes[].placedOrnamentId").type(NUMBER)
                                                        .description("배치된 오너먼트 ID"),
                                                fieldWithPath("body.ornamentsRes[].ornamentId").type(NUMBER)
                                                        .description("오너먼트 ID"),
                                                fieldWithPath("body.ornamentsRes[].writerNickname").type(STRING)
                                                        .description("작성자 닉네임"),
                                                fieldWithPath("body.ornamentsRes[].positionX").type(NUMBER)
                                                        .description("X좌표"),
                                                fieldWithPath("body.ornamentsRes[].positionY").type(NUMBER)
                                                        .description("Y좌표"),
                                                fieldWithPath("body.ornamentsRes[].size").type(STRING)
                                                        .description("오너먼트 크기"),
                                                fieldWithPath("body.ornamentsRes[].imgUrl").type(NUMBER)
                                                        .description("이미지 url"),
                                                fieldWithPath("body.ornamentsRes[].createdDate").type(NUMBER)
                                                        .description("등록 날짜")
                                        )
                                )
                                .requestSchema(Schema.schema("트리 조회 Request"))
                                .responseSchema(Schema.schema("트리 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("트리 조회 실패 - 존재하지 않는 uuid")
    public void tree_details_fail_wrong_uuid() throws Exception {

        // given
        String uuid = "ohmygodi-cant-find-this-tree4016b8d4";

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/trees/{uuid}", uuid)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "트리 조회 실패",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("트리 조회 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("트리 조회 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("트리 사이즈 증가 성공")
    public void resize_tree_success() throws Exception {

        // given

        // when
        ResultActions actions = mockMvc.perform(
                patch("/api/trees/size")
                        .cookie(accessCookie)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "트리 사이즈 증가 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("트리 사이즈 증가 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("트리 사이즈 증가 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("트리 사이즈 증가 실패 - 권한 없음")
    public void resize_tree_fail_unauthorized() throws Exception {

        // given

        // when
        ResultActions actions = mockMvc.perform(
                patch("/api/trees/size")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.header.message").value(UNAUTHORIZED.getMessage()))
                .andDo(document(
                        "트리 사이즈 증가 실패 - 권한 없음",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("트리 사이즈 증가 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("트리 사이즈 증가 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("트리 사이즈 증가 실패 - 최대 크기 도달")
    public void resize_tree_fail_max_size() throws Exception {

        // given

        // when
        ResultActions actions = mockMvc.perform(
                patch("/api/trees/size")
                        .cookie(accessCookie2)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(BAD_REQUEST.getMessage()))
                .andDo(document(
                        "트리 사이즈 증가 실패 - 최대 크기 도달",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("트리 사이즈 증가 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("트리 사이즈 증가 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("오너먼트 배치 성공")
    public void place_ornament_success() throws Exception {

        // given
        String uuid = "b8a3eb59-b956-4df9-8a55-80784016b8d4";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ornamentId", "1");
        jsonObject.put("nickname", "테스트방문자");
        jsonObject.put("message", "너를처음본순간부터좋아했어방학전에고백하고싶었는데바보같이그땐용기가없더라지금은이수많은사람들앞에서오로지너만사랑한다고말하고싶어서큰마음먹고용기내어봐매일매일버스에서너볼때마다두근댔고동아리랑과활동에서도너만보이고너생각만나고지난3월부터계속그랬어니가남자친구랑헤어지고니맘이아파울때내마음도너무아팠지만내심좋은맘두있었어이런내맘을어떻게말할지고민하다가정말인생에서제일크게용기내어세상에서제일멋지게많은사람들앞에서너한테고백해주고싶었어나만의태양이되어줄래?난너의달님이될게내일3시반에너수업마치고학관앞에서기다리고있을게");
        jsonObject.put("positionX", "100");
        jsonObject.put("positionY", "100");
        jsonObject.put("font", "NANUM_PEN");
        
        // when
        ResultActions actions = mockMvc.perform(
                post("/api/trees/{uuid}/ornaments", uuid)
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
                        "오너먼트 배치 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("오너먼트 배치 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("오너먼트 배치 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("오너먼트 배치 실패 - 잘못된 uuid")
    public void place_ornament_fail_wrong_uuid() throws Exception {

        // given
        String uuid = "ohmygodi-cant-find-this-treebyuuid!!";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ornamentId", "1");
        jsonObject.put("nickname", "테스트방문자");
        jsonObject.put("message", "너를처음본순간부터좋아했어방학전에고백하고싶었는데바보같이그땐용기가없더라지금은이수많은사람들앞에서오로지너만사랑한다고말하고싶어서큰마음먹고용기내어봐매일매일버스에서너볼때마다두근댔고동아리랑과활동에서도너만보이고너생각만나고지난3월부터계속그랬어니가남자친구랑헤어지고니맘이아파울때내마음도너무아팠지만내심좋은맘두있었어이런내맘을어떻게말할지고민하다가정말인생에서제일크게용기내어세상에서제일멋지게많은사람들앞에서너한테고백해주고싶었어나만의태양이되어줄래?난너의달님이될게내일3시반에너수업마치고학관앞에서기다리고있을게");
        jsonObject.put("positionX", "100");
        jsonObject.put("positionY", "100");
        jsonObject.put("font", "NANUM_PEN");

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/trees/{uuid}/ornaments", uuid)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "오너먼트 배치 실패 - 잘못된 uuid",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("오너먼트 배치 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("오너먼트 배치 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("오너먼트 배치 실패 - 존재하지 않는 오너먼트")
    public void place_ornament_fail_ornament_not_found() throws Exception {

        // given
        String uuid = "b8a3eb59-b956-4df9-8a55-80784016b8d4";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ornamentId", "999");
        jsonObject.put("nickname", "테스트방문자");
        jsonObject.put("message", "너를처음본순간부터좋아했어방학전에고백하고싶었는데바보같이그땐용기가없더라지금은이수많은사람들앞에서오로지너만사랑한다고말하고싶어서큰마음먹고용기내어봐매일매일버스에서너볼때마다두근댔고동아리랑과활동에서도너만보이고너생각만나고지난3월부터계속그랬어니가남자친구랑헤어지고니맘이아파울때내마음도너무아팠지만내심좋은맘두있었어이런내맘을어떻게말할지고민하다가정말인생에서제일크게용기내어세상에서제일멋지게많은사람들앞에서너한테고백해주고싶었어나만의태양이되어줄래?난너의달님이될게내일3시반에너수업마치고학관앞에서기다리고있을게");
        jsonObject.put("positionX", "100");
        jsonObject.put("positionY", "100");
        jsonObject.put("font", "NANUM_PEN");

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/trees/{uuid}/ornaments", uuid)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "오너먼트 배치 실패 - 존재하지 않는 오너먼트",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("오너먼트 배치 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("오너먼트 배치 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("오너먼트 배치 실패 - 존재하지 않는 폰트")
    public void place_ornament_fail_wrong_font() throws Exception {

        // given
        String uuid = "b8a3eb59-b956-4df9-8a55-80784016b8d4";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ornamentId", "1");
        jsonObject.put("nickname", "테스트방문자");
        jsonObject.put("message", "너를처음본순간부터좋아했어방학전에고백하고싶었는데바보같이그땐용기가없더라지금은이수많은사람들앞에서오로지너만사랑한다고말하고싶어서큰마음먹고용기내어봐매일매일버스에서너볼때마다두근댔고동아리랑과활동에서도너만보이고너생각만나고지난3월부터계속그랬어니가남자친구랑헤어지고니맘이아파울때내마음도너무아팠지만내심좋은맘두있었어이런내맘을어떻게말할지고민하다가정말인생에서제일크게용기내어세상에서제일멋지게많은사람들앞에서너한테고백해주고싶었어나만의태양이되어줄래?난너의달님이될게내일3시반에너수업마치고학관앞에서기다리고있을게");
        jsonObject.put("positionX", "100");
        jsonObject.put("positionY", "100");
        jsonObject.put("font", "NANUM_PENS");

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/trees/{uuid}/ornaments", uuid)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(BAD_REQUEST.getMessage()))
                .andDo(document(
                        "오너먼트 배치 실패 - 존재하지 않는 폰트",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("오너먼트 배치 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("오너먼트 배치 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("오너먼트 배치 실패 - 메세지 300자 초과")
    public void place_ornament_fail_message_too_long() throws Exception {

        // given
        String uuid = "b8a3eb59-b956-4df9-8a55-80784016b8d4";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ornamentId", "1");
        jsonObject.put("nickname", "테스트방문자");
        jsonObject.put("message", "어려분 단체방에 죄송하지만 글 하나만 젇겠습니다.너를처음본순간부터좋아했어방학전에고백하고싶었는데바보같이그땐용기가없더라지금은이수많은사람들앞에서오로지너만사랑한다고말하고싶어서큰마음먹고용기내어봐매일매일버스에서너볼때마다두근댔고동아리랑과활동에서도너만보이고너생각만나고지난3월부터계속그랬어니가남자친구랑헤어지고니맘이아파울때내마음도너무아팠지만내심좋은맘두있었어이런내맘을어떻게말할지고민하다가정말인생에서제일크게용기내어세상에서제일멋지게많은사람들앞에서너한테고백해주고싶었어나만의태양이되어줄래?난너의달님이될게내일3시반에너수업마치고학관앞에서기다리고있을게.이제 누가 공지해주냐");
        jsonObject.put("positionX", "100");
        jsonObject.put("positionY", "100");
        jsonObject.put("font", "NANUM_PEN");

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/trees/{uuid}/ornaments", uuid)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andDo(document(
                        "오너먼트 배치 실패 - 메세지 300자 초과",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("오너먼트 배치 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("오너먼트 배치 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("오너먼트 배치 실패 - 닉네임 유효성 검사 실패")
    public void place_ornament_fail_nickname_not_valid() throws Exception {

        // given
        String uuid = "b8a3eb59-b956-4df9-8a55-80784016b8d4";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ornamentId", "1");
        jsonObject.put("nickname", "テスト訪問者");
        jsonObject.put("message", "너를처음본순간부터좋아했어방학전에고백하고싶었는데바보같이그땐용기가없더라지금은이수많은사람들앞에서오로지너만사랑한다고말하고싶어서큰마음먹고용기내어봐매일매일버스에서너볼때마다두근댔고동아리랑과활동에서도너만보이고너생각만나고지난3월부터계속그랬어니가남자친구랑헤어지고니맘이아파울때내마음도너무아팠지만내심좋은맘두있었어이런내맘을어떻게말할지고민하다가정말인생에서제일크게용기내어세상에서제일멋지게많은사람들앞에서너한테고백해주고싶었어나만의태양이되어줄래?난너의달님이될게내일3시반에너수업마치고학관앞에서기다리고있을게");
        jsonObject.put("positionX", "100");
        jsonObject.put("positionY", "100");
        jsonObject.put("font", "NANUM_PEN");

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/trees/{uuid}/ornaments", uuid)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andDo(document(
                        "오너먼트 배치 실패 - 닉네임 유효성 검사 실패",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("오너먼트 배치 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("오너먼트 배치 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("오너먼트 배치 실패 - 겹침")
    public void place_ornament_fail_intersection() throws Exception {

        // given
        String uuid = "b8a3eb59-b956-4df9-8a55-80784016b8d4";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ornamentId", "1");
        jsonObject.put("nickname", "테스트방문자");
        jsonObject.put("message", "너를처음본순간부터좋아했어방학전에고백하고싶었는데바보같이그땐용기가없더라지금은이수많은사람들앞에서오로지너만사랑한다고말하고싶어서큰마음먹고용기내어봐매일매일버스에서너볼때마다두근댔고동아리랑과활동에서도너만보이고너생각만나고지난3월부터계속그랬어니가남자친구랑헤어지고니맘이아파울때내마음도너무아팠지만내심좋은맘두있었어이런내맘을어떻게말할지고민하다가정말인생에서제일크게용기내어세상에서제일멋지게많은사람들앞에서너한테고백해주고싶었어나만의태양이되어줄래?난너의달님이될게내일3시반에너수업마치고학관앞에서기다리고있을게");
        jsonObject.put("positionX", "1");
        jsonObject.put("positionY", "1");
        jsonObject.put("font", "NANUM_PEN");

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/trees/{uuid}/ornaments", uuid)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(BAD_REQUEST.getMessage()))
                .andDo(document(
                        "오너먼트 배치 실패 - 겹침",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("오너먼트 배치 API")
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
    @DisplayName("배치된 오너먼트 삭제 성공")
    public void delete_placed_ornament_success() throws Exception {

        // given
        String placedOrnamentId = "1";

        // when
        ResultActions actions = mockMvc.perform(
                delete("/api/trees/ornaments/{placedOrnamentId}", placedOrnamentId)
                        .cookie(accessCookie)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions.andExpect(status().isNoContent())
                .andExpect(jsonPath("$.header.message").value(NO_CONTENT.getMessage()))
                .andDo(document(
                        "배치된 오너먼트 삭제 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("배치된 오너먼트 삭제 API")
                                .pathParameters(
                                        parameterWithName("placedOrnamentId")
                                                .description("배치된 오너먼트 ID")
                                )
                                .requestSchema(Schema.schema("배치된 오너먼트 삭제 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("배치된 오너먼트 삭제 실패 - 타인의 오너먼트")
    public void delete_placed_ornament_fail_forbidden() throws Exception {

        // given
        String placedOrnamentId = "2";

        // when
        ResultActions actions = mockMvc.perform(
                delete("/api/trees/ornaments/{placedOrnamentId}", placedOrnamentId)
                        .cookie(accessCookie)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.header.message").value(FORBIDDEN.getMessage()))
                .andDo(document(
                        "배치된 오너먼트 삭제 실패 - 타인의 오너먼트",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("배치된 오너먼트 삭제 API")
                                .pathParameters(
                                        parameterWithName("placedOrnamentId")
                                                .description("배치된 오너먼트 ID")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("배치된 오너먼트 삭제 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("테마 변경 성공")
    public void change_theme_success() throws Exception {

        // given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("theme", "SNOWY");

        // when
        ResultActions actions = mockMvc.perform(
                patch("/api/trees/themes")
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
                        "테마 변경 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("테마 변경 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("테마 변경 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("테마 변경 실패 - 존재하지 않는 테마")
    public void change_theme_fail_wrong_theme() throws Exception {

        // given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("theme", "THEREARENOTHEMELIEKTHIS");

        // when
        ResultActions actions = mockMvc.perform(
                patch("/api/trees/themes")
                        .cookie(accessCookie)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(BAD_REQUEST.getMessage()))
                .andDo(document(
                        "테마 변경 실패 - 존재하지 않는 테마",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("테마 변경 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("테마 변경 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("배경 변경 성공")
    public void change_background_success() throws Exception {

        // given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("background", "SILENT_NIGHT");

        // when
        ResultActions actions = mockMvc.perform(
                patch("/api/trees/backgrounds")
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
                        "배경 변경 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("배경 변경 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("배경 변경 Request"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("배경 변경 실패 - 존재하지 않는 배경")
    public void change_background_fail_wrong_background() throws Exception {

        // given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("background", "THEREARENOBACKGROUNDLIKETHIS");

        // when
        ResultActions actions = mockMvc.perform(
                patch("/api/trees/backgrounds")
                        .cookie(accessCookie)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(BAD_REQUEST.getMessage()))
                .andDo(document(
                        "배경 변경 실패 - 존재하지 않는 배경",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("배경 변경 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("배경 변경 Request"))
                                .build()
                        ))
                );
    }
    
    @Test
    @DisplayName("메세지 조회 성공")
    public void message_details_success() throws Exception {

        // given
        String placedOrnamentId = "1";

        LocalDateTime afterChristmas = LocalDateTime.of(2025, 12, 25, 0, 1);

        MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
        mockedLocalDateTime.when(LocalDateTime::now).thenReturn(afterChristmas);

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/trees/messages/{placedOrnamentId}", placedOrnamentId)
                        .cookie(accessCookie)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "메시지 조회",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("메시지 조회 API")
                                .pathParameters(
                                        parameterWithName("placedOrnamentId")
                                                .description("배치된 오너먼트 ID")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.font").type(STRING)
                                                        .description("폰트"),
                                                fieldWithPath("body.writer").type(STRING)
                                                        .description("작성자"),
                                                fieldWithPath("body.message").type(STRING)
                                                        .description("메세지"),
                                                fieldWithPath("body.createdDate").type(STRING)
                                                        .description("등록 날짜")
                                        )
                                )
                                .requestSchema(Schema.schema("메시지 조회 Request"))
                                .responseSchema(Schema.schema("메시지 조회 Response"))
                                .build()
                        ))
                );
        
        mockedLocalDateTime.close();
    }

    @Test
    @DisplayName("메세지 조회 실패 - 크리스마스 이전")
    public void message_details_fail_before_christmas() throws Exception {

        // given
        String placedOrnamentId = "1";

        LocalDateTime afterChristmas = LocalDateTime.of(2025, 12, 24, 23, 59);

        MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
        mockedLocalDateTime.when(LocalDateTime::now).thenReturn(afterChristmas);

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/trees/messages/{placedOrnamentId}", placedOrnamentId)
                        .cookie(accessCookie)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.header.message").value(FORBIDDEN.getMessage()))
                .andDo(document(
                        "메세지 조회 실패 - 크리스마스 이전",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("메세지 조회 API")
                                .pathParameters(
                                        parameterWithName("placedOrnamentId")
                                                .description("배치된 오너먼트 ID")
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

        mockedLocalDateTime.close();
    }

    @Test
    @DisplayName("메세지 조회 실패 - 권한 없음")
    public void message_details_fail_unauthorized() throws Exception {

        // given
        String placedOrnamentId = "2";

        LocalDateTime afterChristmas = LocalDateTime.of(2025, 12, 25, 0, 1);

        MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
        mockedLocalDateTime.when(LocalDateTime::now).thenReturn(afterChristmas);

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/trees/messages/{placedOrnamentId}", placedOrnamentId)
                        .cookie(accessCookie)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.header.message").value(FORBIDDEN.getMessage()))
                .andDo(document(
                        "메세지 조회 실패 - 권한 없음",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Tree API")
                                .summary("메세지 조회 API")
                                .pathParameters(
                                        parameterWithName("placedOrnamentId")
                                                .description("배치된 오너먼트 ID")
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

        mockedLocalDateTime.close();
    }

}

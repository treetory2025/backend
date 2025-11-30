package site.treetory.global.security.response;

import site.treetory.domain.member.entity.Member;

import java.util.UUID;

import static site.treetory.domain.member.enums.Role.ROLE_USER;


public interface OAuth2Response {

    String getEmail();

    String getNickname();

    default Member toEntity() {
        return Member.builder()
                .uuid(UUID.randomUUID().toString())
                .email(getEmail())
                .nickname(getNickname())
                .role(ROLE_USER)
                .build();
    }
}

package site.treetory.global.security.oauth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.member.repository.MemberRepository;
import site.treetory.domain.tree.entity.Tree;
import site.treetory.domain.tree.repository.TreeRepository;
import site.treetory.global.security.response.GoogleResponse;
import site.treetory.global.security.response.KakaoResponse;
import site.treetory.global.security.response.OAuth2Response;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final TreeRepository treeRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registration = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;

        switch (registration) {
            case "google" -> oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
            case "kakao" -> oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }

        if (oAuth2Response == null) return null;

        String email = oAuth2Response.getEmail();

        Member member = memberRepository.findByEmail(email).orElseGet(oAuth2Response::toEntity);
        memberRepository.save(member);

        treeRepository.save(Tree.createBasicTree(member));

        return new CustomOAuth2User(member, oAuth2User);
    }
}

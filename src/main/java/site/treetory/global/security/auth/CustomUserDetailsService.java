package site.treetory.global.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.member.repository.MemberRepository;

import java.util.Collections;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) {

        Optional<Member> member = memberRepository.findByUuid(memberId);

        return member.map(value ->
                new CustomUserDetails(value, Collections.singletonList(value.getRole()))).orElse(null);
    }

}

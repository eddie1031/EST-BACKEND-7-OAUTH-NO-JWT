package com.est.oauth2.service;

import com.est.oauth2.dao.MemberRepository;
import com.est.oauth2.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 어떤 데이터가 들어오는지 확인!
        log.info("oAuth2User = {}", oAuth2User);

        // 데이터베이스에 데이터 추가 여부만 확인!
        Member member = Member.builder()
                .name("USER1")
                .email("user1@email.com")
                .nickname("USER1")
                .build();

        memberRepository.save(member);

        return oAuth2User;
    }

}

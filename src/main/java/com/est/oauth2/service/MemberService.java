package com.est.oauth2.service;

import com.est.oauth2.dao.MemberRepository;
import com.est.oauth2.domain.Member;
import com.est.oauth2.dto.MemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        MemberDetails memberDetails = MemberDetailsFactory.memberDetails(provider, oAuth2User);

        Optional<Member> memberOptional = memberRepository.findByEmail(memberDetails.getEmail());
        Member findMember = memberOptional.orElseGet(
                () -> {
                    Member member = Member.builder()
                            .email(memberDetails.getEmail())
                            .name(memberDetails.getName())
                            .nickname("USER")
                            .provider(provider)
                            .build();
                    return memberRepository.save(member);
                }
        );

        if ( findMember.getProvider().equals(provider) ) {
            memberDetails.setRole(findMember.getRole());
            return memberDetails;
        } else {
            throw new RuntimeException();
        }
    }

}

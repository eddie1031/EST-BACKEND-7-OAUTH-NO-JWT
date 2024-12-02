package com.est.oauth2.service;


import com.est.oauth2.dto.MemberDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class MemberDetailsFactory {

    public static MemberDetails memberDetails(
            String provider, OAuth2User oauth2User
    ) {

        Map<String, Object> attributes = oauth2User.getAttributes();

        switch (provider) {
            case "GOOGLE" -> {
                return MemberDetails.builder()
                        .name(attributes.get("family_name").toString())
                        .email(attributes.get("email").toString())
                        .attributes(attributes)
                        .build();
            }
            case "KAKAO" -> {
                Map<String, String> properties = (Map<String, String>) attributes.get("properties");
                return MemberDetails.builder()
                        .name(properties.get("nickname"))
                        .email(attributes.get("id").toString() + "@kakao.com")
                        .attributes(attributes)
                        .build();
            }
            case "NAVER" -> {
                Map<String, String> properties = (Map<String, String>) attributes.get("response");
                return MemberDetails.builder()
                        .name(properties.get("name"))
                        .email(properties.get("email"))
                        .attributes(attributes)
                        .build();
            }
            default -> throw new IllegalArgumentException("Unknown provider: " + provider);
        }


    }

}

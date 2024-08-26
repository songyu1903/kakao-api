package com.example.snstest.service;

import com.example.snstest.provider.AuthProvider;
import com.example.snstest.resp.KakaoMemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthProvider authProvider;

    public String getkakaoLoginURI(){
        return authProvider.getLocation();
    }
    public Long getMemberIdByKakaoInfo(String code){
        String accessToken = authProvider.getAccessTokenResponse(code)
                .getAccessToken();

        KakaoMemberInfoResponse infoResp = authProvider.getInfoResponse(accessToken);
        String kakaoId = infoResp.getId();
        System.out.println("kakaoId = " + kakaoId);

        String nickname = infoResp.getProperties().getNickname();
        System.out.println("nickname = " + nickname);

        // 우리 db에 저장된 회원 중 카카오 식별자가 위에서 받아온 정보와 일치하는 회원이 존재하는지
        // 조회하고 있으면 해당 회원의 MemberId를 반환,
        // 없으면 지금 가입처리를 한 후 가입한 회원의 MemberId를 반환
        return 1L;
    }
}

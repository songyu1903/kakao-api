package com.example.snstest.provider;

import com.example.snstest.resp.KakaoAccessTokenResponse;
import com.example.snstest.resp.KakaoMemberInfoResponse;
import com.example.snstest.resp.KakaoProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class AuthProvider{
    @Value("${auth.kakao.client-id}")
    private String kakaoClientId;
    @Value("${auto.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    private static final String Authorization = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String GRANT_TYPE = "authorization_code";

    public String getLocation(){
        WebClient wc = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .build();
        ClientResponse response = wc.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth/authorize")
                        .queryParam("response_type", "code")
                        .queryParam("client_id", this.kakaoClientId)
                        .queryParam("redirect_uri", this.kakaoRedirectUri)
                        .build())
                // 응답을 Mono 타입으로 받는다
                .exchangeToMono(clientResponse ->  // exchangeToMono()는 응답을 어떻게 받을지 설정
                        Mono.just(clientResponse)) // Mono 타입으로 변환
                .block();

        System.out.println("response = " + response);
        System.out.println(response.statusCode());

        URI location = response.headers().asHttpHeaders().getLocation();
        System.out.println("location = " + location);

        return location.toString();
    }

    public KakaoAccessTokenResponse getAccessTokenResponse(String code){
        WebClient wc = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .build();
        KakaoAccessTokenResponse tokenResponse = wc.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth/token")
                        .build())
                .headers(httpHeaders ->
                        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                .body(BodyInserters.fromFormData("grant_type", GRANT_TYPE)
                        .with("client_id", this.kakaoClientId)
                        .with("redirect_uri", this.kakaoRedirectUri)
                        .with("code", code)
                ).retrieve()
                .bodyToMono(KakaoAccessTokenResponse.class)
                .block();

        System.out.println("tokenResponse = " + tokenResponse);

        return tokenResponse;
    }

    public KakaoMemberInfoResponse getInfoResponse(String accessToken){
        WebClient wc = WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .build();
        KakaoMemberInfoResponse infoResponse = wc.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/user/me")
                        .build())
                .headers(httpHeaders -> {
                    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    httpHeaders.set(Authorization, BEARER_PREFIX + accessToken);
                }).retrieve()
                .bodyToMono(KakaoMemberInfoResponse.class)
                .block();
        System.out.println("infoResponse = " + infoResponse);

        return infoResponse;
    }

}

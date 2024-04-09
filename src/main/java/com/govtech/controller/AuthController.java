package com.govtech.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.govtech.dtos.TokenDto;
import com.govtech.dtos.UrlDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;

@RestController
public class AuthController {
    @Value("${spring.security.oauth2.resource-server.opaque-token.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.resource-server.opaque-token.clientSecret}")
    private String clientSecret;


    @GetMapping("/auth/url")
    public ResponseEntity<UrlDto> auth() {
        //This will generate a link where the user can see the login form of google.
        System.out.println("in /auth/url");
        String url = new GoogleAuthorizationCodeRequestUrl(
                clientId,
                //this is callback url for calling frontend
                "http://localhost:4200",
                Arrays.asList("email", "profile", "openid")
        ).build();
        return ResponseEntity.ok(new UrlDto(url));
    }

    //When Google redirects to the frontend it sends a code with the URL. We need this code
    //in the backend to obtain the access token. So we create a new endpoint to get this code and handle.
    @GetMapping("/auth/callback")
    public ResponseEntity<TokenDto> callback(@RequestParam("code") String code) {
        String token;
        System.out.println("in call back");
        System.out.println(code);

        try {
            token = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    new GsonFactory(),
                    clientId,
                    clientSecret,
                    code,
                    "http://localhost:4200"
            ).execute().getAccessToken();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //This token is the one sent back to the frontend ,and frontend will use it in authorization header
        return ResponseEntity.ok(new TokenDto(token));
    }


}

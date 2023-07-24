package com.ltj.myboard.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TokenResponseDTO {
    private String accessToken;
    private long accessTokenExpirationTime;
}

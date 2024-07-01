package com.hamza.spring.myblog.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtAuthResponseDto {
    private String token;
    private String type = "Bearer";

    public JwtAuthResponseDto(String token) {
        this.token = token;
    }

}

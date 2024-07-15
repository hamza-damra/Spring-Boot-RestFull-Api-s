package com.hamza.spring.myblog.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {
    private Long id;
    private String url;

    public ImageDto(String url) {
        this.url = url;
    }
}

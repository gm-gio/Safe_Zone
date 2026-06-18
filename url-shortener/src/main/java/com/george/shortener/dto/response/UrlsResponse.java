package com.george.shortener.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
@Data
@Builder
@AllArgsConstructor
public class UrlsResponse {
    private Long urlId;
    private Map<String, String> urlOptionMap;
}

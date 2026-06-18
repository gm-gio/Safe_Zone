package com.george.clients.urlShortener;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
@Data
@Builder
public class UrlsResponse {
    private Long urlId;
    private Map<String, String> urlOptionMap;
}

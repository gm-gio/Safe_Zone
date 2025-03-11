package com.george.template.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateRequest {
    private String title;
    private String content;
}

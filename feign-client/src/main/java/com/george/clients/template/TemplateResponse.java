package com.george.clients.template;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateResponse {
    private Long templateId;
    private String title;
    private String content;
}

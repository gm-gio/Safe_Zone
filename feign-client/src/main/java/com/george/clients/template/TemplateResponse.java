package com.george.clients.template;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TemplateResponse {
    private Long templateId;
    private String title;
    private String content;
}

package com.george.template.service;

import com.george.template.dto.TemplateRequest;
import com.george.template.dto.TemplateResponse;
import org.springframework.transaction.annotation.Transactional;

public interface TemplateService {
    TemplateResponse create(TemplateRequest request);

    @Transactional(readOnly = true)
    TemplateResponse getById(Long templateId);

    void deleteById(Long templateId);

    TemplateResponse update(Long templateId, TemplateRequest request);
}

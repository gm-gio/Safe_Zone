package com.george.service.impl;

import com.george.dto.TemplateRequest;
import com.george.dto.TemplateResponse;
import com.george.entity.Template;
import com.george.exception.TemplateCreationException;
import com.george.exception.TemplateNotFoundException;
import com.george.exception.TemplateTitleAlreadyExistsException;
import com.george.mapper.TemplateMapper;
import com.george.repository.TemplateRepository;
import com.george.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final TemplateMapper mapper;

    @Override
    public TemplateResponse create(TemplateRequest request) {
        if (templateRepository.existsTemplateByTitle(request.getTitle())) {
            throw new TemplateTitleAlreadyExistsException(
                    "template.title_already_exists"
            );
        }
        return Optional.of(request)
                .map(mapper::mapToEntity)
                .map(templateRepository::save)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new TemplateCreationException(
                        "Failed to create template. Request might be invalid."
                ));

    }

    @Override
    public TemplateResponse getById(Long templateId) {
        return templateRepository.findById(templateId)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new TemplateNotFoundException(
                        "Template with ID " + templateId + " not found"
                ));
    }

    @Override
    public void deleteById(Long templateId) {
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new TemplateNotFoundException("Template with ID " + templateId + " not found" ));

        templateRepository.delete(template);
    }

    @Override
    public TemplateResponse update(Long templateId, TemplateRequest request) {
        Template existingTemplate = templateRepository.findById(templateId)
                .orElseThrow(() -> new TemplateNotFoundException(
                        "Template with ID " + templateId + " not found"
                ));

        existingTemplate.setTitle(request.getTitle());
        existingTemplate.setContent(request.getContent());

        Template updatedTemplate = templateRepository.save(existingTemplate);
        return mapper.mapToResponse(updatedTemplate);
    }
}

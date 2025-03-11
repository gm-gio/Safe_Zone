package com.george.template.mapper;


import com.george.template.dto.TemplateRequest;
import com.george.template.dto.TemplateResponse;
import com.george.template.entity.Template;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TemplateMapper {

    TemplateResponse mapToResponse(Template template);

    @Mapping(target = "templateId", ignore = true)
    Template mapToEntity (TemplateRequest request);
}

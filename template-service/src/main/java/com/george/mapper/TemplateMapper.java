package com.george.mapper;


import com.george.dto.TemplateRequest;
import com.george.dto.TemplateResponse;
import com.george.entity.Template;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TemplateMapper {

    TemplateResponse mapToResponse(Template template);

    @Mapping(target = "templateId", ignore = true)
    Template mapToEntity (TemplateRequest request);
}

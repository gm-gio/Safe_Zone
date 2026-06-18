package com.george.shortener.mapper;

import com.george.shortener.dto.request.NotificationOptionsRequest;
import com.george.shortener.entity.Response;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResponseMapper {
    Response mapToResponse(NotificationOptionsRequest request);
}

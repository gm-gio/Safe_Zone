package com.george.group.mapper;

import com.george.group.dto.GroupRequest;
import com.george.group.dto.GroupResponse;
import com.george.group.entity.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupResponse toResponse(Group group);

    Group toEntity(GroupRequest groupRequest);
}

package com.george.group.mapper;

import com.george.group.dto.GroupRequest;
import com.george.group.dto.GroupResponse;
import com.george.group.entity.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupResponse mapToResponse(Group group);

    @Mapping(target = "groupId", ignore = true)
    Group mapToEntity(GroupRequest groupRequest);
}

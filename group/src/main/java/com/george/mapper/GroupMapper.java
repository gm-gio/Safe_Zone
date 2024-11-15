package com.george.mapper;

import com.george.dto.GroupRequest;
import com.george.dto.GroupResponse;
import com.george.group.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupResponse toResponse(Group group);
    Group toEntity(GroupResponse groupResponse);

    GroupRequest toRequest(Group group);
    Group toEntity(GroupRequest groupRequest);
}

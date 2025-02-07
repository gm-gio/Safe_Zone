package com.george.group.service.impl;

import com.george.group.dto.GroupRequest;
import com.george.group.dto.GroupResponse;
import com.george.group.entity.Group;
import com.george.group.exception.GroupNotFoundException;
import com.george.group.exception.GroupRequestException;
import com.george.group.mapper.GroupMapper;
import com.george.group.repository.GroupRepository;
import com.george.group.service.GroupService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper mapper;


    @Transactional(readOnly = true)
    @Override
    public GroupResponse getById(Long groupId) {
        return groupRepository.findById(groupId)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new GroupNotFoundException("Group with ID:" + groupId + "not found"));
    }

    @Transactional
    @Override
    public void deleteById(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group with ID:" + groupId + "not found"));

        groupRepository.delete(group);
    }


    @Transactional
    @Override
    public GroupResponse create(GroupRequest request) {

        Optional<Group> existingGroup = groupRepository.findByGroupName(request.getGroupName());

        if (existingGroup.isPresent()) {
            throw new GroupRequestException("Group with name '" + request.getGroupName() + "' already exists");
        }

        Group group = mapper.mapToEntity(request);
        Group savedGroup = groupRepository.save(group);

        return mapper.mapToResponse(savedGroup);
    }

    @Transactional
    @Override
    public GroupResponse update(Long groupId, GroupRequest request) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group with ID:" + groupId + "not found"));


        Optional<Group> existingGroup = groupRepository.findByGroupName(request.getGroupName());
        if (existingGroup.isPresent() && !existingGroup.get().getGroupId().equals(groupId)) {
            throw new GroupRequestException("Group with name '" + request.getGroupName() + "' already exists");
        }

        group.setGroupName(request.getGroupName());
        group.setGroupDescription(request.getGroupDescription());

        Group updatedGroup = groupRepository.save(group);
        return mapper.mapToResponse(updatedGroup);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GroupResponse> getAll() {
        return groupRepository.findAll()
                .stream()
                .map(mapper::mapToResponse)
                .toList();
    }
}

package com.george.group.service;

import com.george.group.dto.GroupRequest;
import com.george.group.dto.GroupResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GroupService {
    @Transactional
    void addNewUserToDefaultGroup(Long userId, String groupName);

    @Transactional
    GroupResponse addUserToGroup(Long groupId, Long userId);

    @Transactional
    GroupResponse removeUserFromGroup(Long groupId, Long userId);

    @Transactional(readOnly = true)
    GroupResponse getById(Long groupId);

    @Transactional
    void deleteById(Long groupId);

    @Transactional
    GroupResponse create(GroupRequest request);

    @Transactional
    GroupResponse update(Long groupId, GroupRequest request);

    @Transactional(readOnly = true)
    List<GroupResponse> getAll();
}

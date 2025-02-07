package com.george.group.service;

import com.george.group.dto.GroupRequest;
import com.george.group.dto.GroupResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GroupService {

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

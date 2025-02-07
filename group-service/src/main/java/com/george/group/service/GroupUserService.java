package com.george.group.service;

import com.george.core.UserRegisterEvent;
import com.george.core.UserRemoveEvent;
import com.george.core.UserUpdateEvent;
import com.george.group.dto.GroupResponse;
import org.springframework.transaction.annotation.Transactional;

public interface GroupUserService {
    @Transactional
    void addNewUserToDefaultGroup(UserRegisterEvent event, String groupName);

    @Transactional
    GroupResponse addUserToGroup(Long groupId, Long userId);

    @Transactional
    GroupResponse removeUserFromGroup(Long groupId, Long userId);

    @Transactional
    void updateUserInGroup(UserUpdateEvent event);

    @Transactional
    void removeUserFromAllGroups(UserRemoveEvent event);
}

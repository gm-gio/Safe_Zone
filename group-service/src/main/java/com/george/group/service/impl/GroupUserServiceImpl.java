package com.george.group.service.impl;

import com.george.clients.user.UserClient;
import com.george.clients.user.UserResponse;
import com.george.core.UserRegisterEvent;
import com.george.core.UserRemoveEvent;
import com.george.core.UserUpdateEvent;
import com.george.group.dto.GroupResponse;
import com.george.group.entity.Group;
import com.george.group.entity.GroupUser;
import com.george.group.exception.GroupNotFoundException;
import com.george.group.exception.UserNotFoundException;
import com.george.group.mapper.GroupMapper;
import com.george.group.repository.GroupRepository;
import com.george.group.repository.GroupUserRepository;
import com.george.group.service.GroupUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class GroupUserServiceImpl implements GroupUserService {

    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;
    private final GroupMapper mapper;
    private final UserClient userClient;


    @Transactional
    @Override
    public GroupResponse addUserToGroup(Long groupId, Long userId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group with ID " + groupId + " not found"));

        if (groupUserRepository.existsByGroupAndUserId(group, userId)) {
            throw new IllegalArgumentException("User with ID " + userId + " is already in the group");
        }

        UserResponse userResponse = userClient.getUserById(userId);

        GroupUser groupUser = GroupUser.builder()
                .group(group)
                .userId(userId)
                .firstName(userResponse.getFirstName())
                .lastName(userResponse.getLastName())
                .phone(userResponse.getPhone())
                .email(userResponse.getEmail())
                .build();

        groupUserRepository.save(groupUser);
        log.info("User with ID {} has been added to Group with ID {}", userId, groupId);

        return mapper.mapToResponse(group);
    }


    @Transactional
    @Override
    public GroupResponse removeUserFromGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group with ID: " + groupId + " not found"));

        Optional<GroupUser> groupUser = groupUserRepository.findByGroupAndUserId(group, userId);

        if (groupUser.isPresent()) {
            groupUserRepository.delete(groupUser.get());
            log.info("User with ID {} has been removed from Group with ID {}", userId, groupId);
        } else {
            log.warn("User with ID {} is not a member of Group with ID {}", userId, groupId);
        }

        return mapper.mapToResponse(group);
    }


    @Transactional
    @Override
    public void addNewUserToDefaultGroup(UserRegisterEvent event, String groupName) {
        Group group = groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new GroupNotFoundException("Group with name " + groupName + " not found"));

        GroupUser newUser = GroupUser.builder()
                .userId(event.getUserId())
                .firstName(event.getFirstName())
                .lastName(event.getLastName())
                .email(event.getEmail())
                .phone(event.getPhone())
                .group(group)
                .build();


        group.getUsers().add(newUser);


        groupRepository.save(group);
    }

    @Transactional
    @Override
    public void updateUserInGroup(UserUpdateEvent event) {

        List<Group> groups = groupRepository.findByUsers_UserId(event.getUserId());

        if (groups.isEmpty()) {
            throw new GroupNotFoundException("No groups found for userId: " + event.getUserId());
        }

        for (Group group : groups) {
            GroupUser groupUser = group.getUsers().stream()
                    .filter(user -> user.getUserId().equals(event.getUserId()))
                    .findFirst()
                    .orElseThrow(() -> new UserNotFoundException("User not found in group"));

            groupUser.setFirstName(event.getFirstName());
            groupUser.setLastName(event.getLastName());
            groupUser.setPhone(event.getPhone());

            groupRepository.save(group);
        }
    }

    @Transactional
    @Override
    public void removeUserFromAllGroups(UserRemoveEvent event) {

        List<Group> groups = groupRepository.findByUsers_UserId(event.getUserId());

        if (groups.isEmpty()) {
            log.info("No groups found for userId: {}", event.getUserId());
            return;
        }

        for (Group group : groups) {
            group.getUsers().removeIf(user -> user.getUserId().equals(event.getUserId()));
            groupRepository.save(group);
        }

        log.info("User with userId: {} removed from all groups.", event.getUserId());
    }
}

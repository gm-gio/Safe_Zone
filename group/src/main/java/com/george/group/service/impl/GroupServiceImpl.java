package com.george.group.service.impl;

import com.george.group.entity.Group;
import com.george.group.exception.GroupNotFoundException;
import com.george.group.repository.GroupRepository;
import com.george.group.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;


    @Override
    @Transactional
    public void addUserToDefaultGroup(Long userId, String groupName) {
        Group group = groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new GroupNotFoundException("Group with name " + groupName + " not found"));

        group.getUserId().add(userId);
        groupRepository.save(group);
    }


    public void addUSerToGroup(Long groupId, Long userId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));


        group.getUserId().add(userId);
        groupRepository.save(group);
    }

    public void removeUserFromGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        group.getUserId().remove(userId);

        groupRepository.save(group);
    }

    public Group getById(Long groupId) throws ChangeSetPersister.NotFoundException {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
    }

}

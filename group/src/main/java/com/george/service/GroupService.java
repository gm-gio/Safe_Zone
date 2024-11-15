package com.george.service;

import com.george.group.Group;
import com.george.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    public void addUSerToGroup(Long groupId, Long userId){

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));


        group.getUserId().add(userId);
        groupRepository.save(group);
    }

    public void removeUserFromGroup(Long groupId, Long userId){
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

package com.george.group.repository;

import com.george.group.entity.Group;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByGroupName(String groupName);
}

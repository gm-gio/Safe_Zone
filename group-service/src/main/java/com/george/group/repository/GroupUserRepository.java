package com.george.group.repository;

import com.george.group.entity.Group;
import com.george.group.entity.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {

    Optional<GroupUser> findByGroupAndUserId(Group group, Long userId);

    boolean existsByGroupAndUserId(Group group, Long userId);
}

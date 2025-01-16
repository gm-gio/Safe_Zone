package com.george.group.controller;


import com.george.group.dto.GroupRequest;
import com.george.group.dto.GroupResponse;
import com.george.group.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups")
public class GroupController {

    private final GroupService groupService;


    @PostMapping("/{groupId}/users/{userId}")
    @Operation(summary = "add User to a Group")
    public ResponseEntity<GroupResponse> addUserToGroup(@PathVariable Long groupId, @PathVariable Long userId) {

        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.addUserToGroup(groupId, userId));
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    @Operation(summary = "Remove User from Group")
    public ResponseEntity<GroupResponse> removeUserFromGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        groupService.removeUserFromGroup(groupId, userId);
        return ResponseEntity.status(OK).body(groupService.removeUserFromGroup(groupId, userId));
    }

    @GetMapping("/{groupId}")
    @Operation(summary = "receive Group by ID")
    public ResponseEntity<GroupResponse> getUserById(@PathVariable Long groupId) {
        return ResponseEntity.status(OK).body(groupService.getById(groupId));
    }

    @DeleteMapping("/{groupId}")
    @Operation(summary = "delete Group by ID")
    public ResponseEntity<Void> deleteById(@PathVariable Long groupId) {
        groupService.deleteById(groupId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/new")
    @Operation(summary = "create a new Group")
    public ResponseEntity<GroupResponse> createGroup(@RequestBody GroupRequest request) {
        return ResponseEntity.status(CREATED).body(groupService.create(request));
    }

    @PutMapping("/{groupId}")
    @Operation(summary = "update Group by ID")
    public ResponseEntity<GroupResponse> update(@PathVariable Long groupId, @RequestBody GroupRequest request) {
        return ResponseEntity.status(OK).body(groupService.update(groupId, request));
    }

    @GetMapping("/")
    @Operation(summary = "receive all groups")
    public ResponseEntity<List<GroupResponse>> getAll() {
        return ResponseEntity.status(OK).body(groupService.getAll());
    }
}

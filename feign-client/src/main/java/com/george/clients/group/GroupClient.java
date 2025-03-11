package com.george.clients.group;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "GROUP")
public interface GroupClient {

    @GetMapping("/api/v1/groups/{groupId}")
    public GroupResponse getGroupById(@PathVariable Long groupId);

    @GetMapping("/api/v1/groups/{groupId}/users")
    List<GroupUserResponse> getUsersByGroupId(@PathVariable Long groupId);
}

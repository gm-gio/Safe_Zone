package com.george.group.dto;



import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class GroupRequest {
    private String groupName;
    private String groupDescription;
}

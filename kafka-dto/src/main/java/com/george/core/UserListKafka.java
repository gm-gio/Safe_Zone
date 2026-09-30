package com.george.core;




import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserListKafka {
    private TemplateResponseForUserListK templateResponse;
    private List<Long> userIds;
}

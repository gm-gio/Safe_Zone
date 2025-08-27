package com.george.core;



import com.george.clients.template.TemplateResponse;
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
    TemplateResponse templateResponse;
    private List<Long> userIds;
}

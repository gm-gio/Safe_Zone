package com.george.shortener.dto.request;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NotificationOptionsRequest {

   private List<String> options;
}

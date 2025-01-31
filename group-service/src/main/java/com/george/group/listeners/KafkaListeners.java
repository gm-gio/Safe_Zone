package com.george.group.listeners;


import com.george.core.UserRegisterEvent;
import com.george.group.service.GroupService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class KafkaListeners {

    private final GroupService groupService;


    @KafkaListener(topics = "user-register-topics")
    public void handle(UserRegisterEvent event) {
        log.info("Received event {}", event.getUserId());

        groupService.addNewUserToDefaultGroup(event.getUserId(), "New Users");
    }
}

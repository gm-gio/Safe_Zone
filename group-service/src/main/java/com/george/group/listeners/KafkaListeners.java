package com.george.group.listeners;


import com.george.core.UserRegisterEvent;
import com.george.core.UserRemoveEvent;
import com.george.core.UserUpdateEvent;
import com.george.group.service.GroupUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class KafkaListeners {

    private final GroupUserService groupUserService;


    @KafkaListener(topics = "user-register-topics")
    public void handle(UserRegisterEvent event) {
        log.info("Received event {}", event.getUserId());

        groupUserService.addNewUserToDefaultGroup(event, "New Users");
    }

    @KafkaListener(topics = "user-update-topics")
    public void handleUserUpdate(UserUpdateEvent event) {
        log.info("Received user update event for userId: {}", event.getUserId());
        groupUserService.updateUserInGroup(event);
    }

    @KafkaListener(topics = "user-remove-topics")
    public void handleUserRemove(UserRemoveEvent event) {
        log.info("Received user remove event for userId: {}", event.getUserId());
        groupUserService.removeUserFromAllGroups(event);
    }

}

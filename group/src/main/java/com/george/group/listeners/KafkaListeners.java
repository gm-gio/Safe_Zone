package com.george.group.listeners;




import com.george.core.UserRegisterEvent;
import com.george.group.service.GroupService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KafkaListeners {

    private final GroupService groupService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());



    @KafkaListener(topics = "user-register-topics")
    public void handle(UserRegisterEvent event){
        LOGGER.info("Received event {}", event.getFirstName());

        groupService.addUserToDefaultGroup(event.getUserId(), "New Users");
    }
}

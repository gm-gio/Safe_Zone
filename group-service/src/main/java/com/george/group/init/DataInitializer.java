package com.george.group.init;

import com.george.group.entity.Group;
import com.george.group.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final GroupRepository groupRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!groupRepository.existsByGroupName("New Users")) {

            Group newGroup = Group.builder()
                    .groupName("New Users")
                    .groupDescription("Group for new users")
                    .build();
            groupRepository.save(newGroup);
            System.out.println("Group 'New Users' has been created.");
        }

    }
}

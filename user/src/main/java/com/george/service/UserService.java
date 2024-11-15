package com.george.service;

import com.george.repository.UserRepository;

import com.george.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

//    public void registerUser(UserRegistrationRequest request) {
//    User user = User.builder()
//            .firstName(request.firstName())
//            .lastName(request.lastName())
//            .email(request.email())
//            .build();
//
//    userRepository.save(user);
//    }

    public User getById(Long userId) throws ChangeSetPersister.NotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user not fnd"));
    }

}

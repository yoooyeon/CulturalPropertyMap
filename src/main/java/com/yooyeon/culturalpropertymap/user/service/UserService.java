package com.yooyeon.culturalpropertymap.user.service;

import com.yooyeon.culturalpropertymap.user.domain.User;
import com.yooyeon.culturalpropertymap.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;


    public List<User> findUsers() {
        return userRepository.findAll();
    }

}

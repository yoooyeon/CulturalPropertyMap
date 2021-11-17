package com.yooyeon.culturalpropertymap.user.service;

import com.yooyeon.culturalpropertymap.user.repository.SessionUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class SessionUserService {
    private final SessionUserRepository sessionUserRepository;



}

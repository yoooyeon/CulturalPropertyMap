package com.yooyeon.culturalpropertymap.user.repository;

import com.yooyeon.culturalpropertymap.config.auth.dto.SessionUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionUserRepository extends JpaRepository<SessionUser, Long> {
    SessionUser save(SessionUser user);
//    SessionUser findOneByEmail(String email);
    List<SessionUser> findByEmail(String email);
}
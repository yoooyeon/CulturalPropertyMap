package com.yooyeon.culturalpropertymap.user.repository;

import com.yooyeon.culturalpropertymap.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
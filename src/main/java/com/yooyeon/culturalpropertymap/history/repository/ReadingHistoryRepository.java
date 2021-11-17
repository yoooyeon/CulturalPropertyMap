package com.yooyeon.culturalpropertymap.history.repository;

import com.yooyeon.culturalpropertymap.config.auth.dto.SessionUser;
import com.yooyeon.culturalpropertymap.history.domain.ReadingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReadingHistoryRepository extends JpaRepository<ReadingHistory, Long> {

    Optional<ReadingHistory> findById(Long id);

    List<ReadingHistory> findAll();

    ReadingHistory findByNo(String no);

    List<ReadingHistory> findByUserOrderByModifiedDateDesc(SessionUser user);

    List<ReadingHistory> findTop10ByUserOrderByCountDesc(SessionUser user);

    List<ReadingHistory> findTop5ByOrderByCountDesc();

    boolean existsByNo(String no);

    List<ReadingHistory> findByUser(SessionUser user);

    List<ReadingHistory> findTop10ByUserOrderByModifiedDateDesc(SessionUser user);
}

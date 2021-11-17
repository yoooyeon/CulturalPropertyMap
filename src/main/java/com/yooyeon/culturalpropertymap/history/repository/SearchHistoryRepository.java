package com.yooyeon.culturalpropertymap.history.repository;

import com.yooyeon.culturalpropertymap.config.auth.dto.SessionUser;
import com.yooyeon.culturalpropertymap.history.domain.SearchHistory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {


    List<SearchHistory> findByUser(SessionUser user, Sort sort);

    List<SearchHistory> findByUser(SessionUser user);

    List<SearchHistory> findTop10ByUserOrderByCountDesc(SessionUser user);

    List<SearchHistory> findTop5ByOrderByCountDesc();

    List<SearchHistory> findTop50ByUserOrderByModifiedDateDesc(SessionUser user);


}
package com.yooyeon.culturalpropertymap.heritage.repository;


import com.yooyeon.culturalpropertymap.heritage.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {


    List<Tag> findByName(String title);

    @Query(value = "SELECT t.name FROM Tag t where t.tag_id in ( select ht.tag_id from reading_history r join heritage_tag ht on r.no = ht.no where r.user_id = :userId"+
            ")", nativeQuery = true)
    List<String> findTagNameByReadingHistory(@Param("userId") Long userId);

    @Query(value = "SELECT t.* FROM Tag t where t.tag_id in ( select ht.tag_id from reading_history r join heritage_tag ht on r.no = ht.no where r.user_id = :userId"+
            " and timestampdiff(hour, r.modified_date,now())<12)", nativeQuery = true)
    List<Tag> findTagByReadingHistory(@Param("userId") Long userId);

}

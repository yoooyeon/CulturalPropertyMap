package com.yooyeon.culturalpropertymap.heritage.repository;


import com.yooyeon.culturalpropertymap.heritage.domain.HeritageTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeritageTagRepository extends JpaRepository<HeritageTag, Long> {


}

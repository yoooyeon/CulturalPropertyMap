package com.yooyeon.culturalpropertymap.recommendation.repository;

import com.yooyeon.culturalpropertymap.recommendation.domain.SimilarItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SimilarItemsRepository extends JpaRepository<SimilarItems, String > {


    List<SimilarItems> findByNo(String no);


}

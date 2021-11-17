package com.yooyeon.culturalpropertymap.heritage.repository;

import com.yooyeon.culturalpropertymap.heritage.domain.Heritage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeritageRepository extends JpaRepository<Heritage, String> {
    Heritage findByNo(String no);

    // desc 문화재 이름으로 검색
    public List<Heritage> findHeritageByCcbaMnm1(String title);

    // desc 모두 검색
    public List<Heritage> findAll();

    // desc 시대 이름으로 검색
    public List<Heritage> findByCcceNameContaining(String ccceName, Sort sort);

    // desc 문화재 이름으로 검색
    public List<Heritage> findByCcbaMnm1Containing(String name, Sort sort);

    // desc 시대 키워드 및 문화재 이름으로 검색
    public List<Heritage> findByCcceNameContainingOrCcbaMnm1Containing(String ccceName, String ccbaMnm1, Sort sort);

    // desc 시대 키워드 및 문화재 이름으로 검색 - 페이징
    public Page<Heritage> findByCcceNameContainingOrCcbaMnm1Containing(String ccceName, String ccbaMnm1,Pageable pageable);

    // desc Page로 모두 검색
    public Page<Heritage> findAll(Pageable pageable);

    // desc 시대 키워드를 포함하는 문화재 검색
    public Page<Heritage> findByCcceNameContaining(String ccceName, Pageable pageable);

    // desc 문화재 이름과 상세 소재지로 검색
    List<Heritage> findByCcbaMnm1AndCcbaLcad(String title, String place);

    // desc 받아온 위치(현재 위치)로 Top 50 가져오는 로직
    @Query(value = "SELECT h.* ,  (6371*acos(cos(radians(:latitude))*cos(RADIANS(h.latitude))*cos(RADIANS(h.longitude)-radians(:longitude))+sin(radians(:latitude))*sin(RADIANS(h.latitude)))) " +
            "AS distance FROM Heritage h ORDER BY DISTANCE LIMIT 50", nativeQuery = true)
    List<Heritage> findNearHeritage(@Param("latitude") double latitude, @Param("longitude") double longitude);

    // desc 100번째 인덱스부터 100개 추가로
    @Query(value = "SELECT h.* ,  (6371*acos(cos(radians(:latitude))*cos(RADIANS(h.latitude))*cos(RADIANS(h.longitude)-radians(:longitude))+sin(radians(:latitude))*sin(RADIANS(h.latitude)))) " +
            "AS distance FROM Heritage h ORDER BY DISTANCE LIMIT :curIndex,100", nativeQuery = true)
    List<Heritage> findNearHeritageSecond(@Param("latitude") double latitude, @Param("longitude") double longitude,@Param("curIndex") int curIndex);

    // desc 받아온 위치(현재 위치)로 Top 6 가져오는 로직
    @Query(value = "SELECT h.* ,  (6371*acos(cos(radians(:latitude))*cos(RADIANS(h.latitude))*cos(RADIANS(h.longitude)-radians(:longitude))+sin(radians(:latitude))*sin(RADIANS(h.latitude)))) " +
            "AS distance FROM Heritage h ORDER BY DISTANCE LIMIT 50", nativeQuery = true)
    List<Heritage> findNearHeritageSix(@Param("latitude") double latitude, @Param("longitude") double longitude);


}

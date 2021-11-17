package com.yooyeon.culturalpropertymap.heritage.repository;

import com.yooyeon.culturalpropertymap.heritage.domain.EccpdTimeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EccpdTimeInfoRepository extends JpaRepository<EccpdTimeInfo, Long> {

    // desc 시대 이름을 포함하는 연표 정보 반환
    List<EccpdTimeInfo> findByCcceNameContaining(String ccceName);
}

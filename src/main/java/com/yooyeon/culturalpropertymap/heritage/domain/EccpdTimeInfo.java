package com.yooyeon.culturalpropertymap.heritage.domain;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class EccpdTimeInfo {

    @Id
    private Long timeSeq; // var_desc 연표 일련번호
    private String time; // var_desc 연도
    private String bcDvsn; // var_desc 시기 구분 Y/N
    private String timeTitle; // var_desc 사건 이름
    private String timeDscr; // var_desc 사건 설명
    private Long dspNmord; // var_desc 노출 순서
    private String useYn; // var_desc 사용 여부
    private java.sql.Timestamp regDtm; // var_desc 등록 일시
    private String regUser; // var_desc 등록자
    private java.sql.Timestamp modDtm; // var_desc 수정일시
    private String modUser; // var_desc 수정자
    private String timeDvsnCd; // var_desc 연표구분코드
    private String cDvsn; // var_desc 경여부 Y/N
    private String ccceName; // var_desc 시대 이름: 직접 분류함

}

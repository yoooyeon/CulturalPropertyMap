package com.yooyeon.culturalpropertymap.history.domain;

import com.yooyeon.culturalpropertymap.BaseTimeEntity;
import com.yooyeon.culturalpropertymap.config.auth.dto.SessionUser;
import lombok.*;

import javax.persistence.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@Table(name = "reading_history")
public class ReadingHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String no; // var_desc 문화재 번호 - 문화재청 데이터

    Integer count; //var_desc 열람 횟수

    Double accumulatedReadingTime; // var_desc 열람 누적 시간

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private SessionUser user;

    public ReadingHistory addCount() {
        count += 1;
        return this;
    }

    public ReadingHistory addTime(Double time) {
        accumulatedReadingTime += time;
        return this;
    }
}

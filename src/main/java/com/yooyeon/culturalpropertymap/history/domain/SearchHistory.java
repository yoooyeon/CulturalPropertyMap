package com.yooyeon.culturalpropertymap.history.domain;

import com.yooyeon.culturalpropertymap.BaseTimeEntity;
import com.yooyeon.culturalpropertymap.config.auth.dto.SessionUser;
import lombok.*;

import javax.persistence.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Setter(AccessLevel.PROTECTED)
public class SearchHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String searchKeyword; // desc 검색 키워드

    Integer count = 0;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private SessionUser user;

    public SearchHistory addCount() {
        count += 1;
        return this;
    }
}

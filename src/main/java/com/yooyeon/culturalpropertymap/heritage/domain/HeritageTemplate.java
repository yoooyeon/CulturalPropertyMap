package com.yooyeon.culturalpropertymap.heritage.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class HeritageTemplate {
    @Id
    private String no;
    private String ccbaMnm1;
    private String content;
    private String imageUrl;
    private String tags;

}

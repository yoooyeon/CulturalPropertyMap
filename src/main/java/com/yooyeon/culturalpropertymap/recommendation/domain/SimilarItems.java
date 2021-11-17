package com.yooyeon.culturalpropertymap.recommendation.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class SimilarItems {

  @Id
  private String no;
  private String similarItemsNo;
  private String similarItemsName;

}

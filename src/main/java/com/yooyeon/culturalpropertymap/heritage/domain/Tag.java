package com.yooyeon.culturalpropertymap.heritage.domain;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.gson.annotations.Expose;
import com.yooyeon.culturalpropertymap.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter(AccessLevel.PROTECTED)
@Builder
@Entity
public class Tag extends BaseTimeEntity {
    @Expose
    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Expose
    @Column(unique = true)
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HeritageTag> heritageTagList = new ArrayList<>();

    @Builder
    public Tag(String name) {
        this.name = name;
    }

    //==연관관계 메서드==//
    public void addTag(HeritageTag heritageTag) {
        this.heritageTagList.add(heritageTag);
    }


}

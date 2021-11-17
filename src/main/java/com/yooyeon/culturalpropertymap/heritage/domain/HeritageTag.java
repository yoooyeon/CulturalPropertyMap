package com.yooyeon.culturalpropertymap.heritage.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "heritage_tag")
public class HeritageTag {
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "heritage_tag_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "no")
    private Heritage heritage;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Builder
    public HeritageTag(Heritage heritage, Tag tag) {
        this.heritage = heritage;
        this.tag = tag;
    }





}

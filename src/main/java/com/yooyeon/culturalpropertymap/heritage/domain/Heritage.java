package com.yooyeon.culturalpropertymap.heritage.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.yooyeon.culturalpropertymap.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class Heritage extends BaseTimeEntity {

    @Expose
    @Id
    private String no; // var_desc 유물고유번호
    @Expose
    private String ccbaCpno; // var_desc 문화재연계번호
    @Expose
    private String ccbaCtcdNm; // var_desc 시도명
    @Expose
    private String ccmaName; // var_desc 문화재 종목
    @Expose
    private String ccsiName; // var_desc 시군구명
    @Expose
    private String ccbaKdcd; // var_desc 종목코드
    @Expose
    private String ccbaMnm1; // var_desc 문화재명(국문
    @Expose
    private String ccbaAsno; //  var_desc 지정번호
    @Expose
    private String ccbaCtcd; // var_desc  시도코드
    @Expose
    private String latitude; // var_desc 위도
    @Expose
    private String longitude; // var_desc 경도
    //    @Expose
    @Expose
    private String content; //  var_desc 내용
    @Expose
    private String imageUrl; // var_desc 메인 노출 이미지
    @Expose
    private String ccceName; //  var_desc 시대
    @Expose
    private String ccbaLcad; //  var_desc 소재지 상세

    // desc 여기서 mappedBy의 heritage는 HeritageTag에서 클래서 멤버변수 이름
    @JsonIgnore
    @OneToMany(mappedBy = "heritage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HeritageTag> heritageTagList = new ArrayList<>();

    // desc 연관관계 메서드
    public Heritage addHeritageTag(HeritageTag heritageTag) {
        this.heritageTagList.add(heritageTag);
        return this;
    }

}

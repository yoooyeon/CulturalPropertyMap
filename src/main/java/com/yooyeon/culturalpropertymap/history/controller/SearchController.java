package com.yooyeon.culturalpropertymap.history.controller;

import com.google.common.collect.Lists;
import com.yooyeon.culturalpropertymap.heritage.domain.Heritage;
import com.yooyeon.culturalpropertymap.heritage.domain.HeritageTag;
import com.yooyeon.culturalpropertymap.heritage.domain.Tag;
import com.yooyeon.culturalpropertymap.heritage.repository.TagRepository;
import com.yooyeon.culturalpropertymap.heritage.service.HeritageService;
import com.yooyeon.culturalpropertymap.history.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SearchController {

    private final HeritageService heritageService;
    private final SearchHistoryService searchHistoryService;
    private final TagRepository tagRepository;

    @ResponseBody
    @RequestMapping(value = "getKeyword", method = RequestMethod.POST)
    public List<Heritage> getKeyword(String keyword, HttpServletRequest request) {
        log.info("검색 키워드를 가져와 문화재를 반환하고, 검색 이력에 저장합니다. keyword = " + keyword);
        log.info(keyword);
        List<Heritage> heritageByKeyword = heritageService.getHeritageByHeritageNameorCcceName(keyword, keyword, Sort.unsorted());

        // Todo: 빈칸 검색시 저장 x
        //  desc keyword 저장 처리
        if (heritageByKeyword.size() != 0) {
            if (!keyword.equals("")) {
                searchHistoryService.save(keyword);


            }
        }

        return heritageByKeyword;

    }


    @ResponseBody
    @RequestMapping(value = "heritage/getHeritageList", method = RequestMethod.GET)
    public List<Heritage> getHeritageList(String keyword, HttpServletRequest request) {
        log.info("getKeywordForDataList / 검색 키워드를 가져와 문화재를 반환하고, 검색 이력에 저장합니다. keyword = " + keyword);
        log.info(keyword);


        if (keyword.equals("")) {
            return null;
        }

        List<Heritage> result = new ArrayList<>();
        List<HeritageTag> heritageTagList = null;

        Set<HeritageTag> heritageTagSet = new HashSet<>();
        Set<Heritage> heritageByTags = new HashSet<>();
        Set<Heritage> addTwo = new HashSet<>();

        // desc 1. 이름 and 시대로 한번 찾아보고
        List<Heritage> heritageByKeyword = heritageService.getHeritageByHeritageNameorCcceName(keyword, keyword, Sort.unsorted());

        // desc 2. tag에서 한번 찾아보고
        List<Tag> tags = tagRepository.findByName(keyword);
        log.info("Tags = " + tags.toString());

        for (Tag t : tags) {
            heritageTagSet.addAll(t.getHeritageTagList());
        }

        heritageTagList = Lists.newArrayList(heritageTagSet);

        log.info("heritageTagList = " + heritageTagList.toString());

        for (HeritageTag heritageTag : heritageTagList) {
            heritageByTags.add(heritageTag.getHeritage());
        }

        addTwo.addAll(heritageByKeyword);
        addTwo.addAll(heritageByTags);
        result = Lists.newArrayList(addTwo);


        //  desc keyword 이력에 저장 처리
        if (result.size() != 0) {
            if (!keyword.equals("")) {
                searchHistoryService.save(keyword);
            }
        }

        log.info("result heritageByKeyword = " + result);
        Page<Heritage> pages = new PageImpl<>(result);
        pages.getTotalPages();
        return result;

    }


}

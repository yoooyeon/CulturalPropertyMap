package com.yooyeon.culturalpropertymap.history.service;

import com.yooyeon.culturalpropertymap.config.auth.dto.SessionUser;
import com.yooyeon.culturalpropertymap.heritage.domain.Heritage;
import com.yooyeon.culturalpropertymap.heritage.domain.Tag;
import com.yooyeon.culturalpropertymap.history.domain.ReadingHistory;
import com.yooyeon.culturalpropertymap.history.domain.SearchHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecommendationService {

    private final SearchHistoryService searchHistoryService;
    private final ReadingHistoryService readingHistoryService;
    private final HttpSession httpSession;

    // desc 검색 이력, 열람 이력을 활용한 태그 추천
    public List<Tag> recomnendTag(){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        List<SearchHistory> searchHistoriesSortedTop10 = searchHistoryService.findSearchHistoriesSortedTop10(user);
        String keyword;
        for (SearchHistory history:searchHistoriesSortedTop10){
            keyword = history.getSearchKeyword();

        }

        return null;
    }

    // desc 검색 이력, 열람 이력을 활용한 문화재 추천
    public List<Heritage> recomnendHeritage(){
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        List<ReadingHistory> searchHistoriesSortedTop10 = readingHistoryService.findReadingHistoriesSortedTop10(user);

        for (ReadingHistory history:searchHistoriesSortedTop10){

        }
        return null;
    }





}

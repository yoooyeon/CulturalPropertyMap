package com.yooyeon.culturalpropertymap.history.service;

import com.google.common.collect.Lists;
import com.yooyeon.culturalpropertymap.config.auth.dto.SessionUser;
import com.yooyeon.culturalpropertymap.heritage.domain.Heritage;
import com.yooyeon.culturalpropertymap.heritage.service.HeritageService;
import com.yooyeon.culturalpropertymap.history.domain.SearchHistory;
import com.yooyeon.culturalpropertymap.history.repository.SearchHistoryRepository;
import com.yooyeon.culturalpropertymap.user.repository.SessionUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchHistoryService {
    private final SearchHistoryRepository searchHistoryRepository;
    private final SessionUserRepository sessionUserRepository;
    private final HttpSession httpSession;
    private final HeritageService heritageService;

    public void save(String keyword) {

        log.info("SearchHistoryService save / 검색 키워드 저장합니다. ");

        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        SessionUser savedUser;
        // 이메일로 현재 저장된 유저인지 찾아보기
        List<SessionUser> findUser = sessionUserRepository.findByEmail(user.getEmail());

        if (findUser.isEmpty()) { // 유저 없으면 새로 저장
            savedUser = sessionUserRepository.save(user);
            log.info("!! 유저 새로 저장");
        } else { // 있으면 그대로 사용
            savedUser = findUser.get(0);
            log.info("!! 기존 유저");
        }


        // desc 중복되는 키워드는 있어도 된다. 한 유저에 대해서 같은 키워드가 있으면 안되는 것이다.

        // var_desc 1. 유저가 가지고 있는 열람 이력을 가져온다.
        List<SearchHistory> searchHistoryByUser = searchHistoryRepository.findByUser(savedUser);
        SearchHistory searchHistory;

        // var_desc 2.  그 중 searchHistory가 가지고있는 searchKeyword 중 keyword가 있는지 확인한다.
        for (SearchHistory history : searchHistoryByUser) {
            if (keyword.equals(history.getSearchKeyword())) {
                // var_desc 3.  있으면 카운트를 늘려준다.
                log.info("SearchHistoryService save / 이 유저에게 있는 키워드");

                searchHistory = history.addCount();
                searchHistoryRepository.save(searchHistory);
                return;
            }
        }
        // var_desc 4.  없으면 새로 저장한다.
        log.info("SearchHistoryService save / 이 유저에게 없는 키워드");

        searchHistory = SearchHistory.builder().searchKeyword(keyword).user(savedUser).count(1).build();
        searchHistoryRepository.save(searchHistory);

    }


    // desc 사용자의 검색 내역을 정렬하여 가져온다.
    public List<SearchHistory> findSearchHistoriesSorted(SessionUser user, Sort sort) {
        SessionUser savedUser;

        // 이메일로 현재 저장된 유저인지 찾아보기
        List<SessionUser> findUser = sessionUserRepository.findByEmail(user.getEmail());

        if (findUser.isEmpty()) { // 유저 없으면 새로 저장
            savedUser = sessionUserRepository.save(user);
            log.info("!! 유저 새로 저장");
        } else { // 있으면 그대로 사용
            savedUser = findUser.get(0);
            log.info("!! 기존 유저");
        }
        return searchHistoryRepository.findByUser(savedUser, sort);
    }

    //desc 사용자가 검색한 내역을 정렬하여 top 10 가져온다.
    public List<SearchHistory> findSearchHistoriesSortedTop10(SessionUser user) {
        return searchHistoryRepository.findTop10ByUserOrderByCountDesc(user);
    }

    public List<SearchHistory> getSearchHistoryTop5() {
        return searchHistoryRepository.findTop5ByOrderByCountDesc();
    }

    // desc 최근에 검색한 문화재 (24시간)
    public List<Heritage> getSearchHistoryWithin24() {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        SessionUser savedUser;
        // 이메일로 현재 저장된 유저인지 찾아보기
        List<SessionUser> findUser = sessionUserRepository.findByEmail(user.getEmail());

        if (findUser.isEmpty()) { // 유저 없으면 새로 저장
            savedUser = sessionUserRepository.save(user);
            log.info("!! 유저 새로 저장");
        } else { // 있으면 그대로 사용
            savedUser = findUser.get(0);
            log.info("!! 기존 유저");
        }

        List<SearchHistory> searchHistories = searchHistoryRepository.findTop50ByUserOrderByModifiedDateDesc(savedUser);
        Set<Heritage> heritageSet = new HashSet<>();
        String keyword;

        for (SearchHistory history : searchHistories) {
            if (ChronoUnit.HOURS.between(history.getModifiedDate(), LocalDateTime.now())>12) break;

            if (heritageSet.size() > 20) break;
            keyword = history.getSearchKeyword();
            heritageSet.addAll(heritageService.getHeritageByHeritageNameorCcceName(keyword, keyword, Sort.unsorted()));
        }

        return Lists.newArrayList(heritageSet);

    }
}

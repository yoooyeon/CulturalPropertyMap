package com.yooyeon.culturalpropertymap.user.controller;

import com.yooyeon.culturalpropertymap.config.auth.dto.SessionUser;
import com.yooyeon.culturalpropertymap.history.domain.SearchHistory;
import com.yooyeon.culturalpropertymap.history.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MyPageController {


    private final HttpSession httpSession;
    private final SearchHistoryService searchHistroyService;


    // desc 검색 목록 보여주기
    @GetMapping("/mypage")
    public String mypage(Model model) {

        // 로그인

        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userImg", user.getPicture());
        }
        log.info("user= " + user);

//        assert user != null;
        List<SearchHistory> searchHistories = searchHistroyService.findSearchHistoriesSorted(user, Sort.by(Sort.Direction.DESC,"count"));
        log.info("searchHistories= " + searchHistories.toString());
        log.info("searchHistories= " + searchHistories.size()+"개");
        model.addAttribute("searchHistories", searchHistories);

        return "mypage";
    }













}

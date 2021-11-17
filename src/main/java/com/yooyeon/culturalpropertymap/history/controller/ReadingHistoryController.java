package com.yooyeon.culturalpropertymap.history.controller;

import com.yooyeon.culturalpropertymap.history.service.ReadingHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ReadingHistoryController {
    private final ReadingHistoryService readingHistoryService;

//    // desc: 열람 이력에 횟수 저장하는 로직
//    @ResponseBody
//    @RequestMapping(value = "addToReadingList", method = RequestMethod.POST)
//    public void addToReadingList(@RequestBody HashMap<String, String> bodyMap) {
//        String title = bodyMap.get("title");
//        String place = bodyMap.get("place");
//        readingHistoryService.saveTimeInReadingHistory(title, place);
//    }


    // desc: 열람 이력에 시간 저장하는 로직
    @ResponseBody
    @RequestMapping(value = "saveReadingTime", method = RequestMethod.POST)
    public void saveReadingTime(String duration,String title, String place) {

        Double time = Double.parseDouble(duration);

        log.info("saveReadingTime: " + duration);

        readingHistoryService.saveTimeInReadingHistory(title,place,time);




    }
}

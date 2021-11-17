package com.yooyeon.culturalpropertymap.heritage.service;

import com.google.common.collect.Lists;

import com.yooyeon.culturalpropertymap.config.auth.dto.SessionUser;
import com.yooyeon.culturalpropertymap.heritage.domain.Heritage;
import com.yooyeon.culturalpropertymap.heritage.domain.HeritageTag;
import com.yooyeon.culturalpropertymap.heritage.domain.Tag;
import com.yooyeon.culturalpropertymap.heritage.repository.TagRepository;
import com.yooyeon.culturalpropertymap.history.domain.ReadingHistory;
import com.yooyeon.culturalpropertymap.history.service.ReadingHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class TagService {
    private final TagRepository tagRepository;
    private final ReadingHistoryService readingHistoryService;
    private final HeritageService heritageService;

    public List<Tag> getPopularTag() {

        // Todo: 각 문화재에 해당하는 카운트를 모두 합해야한다.

        List<ReadingHistory> readingHistoryTop5 = readingHistoryService.getReadingHistoryTop5();
        List<HeritageTag> heritageTagList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        String name;
        Heritage heritageByNo;
        List<Tag> tags = new ArrayList<>();
        HashSet<HeritageTag> heritageTagHashSet = new HashSet<>();
        log.info("readingHistoryTop5 = " + readingHistoryTop5);

        for (ReadingHistory history : readingHistoryTop5) {
            heritageByNo = heritageService.findHeritageByNo(history.getNo());
            heritageTagHashSet.addAll(heritageByNo.getHeritageTagList());
            log.info("heritageTagList = " + heritageTagList);

        }

        heritageTagList = Lists.newArrayList(heritageTagHashSet);

        for (HeritageTag heritageTag : heritageTagList) {
            name = heritageTag.getTag().getName();
            nameList.add(name);

        }

        List<Tag> tagList;
        if (nameList.size() < 1) {
            return null;
        } else {
            tagList = tagRepository.findByName(nameList.get(0));
        }
        return tagList;
    }

    public List<HeritageTag> getHeritageTag(String key) {
        List<Tag> byName = tagRepository.findByName(key);
        Set<HeritageTag> heritageTagSet = new HashSet<>();
        for (Tag t : byName) {
            heritageTagSet.addAll(t.getHeritageTagList());
        }
        return Lists.newArrayList(heritageTagSet);
    }


    public List<String> getTagNameFromReadingHistoryByUser(SessionUser user) {
        List<String> list = new ArrayList<>();
        log.info("getTagNameFromReadingHistoryByUser = "+user.getId());
        List<Tag> tagByReadingHistory = tagRepository.findTagByReadingHistory(user.getId());
        for (Tag t : tagByReadingHistory) {


            list.add(t.getName());
        }
        log.info("TagService / getTagNameFromReadingHistoryByUser = " + list);
        return list;
    }

}

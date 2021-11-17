package com.yooyeon.culturalpropertymap.recommendation.service;

import com.google.common.collect.Lists;
import com.yooyeon.culturalpropertymap.recommendation.domain.SimilarItems;
import com.yooyeon.culturalpropertymap.recommendation.repository.SimilarItemsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class SimilarItemsService {

    private final SimilarItemsRepository similarItemsRepository;

    public List<String> getSimilarItemsByNo(String no) {

        List<SimilarItems> items = similarItemsRepository.findByNo(no);

        String[] split=null;
        for (SimilarItems item : items) {

            String numbers = item.getSimilarItemsNo();
            split = numbers.split("~");


        }
        ArrayList<String> noList = Lists.newArrayList(split);

        log.info("SimilarItemsService = "+noList);

        return noList;

    }


}
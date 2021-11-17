package com.yooyeon.culturalpropertymap.recommendation.controller;

import com.yooyeon.culturalpropertymap.recommendation.service.SimilarItemsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;


@Slf4j
@RequiredArgsConstructor
@Controller
public class SimilarItemsController {

    private final SimilarItemsService similarItemsService;



}
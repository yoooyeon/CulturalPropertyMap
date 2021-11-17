package com.yooyeon.culturalpropertymap.heritage.controller;

import com.google.gson.Gson;

import com.yooyeon.culturalpropertymap.config.auth.dto.SessionUser;
import com.yooyeon.culturalpropertymap.heritage.domain.Heritage;
import com.yooyeon.culturalpropertymap.heritage.domain.HeritageTag;
import com.yooyeon.culturalpropertymap.heritage.domain.Tag;
import com.yooyeon.culturalpropertymap.heritage.repository.HeritageRepository;
import com.yooyeon.culturalpropertymap.heritage.repository.HeritageTagRepository;
import com.yooyeon.culturalpropertymap.heritage.repository.TagRepository;
import com.yooyeon.culturalpropertymap.heritage.service.HeritageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HeritageController {

    private final HeritageService heritageService;
    private final HttpSession httpSession;
    private final TagRepository tagRepository;
    private final HeritageTagRepository heritageTagRepository;
    private final HeritageRepository heritageRepository;

    // desc 문화재청의 데이터를 반환합니다.
    @ResponseBody
    @RequestMapping(value = "getHeritageInfo", method = RequestMethod.POST)
    public Heritage getHeritageInfo(String title) {
        log.info("getHeritageInfo / 문화재청의 데이터를 반환합니다.");


        return heritageService.findOneByTitle(title).get(0);
    }


    @ResponseBody
    @RequestMapping(value = "getPublicDataTag", method = RequestMethod.POST)
    public List<String> getPublicDataTag(String no) {
        log.info("공공데이터 내용 형태소 분석 및 반환합니다. ");

        Heritage heritage = heritageService.findOneByNo(no);
        String content = heritage.getContent();


        // desc tag 저장 로직
        List<String> heritageTagList = getHeritageTagList(content);
        if (heritageTagList==null){
            return null;
        }
        Tag tag;
        for (String s : heritageTagList) {
            List<Tag> saved = tagRepository.findByName(s);
            if (saved.isEmpty()) {
                tag = Tag.builder().name(s).build();
                tag = tagRepository.save(tag);
            }
            else{
                tag = saved.get(0);
            }
            HeritageTag heritageTag = HeritageTag.builder().tag(tag).heritage(heritage).build();
            heritageTag = heritageTagRepository.save(heritageTag);

            heritage = heritage.addHeritageTag(heritageTag);

            // desc 최종 저장
            heritage = heritageRepository.save(heritage);
        }

        return heritageTagList;

    }


    // desc 문화재청 데이터 형태소 분석
    private List<String> getHeritageTagList(String content) {
        log.info("yooyeon: 웅진백과 컨텐트 내용 형태소 분석합니다.");
        List<String> tagList = new ArrayList<>();
        System.out.println("getHeritageTagList");

        // 언어 분석 기술 문어/구어 중 한가지만 선택해 사용
        // 언어 분석 기술(문어)
        String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU";
        // 언어 분석 기술(구어)
        //String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU_spoken";
        String accessKey = "9092fb80-a53c-4d71-b0d5-1147185b3de8";// ""b43e96eb-2ec7-4cf2-8251-745f0baf2f45";// 내꺼 "b43e96eb-2ec7-4cf2-8251-745f0baf2f45";   // 발급받은 API Key // Todo: 은닉 cbfb61c4-783a-4735-bdd1-11c2d355516c
        String analysisCode = "ner";        // 언어 분석 코드
        String text = "";           // 분석할 텍스트 데이터
        Gson gson = new Gson();

        text = content;

        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();

        argument.put("analysis_code", analysisCode);
        argument.put("text", text);

        request.put("access_key", accessKey);
        request.put("argument", argument);

        URL url;
        Integer responseCode = null;
        String responBodyJson = null;
        Map<String, Object> responeBody = null;

        try {
            url = new URL(openApiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(gson.toJson(request).getBytes("UTF-8"));
            wr.flush();
            wr.close();

            responseCode = con.getResponseCode();
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();

            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            responBodyJson = sb.toString();

            // http 요청 오류 시 처리
            if (responseCode != 200) {
                // 오류 내용 출력
                System.out.println("[error] " + responBodyJson);
                return null;
            }

            responeBody = gson.fromJson(responBodyJson, Map.class);
            Integer result = ((Double) responeBody.get("result")).intValue();
            Map<String, Object> returnObject;
            List<Map> sentences;

            // 분석 요청 오류 시 처리
            if (result != 0) {

                // 오류 내용 출력
                System.out.println("[error] " + responeBody.get("result"));
                return null;
            }

            // 분석 결과 활용
            returnObject = (Map<String, Object>) responeBody.get("return_object");
            sentences = (List<Map>) returnObject.get("sentence");

            Map<String, Morpheme> morphemesMap = new HashMap<String, Morpheme>();
            Map<String, NameEntity> nameEntitiesMap = new HashMap<String, NameEntity>();
            List<Morpheme> morphemes = null;
            List<NameEntity> nameEntities = null;

            for (Map<String, Object> sentence : sentences) {
                // 형태소 분석기 결과 수집 및 정렬
                List<Map<String, Object>> morphologicalAnalysisResult = (List<Map<String, Object>>) sentence.get("morp");
                for (Map<String, Object> morphemeInfo : morphologicalAnalysisResult) {
                    String lemma = (String) morphemeInfo.get("lemma");
                    Morpheme morpheme = morphemesMap.get(lemma);
                    if (morpheme == null) {
                        morpheme = new Morpheme(lemma, (String) morphemeInfo.get("type"), 1);
                        morphemesMap.put(lemma, morpheme);
                    } else {
                        morpheme.count = morpheme.count + 1;
                    }
                }

                // 개체명 분석 결과 수집 및 정렬
                List<Map<String, Object>> nameEntityRecognitionResult = (List<Map<String, Object>>) sentence.get("NE");
                for (Map<String, Object> nameEntityInfo : nameEntityRecognitionResult) {
                    String name = (String) nameEntityInfo.get("text");
                    NameEntity nameEntity = nameEntitiesMap.get(name);
                    if (nameEntity == null) {
                        nameEntity = new NameEntity(name, (String) nameEntityInfo.get("type"), 1);
                        nameEntitiesMap.put(name, nameEntity);
                    } else {
                        nameEntity.count = nameEntity.count + 1;
                    }
                }
            }

            if (0 < morphemesMap.size()) {
                morphemes = new ArrayList<Morpheme>(morphemesMap.values());
                morphemes.sort((morpheme1, morpheme2) -> {
                    return morpheme2.count - morpheme1.count;
                });
            }

            if (0 < nameEntitiesMap.size()) {
                nameEntities = new ArrayList<NameEntity>(nameEntitiesMap.values());
                nameEntities.sort((nameEntity1, nameEntity2) -> {
                    return nameEntity2.count - nameEntity1.count;
                });
            }
//            System.out.println("형태소");
//            System.out.println();
//            System.out.println();
//            System.out.println();

            // 형태소들 중 명사들에 대해서 많이 노출된 순으로 출력 ( 최대 5개 )
            morphemes
                    .stream()
                    .filter(morpheme -> {
                        return morpheme.type.equals("NNG") ||
                                morpheme.type.equals("NNP") ||
                                morpheme.type.equals("NNB");
                    })
                    .limit(4)
                    .forEach(morpheme -> {
//                        System.out.println("[명사] " + morpheme.text + " (" + morpheme.count + ")");
//                        System.out.println("morpheme = " + morpheme.text);
//                        System.out.println("morpheme = " + morpheme.text.length());
                        if (morpheme.text.length() < 2) {
                            return;
                        }
                        tagList.add(morpheme.text);
                    });

            System.out.println("");
            nameEntities
                    .stream()
                    .limit(4)
                    .forEach(nameEntity -> {
//                        System.out.println("[개체명] " + nameEntity.text + " (" + nameEntity.count + ")");
                        if (nameEntity.text.length() < 2) {
                            return;
                        }
                        tagList.add(nameEntity.text);

                    });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tagList;


    }


    static public class Morpheme {
        final String text;
        final String type;
        Integer count;

        public Morpheme(String text, String type, Integer count) {
            this.text = text;
            this.type = type;
            this.count = count;
        }
    }

    static public class NameEntity {
        final String text;
        final String type;
        Integer count;

        public NameEntity(String text, String type, Integer count) {
            this.text = text;
            this.type = type;
            this.count = count;
        }
    }


    // desc 문화재 데이터 목록 탭
    @GetMapping("/heritage/list")
    public String list(Model model, @PageableDefault(size = 4, sort = "ccbaMnm1", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Heritage> heritages = heritageService.findAllHeritages(pageable);
        // 로그인 처리
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userImg", user.getPicture());
        }

        // 시대 키워드 포함
        model.addAttribute("heritageList", heritages);

        // Paging 처리 변수
        model.addAttribute("check", heritageService.getListCheck(pageable));
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        return "heritage-list";

    }


}

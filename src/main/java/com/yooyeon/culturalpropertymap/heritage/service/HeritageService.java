package com.yooyeon.culturalpropertymap.heritage.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yooyeon.culturalpropertymap.heritage.domain.Heritage;
import com.yooyeon.culturalpropertymap.heritage.domain.HeritageTag;
import com.yooyeon.culturalpropertymap.heritage.domain.Tag;
import com.yooyeon.culturalpropertymap.heritage.repository.HeritageRepository;
import com.yooyeon.culturalpropertymap.heritage.repository.HeritageTagRepository;
import com.yooyeon.culturalpropertymap.heritage.repository.TagRepository;
import com.yooyeon.culturalpropertymap.history.domain.ReadingHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
@Service
public class HeritageService {
    private final HeritageRepository heritageRepository;
    private final TagRepository tagRepository;
    private final HeritageTagRepository heritageTagRepository;

    public List<Heritage> findNearHeritageTopSix(double lat, double lon){
       return heritageRepository.findNearHeritageSix(lat,lon);
    }


    public List<String> getTag(String no) {
        log.info("공공데이터 내용 형태소 분석 및 반환합니다. ");

        Heritage heritage = findOneByNo(no);
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
    public List<Heritage> findOneByTitle(String title) {
        return heritageRepository.findHeritageByCcbaMnm1(title);
    }

    public List<Heritage> findHeritages() {

        return heritageRepository.findAll();
    }

    public Heritage findHeritageByNo(String no) {

        return heritageRepository.findByNo(no);
    }

    public Page<Heritage> findAllHeritages(Pageable pageable) {

        return heritageRepository.findAll(pageable);
    }

    // 시대별, 페이징
    public Page<Heritage> findByCcceNameContaining(String ccce, Pageable pageable) {
        return heritageRepository.findByCcceNameContaining(ccce, pageable);
    }

    // 시대별, 소팅    @Transactional(readonly=true)
    public List<Heritage> findByCcceNameContainingList(String ccce, Sort sort) {
        return heritageRepository.findByCcceNameContaining(ccce, sort);
    }

    // 문화재 이름으로 검색
    public List<Heritage> getHeritageByHeritageName(String name, Sort sort) {
        return heritageRepository.findByCcbaMnm1Containing(name, sort);
    }

    //문화재 이름 또는 시대 포함하여 검색
    public List<Heritage> getHeritageByHeritageNameorCcceName(String ccceName, String ccbaMnm1, Sort sort) {
        return heritageRepository.findByCcceNameContainingOrCcbaMnm1Containing(ccceName, ccbaMnm1, sort);

    }
    // desc 문화재 이름 또는 시대 포함하여 검색 - 페이징
    public Page<Heritage> getHeritageByHeritageNameorCcceName(String ccceName, String ccbaMnm1, Pageable pageable) {
        return heritageRepository.findByCcceNameContainingOrCcbaMnm1Containing(ccceName, ccbaMnm1, pageable);

    }

    // Todo: 태그도 포함하기 문화재 이름 또는 시대 포함하여 검색
//    public List<Heritage> getHeritageByHeritageNameorCcceNameorTag(String ccceName, String ccbaMnm1, Sort sort) {
//        return heritageRepository.findByCcceNameContainingOrCcbaMnm1Containing(ccceName, ccbaMnm1, sort);
//
//    }
    public Boolean getListCheck(Pageable pageable) {
        Page<Heritage> saved = getHeritageList(pageable);
        return saved.hasNext();
    }

    public Page<Heritage> getHeritageList(Pageable pageable) {
        return heritageRepository.findAll(pageable);
    }


    public Heritage findOneByNo(String no) {
        return heritageRepository.findByNo(no);
    }

    int curIndex = 0;

    // desc 가까운 문화재를 찾아서 반환
    public String getHeritageNear(Double lat, Double lon) {

        curIndex = 51; // var_desc 백그라운드에서 50번째부터 가져오기, 초기화

        if (lat == null || lon == null) {
            return null;
        }
        List<Heritage> heritages = heritageRepository.findNearHeritage(lat, lon);
        log.info("HeritageService heritages");
        log.info(heritages.toString());
        log.info("Finish!!!");

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setLenient()
                .create();

        return gson.toJson(heritages);

    }

    // desc 가까운 문화재를 찾아서 반환
    public List<Heritage> getHeritageNear50(Double lat, Double lon) {


        if (lat == null || lon == null) {
            return null;
        }


        return heritageRepository.findNearHeritage(lat, lon);

    }

    // desc 가까운 문화재를 찾아서 반환 후 백그라운드에서 추가 가져오기

    public String getHeritageNearInBackground(Double lat, Double lon) {

        if (lat == null || lon == null) {
            return null;
        }
        curIndex += 100;
        log.info("curIndex = " + curIndex);

        List<Heritage> heritages = heritageRepository.findNearHeritageSecond(lat, lon, curIndex);
        log.info("getHeritageNearInBackground = ");
//        log.info(heritages.toString());
        log.info("Finish!!!");

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setLenient()
                .create();

        return gson.toJson(heritages);
    }

    public List<Heritage> getHeritageByReadingHistory(List<ReadingHistory> readingHistoryByUser) {
        List<Heritage> result = new ArrayList<>();
        if (readingHistoryByUser == null) {
            return null;

        }
        Heritage tmp;
        for (ReadingHistory history : readingHistoryByUser) {
            tmp = heritageRepository.findByNo(history.getNo());
            result.add(tmp);
        }
        return result;
    }
    private List<String> getHeritageTagList(String content) {
        log.info("yooyeon: 웅진백과 컨텐트 내용 형태소 분석합니다.");
        List<String> tagList = new ArrayList<>();
        System.out.println("getHeritageTagList");

        // 언어 분석 기술 문어/구어 중 한가지만 선택해 사용
        // 언어 분석 기술(문어)
        String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU";
        // 언어 분석 기술(구어)
        //String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU_spoken";
        String accessKey = "a5653a2d-1ecf-4371-8f05-5f611d6889aa";// ""b43e96eb-2ec7-4cf2-8251-745f0baf2f45";// 내꺼 "b43e96eb-2ec7-4cf2-8251-745f0baf2f45";   // 발급받은 API Key // Todo: 은닉 cbfb61c4-783a-4735-bdd1-11c2d355516c
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
}

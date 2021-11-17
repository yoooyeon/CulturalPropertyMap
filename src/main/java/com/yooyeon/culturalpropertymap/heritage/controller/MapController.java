package com.yooyeon.culturalpropertymap.heritage.controller;

import com.google.gson.Gson;
import com.yooyeon.culturalpropertymap.config.auth.dto.SessionUser;
import com.yooyeon.culturalpropertymap.heritage.domain.Heritage;
import com.yooyeon.culturalpropertymap.heritage.service.HeritageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MapController {

    private final HttpSession httpSession;
    private final HeritageService heritageService;
    private String ccce = null;


    @GetMapping("map")
    public String kakaoMap(Model model) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userImg", user.getPicture());
        }

        return "kakao-map";
    }


    // desc kakao map 문화재 데이터를 Json 타입으로 변환하여 넘겨준다.
    @GetMapping(value = "/heritageList", produces = "application/json")
    public @ResponseBody
    String getPositionData(HttpServletRequest req, HttpServletResponse res) {
        List<Heritage> heritages = heritageService.findHeritages();
        return new Gson().toJson(heritages);
    }

    // desc 시대 이름 정보 가져옴
    @ResponseBody
    @RequestMapping(value = "getCcce", method = RequestMethod.POST)
    public void getCcce(String ccce) {
        this.ccce = ccce;
    }

    // desc 현재 위치 반환
    @ResponseBody
    @RequestMapping(value = "getCurPos", method = RequestMethod.POST)
    public String getPosition(@RequestBody HashMap<String, Object> bodyMap) {
        Double lat = (Double) bodyMap.get("lat");
        Double lon = (Double) bodyMap.get("lon");
        log.info("getCurPos");
        log.info(lat + ", " + lon);
        return heritageService.getHeritageNear(lat, lon);

    }
    @ResponseBody
    @RequestMapping(value = "getCurPosInBackground", method = RequestMethod.POST)
    public String getCurPostBackground( @RequestBody HashMap<String, Object> bodyMap) {

        Double lat = (Double) bodyMap.get("lat");
        Double lon = (Double) bodyMap.get("lon");
        log.info("getCurPos");
        log.info(lat + ", " + lon);

        return heritageService.getHeritageNearInBackground(lat, lon);

    }

    // desc 시대별 버튼 눌렀을 때 해당 데이터 반환
    @GetMapping(value = "displayByCcce", produces = "application/json")
    public @ResponseBody
    List<Heritage> displayByCcceName() {
        Map<String, String> ccceMap = new HashMap<>();
        ccceMap.put("prehistory", "선사시대");
        ccceMap.put("samkook", "삼국시대"); // 고구려, 백제, 신라
        ccceMap.put("balhae", "발해"); // 발해, 통일신라
        ccceMap.put("goryeo", "고려");
        ccceMap.put("joseon", "조선");
        List<Heritage> heritages;
        if (ccce.equals("prehistory")) {
            heritages = heritageService.findByCcceNameContainingList(ccceMap.get(ccce), Sort.unsorted()); // 선사시대
            heritages.addAll(heritageService.findByCcceNameContainingList("구석기", Sort.unsorted()));
            heritages.addAll(heritageService.findByCcceNameContainingList("신석기", Sort.unsorted()));
            heritages.addAll(heritageService.findByCcceNameContainingList("청동", Sort.unsorted()));
        } else if (ccce.equals("samkook")) {
            heritages = heritageService.findByCcceNameContainingList(ccceMap.get(ccce), Sort.unsorted()); // 삼국시대
            heritages.addAll(heritageService.findByCcceNameContainingList("고구려", Sort.unsorted()));
            heritages.addAll(heritageService.findByCcceNameContainingList("백제", Sort.unsorted()));
            heritages.addAll(heritageService.findByCcceNameContainingList("신라", Sort.unsorted())); // Todo: 통일신라 나오지 않도록
        } else if (ccce.equals("balhae")) {
            heritages = heritageService.findByCcceNameContainingList(ccceMap.get(ccce), Sort.unsorted()); // 발해
            heritages.addAll(heritageService.findByCcceNameContainingList("통일신라", Sort.unsorted()));

        } else {
            heritages = heritageService.findByCcceNameContainingList(ccceMap.get(ccce), Sort.unsorted());

        }
        log.info("displayByCcce = " + ccce);
//        log.info(heritages.toString());
        return heritages;

    }

}

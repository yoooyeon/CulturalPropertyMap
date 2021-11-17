package com.yooyeon.culturalpropertymap.history.service;

import com.yooyeon.culturalpropertymap.config.auth.dto.SessionUser;
import com.yooyeon.culturalpropertymap.heritage.domain.Heritage;
import com.yooyeon.culturalpropertymap.heritage.repository.HeritageRepository;
import com.yooyeon.culturalpropertymap.heritage.service.HeritageService;
import com.yooyeon.culturalpropertymap.history.domain.ReadingHistory;
import com.yooyeon.culturalpropertymap.history.repository.ReadingHistoryRepository;
import com.yooyeon.culturalpropertymap.user.repository.SessionUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReadingHistoryService {

    private final ReadingHistoryRepository readingHistoryRepository;
    private final HeritageRepository heritageRepository;
    private final HttpSession httpSession;
    private final SessionUserRepository sessionUserRepository;
    private final HeritageService heritageService;


    public void saveTimeInReadingHistory(String title, String place, Double time) {
        log.info("ReadingHistoryService: 열람 이력 시간 저장합니다.");
        // 현재 로그인한 유저 정보 가져오기
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


        // desc 중복되는 문화재는 있어도 된다. 한 유저에 대해서 같은 문화재가 있으면 안되는 것이다.
        List<Heritage> heritages = heritageRepository.findByCcbaMnm1AndCcbaLcad(title, place);
        if (!heritages.isEmpty()) {
            log.info(heritages + "");
            log.info(heritages.size() + " size()");
            // var_desc 1. 유저가 가지고 있는 열람 이력을 가져온다.
            List<ReadingHistory> readingHistoryByUser = readingHistoryRepository.findByUser(savedUser);
            ReadingHistory readingHistory;
            // var_desc 2.  그 중 readingHistory 가지고있는 searchKeyword 중 no(문화재) 있는지 확인한다.
            for (ReadingHistory history : readingHistoryByUser) {
                log.info("증복 확인.. 뭐지?");

                log.info((heritages.get(0).getNo()) + " " + history.getNo());

                if ((heritages.get(0).getNo()).equals(history.getNo())) {
                    // var_desc 3.  있으면 카운트를 늘려준다.
                    log.info("ReadingHistoryService saveTimeInReadingHistory / 이 유저가 열람 이력이 있는 문화재");

                    readingHistory = history.addCount();
                    readingHistory = readingHistory.addTime(time);
                    readingHistoryRepository.save(readingHistory);

                    return;
                }
            }


            // var_desc 4.  없으면 새로 저장한다.

            log.info("ReadingHistoryService saveTimeInReadingHistory / 이 유저가 처음 열람하는 문화재");

            readingHistory = ReadingHistory.builder().count(1).accumulatedReadingTime(time).user(savedUser).no(heritages.get(0).getNo()).build();
            readingHistory = readingHistoryRepository.save(readingHistory);
        }

    }

    public List<ReadingHistory> getReadingHistoryByUser(SessionUser user) {

        return readingHistoryRepository.findByUserOrderByModifiedDateDesc(user);
    }

    // desc 최근에 열람한 문화재 6개
    public List<ReadingHistory> getSixReadingHistoryByUser(SessionUser user) {
        List<ReadingHistory> temp = new ArrayList<>();

        List<ReadingHistory> history = readingHistoryRepository.findByUserOrderByModifiedDateDesc(user);
        int count = 0;
        if (history.isEmpty()) {
            return null;
        }
        for (ReadingHistory h : history) {

            temp.add(h);
            count += 1;
            if (count > 5) {
                break;

            }
        }
        log.info("getSixReadingHistoryByUser = ");
        log.info("ReadingHistory = " + temp.toString());
        return temp;
    }

    //desc 사용자가 열람한 내역을 정렬하여 top 10 가져온다.
    public List<ReadingHistory> findReadingHistoriesSortedTop10(SessionUser user) {
        return readingHistoryRepository.findTop10ByUserOrderByCountDesc(user);
    }

    public List<ReadingHistory> getReadingHistoryTop5() {
        return readingHistoryRepository.findTop5ByOrderByCountDesc();
    }

//    // desc 24시간 안에 열람한 문화재
//    public List<Heritage> getReadingHistoryWithin24() {
//        SessionUser user = (SessionUser) httpSession.getAttribute("user");
//        SessionUser savedUser;
//        // 이메일로 현재 저장된 유저인지 찾아보기
//        List<SessionUser> findUser = sessionUserRepository.findByEmail(user.getEmail());
//
//        if (findUser.isEmpty()) { // 유저 없으면 새로 저장
//            savedUser = sessionUserRepository.save(user);
//            log.info("!! 유저 새로 저장");
//        } else { // 있으면 그대로 사용
//            savedUser = findUser.get(0);
//            log.info("!! 기존 유저");
//        }
//
//        List<ReadingHistory> readingHistories = readingHistoryRepository.findTop10ByUserOrderByModifiedDateDesc(savedUser);
//        Set<Heritage> heritageSet = new HashSet<>();
//        String no;
//
//        for (ReadingHistory history : readingHistories) {
//            if (ChronoUnit.HOURS.between(history.getModifiedDate(), LocalDateTime.now()) > 12)
//                break; // desc 열람한지 24시간 지났다면 저장x
//            if (heritageSet.size() > 20) break; // 추천로직에서 계산할 때 지연될까봐 100개에서 자릅니다
//            no = history.getNo();
//            heritageSet.add(heritageService.findHeritageByNo(no));
//        }
//
//        return Lists.newArrayList(heritageSet);
//
//
//    }
}

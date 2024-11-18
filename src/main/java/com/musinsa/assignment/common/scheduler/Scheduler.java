package com.musinsa.assignment.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = "scheduling.enable", havingValue = "true")
@Component
public class Scheduler {

    @Value("${server.port}")
    private String port;

    /**
     * 본 스케줄러는 "과제 전용"으로, [구현2] 단일 브랜드 묶음구매 최저가 API 를 위한 브랜드별 집계 배치 처리에 사용됨.
     * (복수의 인스턴스로 운영되는 실제 운영환경에서는 중복실행 되므로 사용할 수 없으며, 별도의 배치 서버에서 처리)
     * 과제이므로 빠른 업데이트 확인을 위해 20초 주기로 실행
     * */

    @Scheduled(fixedRate = 20000)
    public void run() {
        log.info("=============== [START] 과제용 Run scheduler (20초 주기) - {} ===============", LocalDateTime.now());
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://localhost:" + port;

        restTemplate.postForObject(baseUrl + "/brands/batch", null, String.class);
        log.info("=============== [END] 과제용 Run scheduler (20초 주기) - {} ===============", LocalDateTime.now());
    }

}

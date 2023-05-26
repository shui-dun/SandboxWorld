package com.shuidun.sandbox_town_backend.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
public class Timer {
    // 白天时长5分钟（300000ms），黄昏时长1分钟（60000ms），夜晚时长3分钟（180000ms），黎明时长1分钟（60000ms）
    // 因此，白天从0ms开始，黄昏从300000ms开始，夜晚从360000ms开始，黎明从540000ms开始
    // 一天总时长为10分钟（600000ms）

    @Scheduled(initialDelay = 0, fixedDelay = 600000)
    public void printMorning() {
        log.info("morning");
    }

    @Scheduled(initialDelay = 300000, fixedDelay = 600000)
    public void printDusk() {
        log.info("dusk");
    }

    @Scheduled(initialDelay = 360000, fixedDelay = 600000)
    public void printNight() {
        log.info("night");
    }

    @Scheduled(initialDelay = 540000, fixedDelay = 600000)
    public void printDawn() {
        log.info("dawn");
    }
}
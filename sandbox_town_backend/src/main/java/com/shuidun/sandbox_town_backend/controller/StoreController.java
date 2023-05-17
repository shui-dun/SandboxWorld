package com.shuidun.sandbox_town_backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/store")
public class StoreController {
    @RequestMapping("/foo")
    public String foo() {
        log.info("foo triggered");
        return "foo";
    }
}

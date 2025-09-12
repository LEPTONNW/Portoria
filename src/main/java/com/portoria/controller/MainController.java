package com.portoria.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Log4j2
public class MainController {
    @GetMapping("/")
    public String home() {
        return "main";
    }

    @GetMapping("/createbrand")
    public String createBrand() {
        return "createbrand";
    }

    @GetMapping("/aiwine")
    public String aiwine() {
        return "aiwine";
    }

    //asdashdhadksahdh
}

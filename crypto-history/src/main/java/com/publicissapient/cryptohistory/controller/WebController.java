package com.publicissapient.cryptohistory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping
    public String redirect() {
        return "forward:/index.html";
    }
}

package com.lewisallen.rtdptiCache.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping(value = "/")
    public String showTitle() {
        return "title";
    }
}

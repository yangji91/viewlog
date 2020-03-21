package com.ronfton.viewlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liubinqiang
 */
@Controller
public class HomeController {
    @RequestMapping("/")
    public String index() {
        return "forward:/viewlog";
    }
}

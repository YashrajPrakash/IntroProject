package com.letsGo.me.learningSB;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeeloWorld {

    @GetMapping("/")
    public String helllo(){
        return "Helllo world from Yash!";
    }
}

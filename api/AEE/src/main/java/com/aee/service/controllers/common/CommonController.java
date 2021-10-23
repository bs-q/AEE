package com.aee.service.controllers.common;

import com.aee.service.context.Milk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/common")
public class CommonController {

    @Qualifier("getCowMilk")
    @Autowired
    Milk.CowMilk cowMilk;

    @Qualifier("getCheapCowMilk")
    @Autowired
    Milk.CowMilk cheapCowMilk;

    @Autowired
    Milk.GoatMilk goatMilk;

    @GetMapping("/cow_milk")
    public List<Milk.CowMilk> getCowMilk() {
        List<Milk.CowMilk> l = new ArrayList<>();
        l.add(cowMilk);
        l.add(cheapCowMilk);
        return l;
    }
    @GetMapping("/chicken_milk")
    public Milk.GoatMilk getChickenMilk() {
        return goatMilk;
    }

}

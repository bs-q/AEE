package com.aee.service.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Milk {

    public static class CowMilk {
        public String brand = "DI";
        public Integer price = 999;
    }
    public static class GoatMilk {
        public String brand = "IOC";
        public Integer price = 0;
    }

    @Bean
    public CowMilk getCowMilk(){ return  new CowMilk();}

    @Bean
    public CowMilk getCheapCowMilk(){
            CowMilk t = new CowMilk();
            t.price = 2;
            return t;
    }
    @Bean
    public GoatMilk getGoatMilk(){ return  new GoatMilk();}


}

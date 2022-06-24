package com.rjgf.threecost.crawdata.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author linch
 * @create 2022/3/24 15:12
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String getHello(){
        return "8084 端口网站";
    }


    @RequestMapping(value = "/testPost", method = RequestMethod.POST)
    public String testPost(String test1){
        System.out.println(test1);
        return "8084 端口网站";
    }


}

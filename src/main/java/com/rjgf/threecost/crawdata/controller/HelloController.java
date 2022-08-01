package com.rjgf.threecost.crawdata.controller;

import com.rjgf.threecost.crawdata.service.SbDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;

/**
 * @author linch
 * @create 2022/3/24 15:12
 */
@Slf4j
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

    @RequestMapping(value = "/testPosts", method = RequestMethod.POST)
    public String testPosts(String test1){
        System.out.println(test1);
        return "8084 端口网站";
    }

    @RequestMapping(value = "/testPosts3", method = RequestMethod.POST)
    public String testPosts3(String test1){
        System.out.println(test1);
        return "8084 端口网站";
    }


    @RequestMapping(value = "/testPost2e", method = RequestMethod.POST)
    public String testPost2e(String test1){
        System.out.println(test1);
        return "8084 端口网站";
    }

    @RequestMapping(value = "/testPost3443", method = RequestMethod.POST)
    public String testPost3443(String test1){
        System.out.println(test1);
        return "8084 端口网站";
    }


    @Resource
    SbDataService sbDataService;

    @RequestMapping(value = "/testAsync", method = RequestMethod.GET)
    public String testAsync(String test1) throws FileNotFoundException {
        log.info("start testAsync");
        sbDataService.testAsync();
        log.info("end testAsync and return");
        return "8084 端口网站";
    }





}

package com.rjgf.threecost.crawdata.controller;

import com.rjgf.threecost.crawdata.async.MyAsync;
import com.rjgf.threecost.crawdata.service.SbDataService;
import com.rjgf.threecost.crawdata.util.aspect.RequestTimeAnnotation;
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
@RequestTimeAnnotation("testController")
@Slf4j
@RestController
public class HelloController {
//    @RequestTimeAnnotation
    @RequestMapping("/hello")
    public String getHello(){
        return "8084 端口网站";
    }

//    @RequestTimeAnnotation
    @RequestMapping(value = "/testPost", method = RequestMethod.GET)
    public String testPost(String test1){
        System.out.println(test1);
        return "8084 端口网站";
    }

//    @RequestTimeAnnotation
    @RequestMapping(value = "/testPosts", method = RequestMethod.GET)
    public String testPosts(String test1){
        System.out.println(test1);
        return "8084 端口网站";
    }


    @RequestMapping(value = "/testPosts3", method = RequestMethod.GET)
    public String testPosts3(String test1){
        System.out.println(test1);
        return "8084 端口网站";
    }


    @RequestMapping(value = "/testPost2e", method = RequestMethod.GET)
    public String testPost2e(String test1){
        System.out.println(test1);
        return "8084 端口网站";
    }

    @RequestMapping(value = "/testPost3443", method = RequestMethod.GET)
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

    @RequestMapping(value = "/testRollback", method = RequestMethod.GET)
    public String testRollback(){
        log.info("start testAsync");
        sbDataService.testRollback();
        log.info("end testAsync and return");
        return "8084 端口网站";
    }

    @RequestMapping(value = "/testAsyncError", method = RequestMethod.GET)
    public String testAsyncError(){
        log.info("test testAsyncError");
        sbDataService.testAsyncError();
        return "8084 端口网站";
    }

    @RequestMapping(value = "/testAsyncDefault", method = RequestMethod.GET)
    public String testAsyncDefault(){
        log.info("test testAsyncDefault");
        sbDataService.testAsyncDefault();
        return "8084 端口网站";
    }

    @RequestMapping(value = "/testJconsole", method = RequestMethod.GET)
    public String testJconsole() throws InterruptedException {
//        MyAsync.test();

        return "8084 端口网站 testJconsole";
    }

    @RequestMapping(value = "/testMethodException", method = RequestMethod.GET)
    public String testMethodException(){
        sbDataService.testMethodException();
        return "testMethodException";
    }

}

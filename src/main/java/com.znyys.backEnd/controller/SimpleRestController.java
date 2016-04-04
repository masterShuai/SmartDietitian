package com.znyys.backEnd.controller;

import org.springframework.web.bind.annotation.PathVariable;//URL中的变量
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;//支持HTTP方法
import org.springframework.web.bind.annotation.RestController;
/**
 * Created by wangshuai on 2016/4/4.
 */
@RestController
public class SimpleRestController {
    //匹配多个URL
    @RequestMapping("/")
    public String index() {
        return "Index Page";
    }

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    //URL中的变量
    @RequestMapping("/users/{username}")
    public String userProfile(@PathVariable("username") String username) {
        return String.format("user %s", username);
    }

    @RequestMapping("/posts/{id}")
    public String post(@PathVariable("id") int id) {
        return String.format("post %d", id);
    }

    //支持HTTP方法
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGet() {
        return "Login Page";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginPost() {
        return "Login Post Request";
    }
}
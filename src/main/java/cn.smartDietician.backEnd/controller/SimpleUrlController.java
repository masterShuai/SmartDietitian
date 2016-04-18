package cn.smartDietician.backEnd.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Created by wangshuai on 2016/4/4.
 */

@Controller
public class SimpleUrlController {
    @RequestMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }

    @RequestMapping(value = "/marster/test")
    public String home() {
        return "test";
    }
}

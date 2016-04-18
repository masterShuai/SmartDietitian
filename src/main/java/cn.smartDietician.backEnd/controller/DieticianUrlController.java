package cn.smartDietician.backEnd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wangshuai on 2016/4/17.
 */
@Controller
public class DieticianUrlController {
    @RequestMapping("/")
    public String searchHome() {
        return "homePage1";
    }
}

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

    @RequestMapping("/food")
    public String foodDetail() {
        return "fooddetail";
    }

    @RequestMapping("/DiyCooking")
    public String diyCooking() { return "DiyCooking";}

    @RequestMapping("/personalcenter")
    public String personalcenter() { return "personalcenter"; }

    @RequestMapping("/register")
    public String register(){ return "register";}

    @RequestMapping("/signIn")
    public String signin(){ return "signIn";}
}

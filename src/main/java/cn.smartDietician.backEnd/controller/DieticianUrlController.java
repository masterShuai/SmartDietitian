package cn.smartDietician.backEnd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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



    @RequestMapping(value = "/searchResult",method= RequestMethod.GET)
    public String sitExamSingleAnswer(@RequestParam(value = "searchContent",
            required=true) String content, Model model)
    {
        model.addAttribute("searchContent",content);
        return "searchResult";
    }
}

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

    @RequestMapping("/personalcenter")
    public String personalcenter() { return "personalcenter"; }

    @RequestMapping("/smartCatering")
    public String smartCatering() { return "smartCatering"; }

    @RequestMapping("/register")
    public String register(){ return "register";}

    @RequestMapping("/login")
    public String signin(){ return "login";}

    @RequestMapping(value = "/search",method= RequestMethod.POST)
    public String search(@RequestParam(value = "searchContent",
            required=true) String content, Model model)
    {
        System.out.println(content);
        model.addAttribute("searchContent",content);
        return "searchResult";
    }

    @RequestMapping(value = "/searchResult",method= RequestMethod.GET)
    public String searchWeb(@RequestParam(value = "searchContent",
            required=true) String content, Model model)
    {
        System.out.println(content);
        model.addAttribute("searchContent",content);
        return "searchResult";
    }

    @RequestMapping(value = "/nutritionDetail",method= RequestMethod.POST)
    public String searchNutrition(@RequestParam(value = "nutritionId",
            required=true) String content, Model model)
    {
        System.out.println(content);
        model.addAttribute("nutritionId",content);
        return "nutritionDetail";
    }

    @RequestMapping(value = "/foodDetail",method= RequestMethod.POST)
    public String searchFood(@RequestParam(value = "foodId",
            required=true) String content, Model model)
    {
        System.out.println(content);
        model.addAttribute("foodId",content);
        return "foodDetail";
    }

    @RequestMapping(value = "/cookingDetail",method= RequestMethod.POST)
    public String searchCooking(@RequestParam(value = "cookingId",
            required=true) String content, Model model)
    {
        System.out.println(content);
        model.addAttribute("cookingId",content);
        return "cookingDetail";
    }

    @RequestMapping("/diyCooking")
    public String diyCooking() {
        return "diyCooking";
    }

    @RequestMapping("/marster/test")
    public String test() {
        return "test";
    }
}

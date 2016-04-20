package cn.smartDietician.backEnd.controller;

import cn.smartDietician.backEnd.domain.entity.CookingContent;
import cn.smartDietician.backEnd.protocol.ResponseContent;
import cn.smartDietician.backEnd.protocol.SalerIdPassword;
import cn.smartDietician.backEnd.protocol.SalerUserReqDate;
import cn.smartDietician.backEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by wangshuai on 2016/4/17.
 */
@RestController
@RequestMapping(value = "/User")
public class UserRestController {
    @Autowired
    private UserService userService;

    @RequestMapping(value="/test", method= RequestMethod.GET)
    public ResponseContent test(){
        return ResponseContent.makeSuccessResponse(new SalerUserReqDate());
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseContent login(@RequestBody SalerIdPassword idPassword,
                                 HttpServletRequest request,
                                 HttpSession session) {
        if (userService.doLogin(idPassword)){
            session.setAttribute("User",idPassword.getID());
            return ResponseContent.makeSuccessResponse("success!");
        }else{
            return ResponseContent.makeFailResponse();
        }
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ResponseContent register(@RequestBody SalerUserReqDate userReqDate) {
        System.out.println("start register-----------");
        if (userService.addUser(userReqDate)) {
            System.out.println("register success-----------");
            return ResponseContent.makeSuccessResponse("success!");
        }
        else{
            System.out.println("register false-----------");
            return ResponseContent.makeFailResponse();
        }
    }

    @RequestMapping(value = "/getTodayCooking",method = RequestMethod.POST)
    public ResponseContent getTodayCooking(HttpServletRequest request,
                                           HttpSession session) {
        String userId;
        if(session.getAttribute("User")!=null){
            userId = session.getAttribute("User").toString();
            return ResponseContent.makeSuccessResponse(userService.getTodayDiet(userId));
        }
        else
            return ResponseContent.makeFailResponse();
    }

    @RequestMapping(value = "/setTodayCooking",method = RequestMethod.POST)
    public ResponseContent getTodayCooking(@RequestBody CookingContent cookingContent,
                                           HttpServletRequest request,
                                           HttpSession session) {
        String userId;
        if(session.getAttribute("User")!=null){
            userId = session.getAttribute("User").toString();
            return ResponseContent.makeSuccessResponse(userService.addTodayDiet(userId, cookingContent));
        }
        else
            return ResponseContent.makeFailResponse();
    }
}

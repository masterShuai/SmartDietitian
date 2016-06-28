package cn.smartDietician.backEnd.controller;

import cn.smartDietician.backEnd.domain.entity.CookingContent;
import cn.smartDietician.backEnd.protocol.ResponseContent;
import cn.smartDietician.backEnd.protocol.SalerIdPassword;
import cn.smartDietician.backEnd.protocol.SalerUserReqDate;
import cn.smartDietician.backEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseContent test() {
        return ResponseContent.makeSuccessResponse(new SalerUserReqDate());
    }

    @RequestMapping(value = "/getIdPw", method = RequestMethod.GET)
    public ResponseContent getIdPw() {
        return ResponseContent.makeSuccessResponse(new SalerIdPassword());
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseContent login(@RequestBody SalerIdPassword idPassword,
                                 HttpServletRequest request,
                                 HttpSession session) {
        if (userService.doLogin(idPassword)) {
            session.setAttribute("User",idPassword.getID());
            System.out.println("User login by id ="+session.getAttribute("User"));
            return ResponseContent.makeSuccessResponse("success");
        } else {
            return ResponseContent.makeFailResponse("登录失败!");
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseContent register(@RequestBody SalerUserReqDate userReqDate) {
        System.out.println("start register-----------");
        if (userService.addUser(userReqDate)) {
            System.out.println("register success-----------");
            return ResponseContent.makeSuccessResponse("success!");
        } else {
            System.out.println("register false-----------");
            return ResponseContent.makeFailResponse("注册失败");
        }
    }

    @RequestMapping(value = "/isLogin", method = RequestMethod.POST)
    public ResponseContent isLogin( HttpServletRequest request, HttpSession session) {

        if (session.getAttribute("User")!=null) {
           // System.out.println("用户已登陆!");
            return ResponseContent.makeSuccessResponse("success");
        }else{
           // System.out.println("未登录异常:"+session.getAttribute("User"));
            return ResponseContent.makeFailResponse();
        }
    }

    /**
     * 根据session获取用户所有信息
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/getUserById", method = RequestMethod.POST)
    public ResponseContent getUserById( HttpServletRequest request, HttpSession session) {

        if (session.getAttribute("User")==null) {
            return ResponseContent.makeFailResponse();
           // System.out.println("用户已登陆!");

        }else{
           // System.out.println("未登录异常:"+session.getAttribute("User"));

            String userId = session.getAttribute("User").toString();
            SalerUserReqDate userReqDate= userService.getUserById(userId);
            return ResponseContent.makeSuccessResponse(userReqDate);
        }
    }

    @RequestMapping(value = "/getTodayCooking", method = RequestMethod.POST)
    public ResponseContent getTodayCooking(HttpServletRequest request,
                                           HttpSession session) {
        String userId = session.getAttribute("User").toString();
        if (userId != null && userId!="") {
            userId = session.getAttribute("User").toString();
            return ResponseContent.makeSuccessResponse(userService.getTodayDiet(userId));
        } else
            return ResponseContent.makeFailResponse();
    }

    @RequestMapping(value = "/setTodayCooking", method = RequestMethod.POST)
    public ResponseContent setTodayCooking(@RequestBody CookingContent cookingContent,
                                           HttpServletRequest request,
                                           HttpSession session) {
        String userId;
        if (session.getAttribute("User") != null) {
            userId = session.getAttribute("User").toString();
            if (userService.addTodayDiet(userId, cookingContent)){
                return ResponseContent.makeSuccessResponse("scuccess");
            }
            else{
                return ResponseContent.makeFailResponse();
            }

        } else
            return ResponseContent.makeFailResponse();
    }
}

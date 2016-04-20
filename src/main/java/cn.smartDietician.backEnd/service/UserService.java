package cn.smartDietician.backEnd.service;


import cn.smartDietician.backEnd.domain.entity.*;
import cn.smartDietician.backEnd.domain.repository.UserCookingRepository;
import cn.smartDietician.backEnd.domain.repository.UserNutritionMaxRepository;
import cn.smartDietician.backEnd.domain.repository.UserNutritionMinRepository;
import cn.smartDietician.backEnd.domain.repository.UserRepository;
import cn.smartDietician.backEnd.domain.uionPK.UserCookingUionPK;
import cn.smartDietician.backEnd.domain.uionPK.UserNutritionUionPK;
import cn.smartDietician.backEnd.protocol.SalerIdPassword;
import cn.smartDietician.backEnd.protocol.SalerUserCookingReqDate;
import cn.smartDietician.backEnd.protocol.SalerUserReqDate;
import cn.smartDietician.backEnd.utils.CollectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangshuai on 2016/4/17.
 */
@Service
@CacheConfig(cacheNames = {
        "doLogin",
        "isUserIdExist",
        "getUserById",
        "getTodayDiet"
})
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCookingRepository userCookingRepository;
    @Autowired
    private UserNutritionMinRepository userNutritionMinRepository;
    @Autowired
    private UserNutritionMaxRepository userNutritionMaxRepository;

    //全部用户表信息
    private List<User> userList = new ArrayList<>();

    //<用户名,密码>
    private Hashtable<String, String> UserIdPassword = new Hashtable<>();
    //全部用户_营养元素_推荐摄入量表信息
    private List<User_Nutrition_Min> userNutritionMinList = new ArrayList<>();
    //全部用户_营养元素_摄入上限表信息
    private List<User_Nutrition_Max> userNutritionMaxList = new ArrayList<>();
    //全部用户_选用菜品表信息
    private List<User_Cooking> userCookingList = new ArrayList<>();

    @PostConstruct
    public void Init() {
        //iterable转List
        CollectionHelper<Nutrition> helper = new CollectionHelper<>();
        //读取全部用户
        userList = helper.iterableToList(userRepository.findAll());
        System.out.println(String.format("加载到用户数量：%d", userList.size()));
        //将用户名,密码存入hashtable
        for (int i = 0; i < userList.size(); i++) {
            UserIdPassword.put(userList.get(i).getId(), userList.get(i).getPassword());
        }
        System.out.println(String.format("加载到用户-密码数量：%d", UserIdPassword.size()));
        //读取全部用户营养推荐摄入量
        userNutritionMinList = helper.iterableToList(userNutritionMinRepository.findAll());
        System.out.println(String.format("加载到用户摄入下限数量：%d", userList.size()));
        //读取全部用户营养摄入量上限
        userNutritionMaxList = helper.iterableToList(userNutritionMaxRepository.findAll());
        System.out.println(String.format("加载到用户摄入上限数量：%d", userList.size()));
        //读取全部用户原创菜品表
        userCookingList = helper.iterableToList(userCookingRepository.findAll());
        System.out.println(String.format("加载到用户原创菜品数量：%d", userList.size()));
    }

    /**
     * 用户登录验证
     *
     * @param ip
     * @return 用户名密码正确返回true, 否则返回false
     */
    @Cacheable({"doLogin"})
    public boolean doLogin(SalerIdPassword ip) {
        if (UserIdPassword.get(ip.getID().trim()).equals(ip.getPassWord().trim()))
            return true;
        else
            return false;
    }

    /**
     * 根据用户ID获取所有信息
     *
     * @param id
     */
    @Cacheable({"getUserById"})
    public SalerUserReqDate getUserById(String id) {
        SalerUserReqDate user = new SalerUserReqDate();
        return user;
    }

    /**
     * 判断用户名是否存在
     *
     * @param ID
     * @return 存在为true;不存在为false
     */
    @Cacheable({"isUserIdExist"})
    public boolean isExist(String ID) {
        if (UserIdPassword.get(ID) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取今日饮食计划
     * @param id
     * @return
     */
    @Cacheable({"getTodayDiet"})
    public SalerUserCookingReqDate getTodayDiet(String id){
        SalerUserCookingReqDate salerUserCookingReqDate = new SalerUserCookingReqDate();
        Date today = new Date();
        User_Cooking uc= new User_Cooking();
        for(int i=0;i<userCookingList.size();i++)
        {
            uc = userCookingList.get(i);
            if(isSameDate(uc.getTime(),today)&&uc.getUionPK().getUserId().equals(id)){
                CookingContent cc = new CookingContent();
                cc.cookingId = uc.getUionPK().getCookingId();
                cc.content = uc.getContent();
                cc.numb = uc.getPeople();
                salerUserCookingReqDate.cookingContent.add(cc);
            }
        }
        return salerUserCookingReqDate;
    }

    public boolean addTodayDiet(String userId,CookingContent cookingContent){
        boolean result = false;
        User_Cooking userCooking = new User_Cooking();
        User_Cooking oldDate = new User_Cooking();
        int state = 0;
        UserCookingUionPK ucPK = new UserCookingUionPK(userId,cookingContent.cookingId);
        try {
            if(userCookingRepository.exists(ucPK)){
                oldDate = userCookingRepository.findOne(ucPK);
                state = 1;
                userCookingRepository.delete(ucPK);
                state = 2;
            }

            userCooking.setUionPK(ucPK);
            userCooking.setContent(cookingContent.content);
            userCooking.setPeople(cookingContent.content);
            userCooking.setTime(new Date());
            userCookingRepository.save(userCooking);
            userCookingList.add(userCooking);
            result = true;
        }catch (Exception e){
            if (state == 1){

            }
            result = false;
        }
        return result;
    }


    /**
     * 用户注册
     *
     * @param user
     * @return 成功返回true 失败返回false
     */
    public boolean addUser(SalerUserReqDate user) {
        if (isExist(user.getId())) {
            System.out.println("用户名已存在");
            return false;
        }
        int state = 0;
        User newUser = new User();
        List<User_Nutrition_Min> unml = new ArrayList<>();
        List<User_Nutrition_Max> unMaxl = new ArrayList<>();

        try {
            newUser.setId(user.getId());
            newUser.setName(user.getName());
            newUser.setPassword(user.getPassword());
            System.out.println("用户信息复制");
            newUser.setBirthday((new SimpleDateFormat("yyyy-MM-dd")).parse(user.getBirthday()));
            System.out.println("转换生日成功");
            newUser.setWeight(user.getWeight());
            newUser.setSex(user.getSex());
            newUser.setPregnant(user.isPregnant());//是否怀孕
            newUser.setGestation(user.getGestation());//孕期
            newUser.setLactation(user.isLactation());//是否为乳母
            newUser.setManualWork(user.getManualWork());//体力劳动程度
            //save
            userRepository.save(newUser);
            System.out.println("用户写入成功");

            userList.add(newUser);
            System.out.println("用户存入内存");
            state = 1;
            unml = getMinNutrition(newUser);
            System.out.println("获取推荐营养摄入量");
            for (int i=0;i<unml.size();i++){
                System.out.println(unml.get(i).getContent());
                System.out.println(unml.get(i).getUionPK().getNutritionId());
                System.out.println(unml.get(i).getUionPK().getUserId());
                userNutritionMinRepository.save(unml.get(i));
                System.out.println("==================写入推荐摄入 第"+i+"个==========================");
            }

            userNutritionMinList.addAll(unml);
            state = 2;

          /*  unMaxl = getMaxNutrition(newUser);
            userNutritionMaxRepository.save(unMaxl);
            userNutritionMaxList.addAll(unMaxl);
            state = 3;*/


        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
            try {
                if (state >= 1){
                    userRepository.delete(newUser);
                    userList.remove(newUser);
                }
                if (state >= 2){
                    userNutritionMinRepository.delete(unml);
                    userNutritionMinList.removeAll(unml);
                }
               /* if(state>=3){
                    userNutritionMaxRepository.delete(unMaxl);
                    userNutritionMaxList.removeAll(unMaxl);
                }*/
                return false;
            } catch (Exception e2) {
                System.out.println("```````");
                System.out.println(e.toString());
                System.out.println(e.getMessage());
                System.out.println(e.toString());
                return false;
            }
        }
        return true;
    }


//######################### 内部调用函数 ############################################//

    /**
     * 比较两日期是否相等
     * @param date1
     * @param date2
     * @return
     */
    private static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

    /**
     * 根据用户信息,计算各营养元素每日推荐摄入
     *
     * @param user
     * @return
     */
    public List<User_Nutrition_Min> getMinNutrition(User user) {
        List<User_Nutrition_Min> nutritionMinContents = new ArrayList<>();

        java.util.Date date = new Date();
        java.util.Date mydate = user.getBirthday();
        float age = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000) + 1;

        //添加水的推荐摄入量
        nutritionMinContents.add(
                new User_Nutrition_Min(new UserNutritionUionPK(user.getId(),"1")
                       // ,(float)((user.getWeight()/0.45)*28.35)));
                        ,(float)(user.getWeight()*40)));
        //添加钴的每日摄入量
        nutritionMinContents.add(new User_Nutrition_Min(
                new UserNutritionUionPK(user.getId(),"17"),5));

        //添加胆固醇每日摄入量
        nutritionMinContents.add(new User_Nutrition_Min(
                new UserNutritionUionPK(user.getId(),"24"),50));

        //添加膳食纤维每日摄入量
        nutritionMinContents.add(new User_Nutrition_Min(
                new UserNutritionUionPK(user.getId(),"6"),25));

        //添加18岁以上蛋白质每日摄入量
        if(age>=18){
            if (user.getSex().trim().equals("男")) {
                //添加蛋白质每日摄入量
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"2"),65));
            }else{
                if (user.isPregnant()) {
                    if (user.getGestation() == 2) {
                        //添加蛋白质每日摄入量
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"2"),70));
                    } else if (user.getGestation() == 3) {
                        //添加蛋白质每日摄入量
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"2"),85));
                    }
                } else if (user.isLactation()) {
                    //添加蛋白质每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"2"),80));

                }else {
                    //添加蛋白质每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"2"),55));
                }
            }
        }

        /*
        判断年龄
         */
        if (age < 0.5) {
            //添加蛋白质每日摄入量
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"2"),9));
            //添加能量的每日摄入量
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"5"),(float)(user.getWeight()*90)));

            //添加11岁前 不区分男女 每日摄入 维生素,矿物质

            //VA 18
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"18"),300));
            //VE 23
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"23"),3));
            //VB1 19
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"19"),(float)0.1));
            //VB2 20
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"20"),(float)0.4));
            //烟酸 VB3 21
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"21"),(float)2));
            //VC 22
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"22"),(float)40));


            //Ca 10
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"10"),(float)200));
            //P 11
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"11"),(float)100));
            //K 7
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"7"),(float)350));
            //Mg 9
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"9"),(float)20));
            //Na 8
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"8"),(float)170));
            //Fe 12
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"12"),(float)0.3));
            //Zn 13
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"13"),(float)2));
            //硒 16
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"16"),(float)15));
            //Cu 14
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"14"),(float)0.3));
            //Mn 15
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"15"),(float)0.01));
            if (user.getSex().trim().equals("男")) {

                if (user.getManualWork() == 1) {

                } else if (user.getManualWork() == 2) {

                } else if (user.getManualWork() == 3) {

                }
            } else {
                if (user.getManualWork() == 1) {

                } else if (user.getManualWork() == 2) {

                } else if (user.getManualWork() == 3) {

                }
            }

            //添加脂肪,碳水化合物 每日摄入量
            for (int i = 0; i<nutritionMinContents.size();i++){
                if( nutritionMinContents.get(i).getUionPK().getNutritionId().equals("5")){

                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"3")
                            ,(float)0.48*nutritionMinContents.get(i).getContent()));

                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"4")
                            ,(float)0.6*nutritionMinContents.get(i).getContent()));
                }
            }


        } else if (age >= 0.5 && age < 1) {
            //添加蛋白质每日摄入量
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"2"),20));
            //添加能量的每日摄入量
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"5"),(float)(user.getWeight()*80)));

            //添加11岁前 不区分男女 每日摄入 维生素,矿物质

            //VA 18
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"18"),350));
            //VE 23
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"23"),4));
            //VB1 19
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"19"),(float)0.3));
            //VB2 20
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"20"),(float)0.5));
            //烟酸 VB3 21
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"21"),(float)3));
            //VC 22
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"22"),(float)40));


            //Ca 10
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"10"),(float)250));
            //P 11
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"11"),(float)180));
            //K 7
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"7"),(float)550));
            //Mg 9
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"9"),(float)65));
            //Na 8
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"8"),(float)350));
            //Fe 12
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"12"),(float)10));
            //Zn 13
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"13"),(float)3.5));
            //硒 16
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"16"),(float)20));
            //Cu 14
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"14"),(float)0.3));
            //Mn 15
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"15"),(float)0.7));

            if (user.getSex().trim().equals("男")) {
                if (user.getManualWork() == 1) {

                } else if (user.getManualWork() == 2) {

                } else if (user.getManualWork() == 3) {

                }
            } else {
                if (user.getManualWork() == 1) {

                } else if (user.getManualWork() == 2) {

                } else if (user.getManualWork() == 3) {

                }
            }
            //添加脂肪,碳水化合物 每日摄入量
            for (int i = 0; i<nutritionMinContents.size();i++){
                if( nutritionMinContents.get(i).getUionPK().getNutritionId().equals("5")){

                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"3")
                            ,(float)0.4*nutritionMinContents.get(i).getContent()));

                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"4")
                            ,(float)0.85*nutritionMinContents.get(i).getContent()));
                }
            }
        } else if (age >= 1 && age < 2) {
            //添加蛋白质每日摄入量
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"2"),25));

            //添加11岁前 不区分男女 每日摄入 维生素,矿物质

            //VA 18
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"18"),310));
            //VE 23
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"23"),6));
            //VB1 19
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"19"),(float)0.6));
            //VB2 20
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"20"),(float)0.6));
            //烟酸 VB3 21
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"21"),(float)6));
            //VC 22
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"22"),(float)40));


            //Ca 10
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"10"),(float)600));
            //P 11
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"11"),(float)300));
            //K 7
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"7"),(float)900));
            //Mg 9
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"9"),(float)140));
            //Na 8
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"8"),(float)700));
            //Fe 12
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"12"),(float)9));
            //Zn 13
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"13"),(float)4));
            //硒 16
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"16"),(float)25));
            //Cu 14
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"14"),(float)0.3));
            //Mn 15
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"15"),(float)1.5));

            if (user.getSex().trim().equals("男")) {
                //添加能量的每日摄入量
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"5"),900));
                if (user.getManualWork() == 1) {

                } else if (user.getManualWork() == 2) {

                } else if (user.getManualWork() == 3) {

                }
            } else {
                //添加能量的每日摄入量
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"5"),800));
                if (user.getManualWork() == 1) {

                } else if (user.getManualWork() == 2) {

                } else if (user.getManualWork() == 3) {

                }
            }
            //添加脂肪,碳水化合物 每日摄入量
            for (int i = 0; i<nutritionMinContents.size();i++){
                if( nutritionMinContents.get(i).getUionPK().getNutritionId().equals("5")){

                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"3")
                            ,(float)0.35*nutritionMinContents.get(i).getContent()));
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"4")
                            ,(float)0.5*nutritionMinContents.get(i).getContent()));
                }
            }
        } else if (age >= 2 && age < 3) {
            //添加蛋白质每日摄入量
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"2"),25));

            //添加11岁前 不区分男女 每日摄入 维生素,矿物质

            //VA 18
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"18"),310));
            //VE 23
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"23"),6));
            //VB1 19
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"19"),(float)0.6));
            //VB2 20
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"20"),(float)0.6));
            //烟酸 VB3 21
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"21"),(float)6));
            //VC 22
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"22"),(float)40));


            //Ca 10
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"10"),(float)600));
            //P 11
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"11"),(float)300));
            //K 7
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"7"),(float)900));
            //Mg 9
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"9"),(float)140));
            //Na 8
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"8"),(float)700));
            //Fe 12
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"12"),(float)9));
            //Zn 13
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"13"),(float)4));
            //硒 16
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"16"),(float)25));
            //Cu 14
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"14"),(float)0.3));
            //Mn 15
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"15"),(float)1.5));

            if (user.getSex().trim().equals("男")) {
                //添加能量的每日摄入量
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"5"),1100));
                if (user.getManualWork() == 1) {

                } else if (user.getManualWork() == 2) {

                } else if (user.getManualWork() == 3) {

                }
            } else {
                //添加能量的每日摄入量
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"5"),1000));
                if (user.getManualWork() == 1) {

                } else if (user.getManualWork() == 2) {

                } else if (user.getManualWork() == 3) {

                }
            }

            //添加脂肪,碳水化合物 每日摄入量
            for (int i = 0; i<nutritionMinContents.size();i++){
                if( nutritionMinContents.get(i).getUionPK().getNutritionId().equals("5")){

                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"3")
                            ,(float)0.35*nutritionMinContents.get(i).getContent()));
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"4")
                            ,(float)0.5*nutritionMinContents.get(i).getContent()));
                }
            }
        } else if (age >= 3 && age < 4) {
            //添加蛋白质每日摄入量
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"2"),250));

            //添加11岁前 不区分男女 每日摄入 维生素,矿物质

            //VA 18
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"18"),310));
            //VE 23
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"23"),6));
            //VB1 19
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"19"),(float)0.6));
            //VB2 20
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"20"),(float)0.6));
            //烟酸 VB3 21
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"21"),(float)6));
            //VC 22
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"22"),(float)40));


            //Ca 10
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"10"),(float)600));
            //P 11
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"11"),(float)300));
            //K 7
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"7"),(float)900));
            //Mg 9
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"9"),(float)140));
            //Na 8
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"8"),(float)700));
            //Fe 12
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"12"),(float)9));
            //Zn 13
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"13"),(float)4));
            //硒 16
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"16"),(float)25));
            //Cu 14
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"14"),(float)0.3));
            //Mn 15
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"15"),(float)1.5));

            if (user.getSex().trim().equals("男")) {
                //添加能量的每日摄入量
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"5"),1250));

                if (user.getManualWork() == 1) {

                } else if (user.getManualWork() == 2) {

                } else if (user.getManualWork() == 3) {

                }
            } else {
                //添加能量的每日摄入量
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"5"),1200));
                if (user.getManualWork() == 1) {

                } else if (user.getManualWork() == 2) {

                } else if (user.getManualWork() == 3) {

                }
            }

            //添加脂肪,碳水化合物 每日摄入量
            for (int i = 0; i<nutritionMinContents.size();i++){
                if( nutritionMinContents.get(i).getUionPK().getNutritionId().equals("5")){

                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"3")
                            ,(float)0.35*nutritionMinContents.get(i).getContent()));
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"4")
                            ,(float)0.5*nutritionMinContents.get(i).getContent()));
                }
            }
        } else if (age >= 4 && age < 5) {
            //添加蛋白质每日摄入量
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"2"),30));

            //添加11岁前 不区分男女 每日摄入 维生素,矿物质

            //VA 18
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"18"),360));
            //VE 23
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"23"),7));
            //VB1 19
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"19"),(float)0.8));
            //VB2 20
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"20"),(float)0.7));
            //烟酸 VB3 21
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"21"),(float)8));
            //VC 22
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"22"),(float)50));


            //Ca 10
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"10"),(float)800));
            //P 11
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"11"),(float)350));
            //K 7
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"7"),(float)1200));
            //Mg 9
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"9"),(float)160));
            //Na 8
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"8"),(float)900));
            //Fe 12
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"12"),(float)10));
            //Zn 13
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"13"),(float)5.5));
            //硒 16
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"16"),(float)30));
            //Cu 14
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"14"),(float)0.4));
            //Mn 15
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"15"),(float)2));

            if (user.getSex().trim().equals("男")) {
                //添加能量的每日摄入量
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"5"),1300));
                if (user.getManualWork() == 1) {

                } else if (user.getManualWork() == 2) {

                } else if (user.getManualWork() == 3) {

                }
            } else {
                //添加能量的每日摄入量
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"5"),1250));
                if (user.getManualWork() == 1) {

                } else if (user.getManualWork() == 2) {

                } else if (user.getManualWork() == 3) {

                }
            }
        } else if (age >= 5 && age < 6) {
            //添加蛋白质每日摄入量
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"2"),30));

            //添加11岁前 不区分男女 每日摄入 维生素,矿物质

            //VA 18
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"18"),360));
            //VE 23
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"23"),7));
            //VB1 19
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"19"),(float)0.8));
            //VB2 20
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"20"),(float)0.7));
            //烟酸 VB3 21
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"21"),(float)8));
            //VC 22
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"22"),(float)50));


            //Ca 10
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"10"),(float)800));
            //P 11
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"11"),(float)350));
            //K 7
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"7"),(float)1200));
            //Mg 9
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"9"),(float)160));
            //Na 8
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"8"),(float)900));
            //Fe 12
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"12"),(float)10));
            //Zn 13
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"13"),(float)5.5));
            //硒 16
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"16"),(float)30));
            //Cu 14
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"14"),(float)0.4));
            //Mn 15
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"15"),(float)2));

            if (user.getSex().trim().equals("男")) {
                //添加能量的每日摄入量
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"5"),1400));
                if (user.getManualWork() == 1) {

                } else if (user.getManualWork() == 2) {

                } else if (user.getManualWork() == 3) {

                }
            } else {
                //添加能量的每日摄入量
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"5"),1300));
                if (user.getManualWork() == 1) {

                } else if (user.getManualWork() == 2) {

                } else if (user.getManualWork() == 3) {

                }
            }
        } else if (age >= 6 && age < 7) {
            //添加蛋白质每日摄入量
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"2"),30));

            //添加11岁前 不区分男女 每日摄入 维生素,矿物质

            //VA 18
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"18"),360));
            //VE 23
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"23"),7));
            //VB1 19
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"19"),(float)0.8));
            //VB2 20
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"20"),(float)0.7));
            //烟酸 VB3 21
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"21"),(float)8));
            //VC 22
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"22"),(float)50));


            //Ca 10
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"10"),(float)800));
            //P 11
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"11"),(float)350));
            //K 7
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"7"),(float)1200));
            //Mg 9
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"9"),(float)160));
            //Na 8
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"8"),(float)900));
            //Fe 12
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"12"),(float)10));
            //Zn 13
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"13"),(float)5.5));
            //硒 16
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"16"),(float)30));
            //Cu 14
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"14"),(float)0.4));
            //Mn 15
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"15"),(float)2));

            if (user.getSex().trim().equals("男")) {
                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1400));

                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1600));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1800));
                }
            } else {
                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1250));

                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1450));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1650));

                }
            }
        } else if (age >= 7 && age < 8) {
            //添加蛋白质每日摄入量
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"2"),40));

            //添加11岁前 不区分男女 每日摄入 维生素,矿物质

            //VA 18
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"18"),500));
            //VE 23
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"23"),9));
            //VB1 19
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"19"),(float)1));
            //VB2 20
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"20"),(float)1));
            //烟酸 VB3 21
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"21"),(float)10.5));
            //VC 22
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"22"),(float)65));


            //Ca 10
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"10"),(float)1000));
            //P 11
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"11"),(float)470));
            //K 7
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"7"),(float)1500));
            //Mg 9
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"9"),(float)220));
            //Na 8
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"8"),(float)1200));
            //Fe 12
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"12"),(float)13));
            //Zn 13
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"13"),(float)7));
            //硒 16
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"16"),(float)40));
            //Cu 14
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"14"),(float)0.5));
            //Mn 15
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"15"),(float)3));

            if (user.getSex().trim().equals("男")) {

                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1500));
                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1700));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1900));
                }
            } else {
                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1350));

                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1550));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1750));
                }
            }
        }else if (age >= 8 && age < 9){
            //添加蛋白质每日摄入量
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"2"),40));

            //添加11岁前 不区分男女 每日摄入 维生素,矿物质

            //VA 18
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"18"),500));
            //VE 23
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"23"),9));
            //VB1 19
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"19"),(float)1));
            //VB2 20
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"20"),(float)1));
            //烟酸 VB3 21
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"21"),(float)10.5));
            //VC 22
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"22"),(float)65));


            //Ca 10
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"10"),(float)1000));
            //P 11
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"11"),(float)470));
            //K 7
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"7"),(float)1500));
            //Mg 9
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"9"),(float)220));
            //Na 8
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"8"),(float)1200));
            //Fe 12
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"12"),(float)13));
            //Zn 13
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"13"),(float)7));
            //硒 16
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"16"),(float)40));
            //Cu 14
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"14"),(float)0.5));
            //Mn 15
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"15"),(float)3));

            if (user.getSex().trim().equals("男")) {
                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1650));
                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1850));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2100));
                }
            } else {
                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1450));

                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1700));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1900));
                }
            }
        } else if (age >= 9 && age < 10) {
            //添加蛋白质每日摄入量
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"2"),40));
            //添加11岁前 不区分男女 每日摄入 维生素,矿物质

            //VA 18
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"18"),500));
            //VE 23
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"23"),9));
            //VB1 19
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"19"),(float)1));
            //VB2 20
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"20"),(float)1));
            //烟酸 VB3 21
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"21"),(float)10.5));
            //VC 22
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"22"),(float)65));


            //Ca 10
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"10"),(float)1000));
            //P 11
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"11"),(float)470));
            //K 7
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"7"),(float)1500));
            //Mg 9
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"9"),(float)220));
            //Na 8
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"8"),(float)1200));
            //Fe 12
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"12"),(float)13));
            //Zn 13
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"13"),(float)7));
            //硒 16
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"16"),(float)40));
            //Cu 14
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"14"),(float)0.5));
            //Mn 15
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"15"),(float)3));

            if (user.getSex().trim().equals("男")) {
                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1750));

                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2000));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2250));

                }
            } else {
                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1550));

                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1800));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2000));
                }
            }
        } else if (age >= 10 && age < 11) {
            //添加蛋白质每日摄入量
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"2"),40));

            //添加11岁前 不区分男女 每日摄入 维生素,矿物质

            //VA 18
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"18"),500));
            //VE 23
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"23"),9));
            //VB1 19
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"19"),(float)1));
            //VB2 20
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"20"),(float)1));
            //烟酸 VB3 21
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"21"),(float)10.5));
            //VC 22
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"22"),(float)65));


            //Ca 10
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"10"),(float)1000));
            //P 11
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"11"),(float)470));
            //K 7
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"7"),(float)1500));
            //Mg 9
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"9"),(float)220));
            //Na 8
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"8"),(float)1200));
            //Fe 12
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"12"),(float)13));
            //Zn 13
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"13"),(float)7));
            //硒 16
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"16"),(float)40));
            //Cu 14
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"14"),(float)0.5));
            //Mn 15
            nutritionMinContents.add(new User_Nutrition_Min(
                    new UserNutritionUionPK(user.getId(),"15"),(float)3));

            if (user.getSex().trim().equals("男")) {

                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1800));

                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2050));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2300));

                }
            } else {

                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1650));

                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1900));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2150));

                }
            }
        } else if (age >= 11 && age < 14) {
            if (user.getSex().trim().equals("男")) {
                //添加蛋白质每日摄入量
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"2"),60));
                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2050));

                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2350));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2600));
                }

                //区分男女的 每日摄入 维生素,矿物质

                //VA 18
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"18"),670));
                //VE 23
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"23"),13));
                //VB1 19
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"19"),(float)1.3));
                //VB2 20
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"20"),(float)1.3));
                //烟酸 VB3 21
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"21"),(float)14));
                //VC 22
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"22"),(float)90));


                //Ca 10
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"10"),(float)1200));
                //P 11
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"11"),(float)640));
                //K 7
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"7"),(float)1900));
                //Mg 9
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"9"),(float)300));
                //Na 8
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                //Fe 12
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"12"),(float)15));
                //Zn 13
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"13"),(float)10));
                //硒 16
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"16"),(float)55));
                //Cu 14
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"14"),(float)0.7));
                //Mn 15
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"15"),(float)4));

            } else {
                //添加蛋白质每日摄入量
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"2"),55));
                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1800));

                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2050));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2300));

                }

                //区分男女***的 每日摄入 维生素,矿物质

                //****VA 18
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"18"),630));
                //VE 23
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"23"),13));
                //***VB1 19
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"19"),(float)1.1));
                //***VB2 20
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"20"),(float)1.1));
                //***烟酸 VB3 21
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"21"),(float)12));
                //VC 22
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"22"),(float)90));


                //Ca 10
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"10"),(float)1200));
                //P 11
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"11"),(float)640));
                //K 7
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"7"),(float)1900));
                //Mg 9
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"9"),(float)300));
                //Na 8
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                //***Fe 12
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"12"),(float)18));
                //***Zn 13
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"13"),(float)9));
                //硒 16
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"16"),(float)55));
                //Cu 14
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"14"),(float)0.7));
                //Mn 15
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"15"),(float)4));
            }
        } else if (age >= 14 && age < 18) {
            if (user.getSex().trim().equals("男")) {
                //添加蛋白质每日摄入量
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"2"),75));
                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2500));

                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2850));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),3200));
                }

                //男性的 每日摄入 维生素,矿物质
                //****VA 18
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"18"),820));
                //VE 23
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"23"),14));
                //***VB1 19
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"19"),(float)1.6));
                //***VB2 20
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"20"),(float)1.5));
                //***烟酸 VB3 21
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"21"),(float)16));
                //VC 22
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"22"),(float)100));


                //Ca 10
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"10"),(float)1000));
                //P 11
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"11"),(float)710));
                //K 7
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"7"),(float)2200));
                //Mg 9 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"9"),(float)360));
                //Na 8
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"8"),(float)1600));
                //***Fe 12
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"12"),(float)16));
                //***Zn 13 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"13"),(float)14));
                //硒 16 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"16"),(float)65));
                //Cu 14 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"14"),(float)0.9));
                //Mn 15 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"15"),(float)4.9));

            } else {
                //女性
                if(user.isPregnant()){
                    //怀孕女性
                    if(user.getGestation() == 1){
                        //怀孕初期女性的 每日摄入 维生素,矿物质
                        //****VA 18
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"18"),620));
                        //VE 23
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"23"),14));
                        //***VB1 19
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"19"),(float)1.3));
                        //***VB2 20
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"20"),(float)1.2));
                        //***烟酸 VB3 21
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"21"),(float)13));
                        //VC 22
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"22"),(float)100));


                        //Ca 10
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"10"),(float)1000));
                        //P 11
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"11"),(float)710));
                        //K 7
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"7"),(float)2200));
                        //Mg 9
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"9"),(float)320));
                        //Na 8
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"8"),(float)1600));
                        //***Fe 12
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"12"),(float)18));
                        //***Zn 13
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"13"),(float)8.5));
                        //硒 16
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"16"),(float)60));
                        //Cu 14
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"14"),(float)0.8));
                        //Mn 15
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"15"),(float)4.5));

                    }else if(user.getGestation() == 2){
                        //怀孕中期女性 每日摄入 维生素,矿物质
                        //****VA 18
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"18"),890));
                        //VE 23
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"23"),14));
                        //***VB1 19
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"19"),(float)1.8));
                        //***VB2 20
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"20"),(float)1.7));
                        //***烟酸 VB3 21
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"21"),(float)16));
                        //VC 22
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"22"),(float)115));


                        //Ca 10
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"10"),(float)1200));
                        //P 11
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"11"),(float)710));
                        //K 7
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"7"),(float)2200));
                        //Mg 9
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"9"),(float)360));
                        //Na 8
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"8"),(float)1600));
                        //***Fe 12
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"12"),(float)22));
                        //***Zn 13
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"13"),(float)10.5));
                        //硒 16
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"16"),(float)65));
                        //Cu 14
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"14"),(float)0.9));
                        //Mn 15
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"15"),(float)4.9));

                    }else if (user.getGestation() == 3){
                        //怀孕晚期女性 每日摄入 维生素,矿物质
                        //****VA 18
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"18"),890));
                        //VE 23
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"23"),14));
                        //***VB1 19
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"19"),(float)1.9));
                        //***VB2 20
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"20"),(float)1.8));
                        //***烟酸 VB3 21
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"21"),(float)16));
                        //VC 22
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"22"),(float)115));


                        //Ca 10
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"10"),(float)1200));
                        //P 11
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"11"),(float)710));
                        //K 7
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"7"),(float)2200));
                        //Mg 9
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"9"),(float)360));
                        //Na 8
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"8"),(float)1600));
                        //***Fe 12
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"12"),(float)27));
                        //***Zn 13
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"13"),(float)10.5));
                        //硒 16
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"16"),(float)65));
                        //Cu 14
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"14"),(float)0.9));
                        //Mn 15
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"15"),(float)4.9));
                    }
                }else if(user.isLactation()){
                    //乳母的每日摄入 维生素,矿物质
                    //****VA 18
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"18"),1220));
                    //VE 23
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"23"),17));
                    //***VB1 19
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"19"),(float)1.6));
                    //***VB2 20
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"20"),(float)1.5));
                    //***烟酸 VB3 21
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"21"),(float)16));
                    //VC 22
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"22"),(float)150));


                    //Ca 10
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"10"),(float)1200));
                    //P 11
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"11"),(float)710));
                    //K 7
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"7"),(float)2600));
                    //Mg 9
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"9"),(float)320));
                    //Na 8
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"8"),(float)1600));
                    //***Fe 12
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"12"),(float)22));
                    //***Zn 13
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"13"),(float)13));
                    //硒 16
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"16"),(float)78));
                    //Cu 14
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"14"),(float)1.4));
                    //Mn 15
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"15"),(float)0.3));

                }else{
                    //普通女性每日摄入 维生素,矿物质
                    //****VA 18
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"18"),620));
                    //VE 23
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"23"),14));
                    //***VB1 19
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"19"),(float)1.3));
                    //***VB2 20
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"20"),(float)1.2));
                    //***烟酸 VB3 21
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"21"),(float)13));
                    //VC 22
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"22"),(float)100));


                    //Ca 10
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"10"),(float)1000));
                    //P 11
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"11"),(float)710));
                    //K 7
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"7"),(float)2200));
                    //Mg 9
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"9"),(float)320));
                    //Na 8
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"8"),(float)1600));
                    //***Fe 12
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"12"),(float)18));
                    //***Zn 13
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"13"),(float)8.5));
                    //硒 16
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"16"),(float)60));
                    //Cu 14
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"14"),(float)0.8));
                    //Mn 15
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"15"),(float)4.5));
                }

                //添加蛋白质每日摄入量
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"2"),60));
                if (user.getManualWork() == 1) {
                    if (user.isPregnant()) {
                        if (user.getGestation() == 2) {
                            //添加能量的每日摄入量
                            nutritionMinContents.add(new User_Nutrition_Min(
                                    new UserNutritionUionPK(user.getId(),"5"),2300));

                        } else if (user.getGestation() == 3) {
                            //添加能量的每日摄入量
                            nutritionMinContents.add(new User_Nutrition_Min(
                                    new UserNutritionUionPK(user.getId(),"5"),2450));

                        }
                    } else if (user.isLactation()) {
                        //添加能量的每日摄入量
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"5"),2500));

                    }else{
                        //添加能量的每日摄入量
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"5"),2000));
                    }
                } else if (user.getManualWork() == 2) {
                    if (user.isPregnant()) {
                        if (user.getGestation() == 2) {
                            //添加能量的每日摄入量
                            nutritionMinContents.add(new User_Nutrition_Min(
                                    new UserNutritionUionPK(user.getId(), "5"), 2600));

                        } else if (user.getGestation() == 3) {
                            //添加能量的每日摄入量
                            nutritionMinContents.add(new User_Nutrition_Min(
                                    new UserNutritionUionPK(user.getId(), "5"), 2750));

                        }
                    } else if (user.isLactation()) {
                        //添加能量的每日摄入量
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(), "5"), 2800));

                    }else {
                        //添加能量的每日摄入量
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(), "5"), 2300));
                    }

                } else if (user.getManualWork() == 3) {
                    if (user.isPregnant()) {
                        if (user.getGestation() == 2) {
                            //添加能量的每日摄入量
                            nutritionMinContents.add(new User_Nutrition_Min(
                                    new UserNutritionUionPK(user.getId(), "5"), 2850));

                        } else if (user.getGestation() == 3) {
                            //添加能量的每日摄入量
                            nutritionMinContents.add(new User_Nutrition_Min(
                                    new UserNutritionUionPK(user.getId(), "5"), 3000));

                        }
                    } else if (user.isLactation()) {
                        //添加能量的每日摄入量
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(), "5"), 3050));

                    }else {
                        //添加能量的每日摄入量
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(), "5"), 2550));
                    }
                }
            }
        } else if (age >= 18 && age < 50) {
            if (user.getSex().trim().equals("男")) {

                //男性的 每日摄入 维生素,矿物质
                //****VA 18
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"18"),800));
                //VE 23
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"23"),14));
                //***VB1 19
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"19"),(float)1.4));
                //***VB2 20
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"20"),(float)1.4));
                //***烟酸 VB3 21
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"21"),(float)15));
                //VC 22
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"22"),(float)100));


                //Ca 10
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"10"),(float)800));
                //P 11
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"11"),(float)720));
                //K 7
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                //Mg 9 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"9"),(float)330));
                //Na 8
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"8"),(float)1500));
                //***Fe 12
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"12"),(float)12));
                //***Zn 13 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"13"),(float)12.5));
                //硒 16 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"16"),(float)60));
                //Cu 14 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"14"),(float)0.8));
                //Mn 15 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"15"),(float)4.5));

                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2250));
                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2600));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),3000));

                }
            } else {
                //女性
                if(user.isPregnant()){
                    //怀孕女性
                    if(user.getGestation() == 1){
                        //怀孕初期女性的 每日摄入 维生素,矿物质
                        //****VA 18
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"18"),700));
                        //VE 23
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"23"),14));
                        //***VB1 19
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"19"),(float)1.2));
                        //***VB2 20
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"20"),(float)1.2));
                        //***烟酸 VB3 21
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"21"),(float)12));
                        //VC 22
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"22"),(float)100));


                        //Ca 10
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"10"),(float)800));
                        //P 11
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"11"),(float)720));
                        //K 7
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                        //Mg 9 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"9"),(float)370));
                        //Na 8
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"8"),(float)1500));
                        //***Fe 12
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"12"),(float)20));
                        //***Zn 13 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"13"),(float)9.5));
                        //硒 16 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"16"),(float)65));
                        //Cu 14 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"14"),(float)0.9));
                        //Mn 15 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"15"),(float)4.9));

                    }else if(user.getGestation() == 2){
                        //怀孕中期女性 每日摄入 维生素,矿物质
                        //****VA 18
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"18"),770));
                        //VE 23
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"23"),14));
                        //***VB1 19
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"19"),(float)1.4));
                        //***VB2 20
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"20"),(float)1.4));
                        //***烟酸 VB3 21
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"21"),(float)12));
                        //VC 22
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"22"),(float)115));


                        //Ca 10
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"10"),(float)1000));
                        //P 11
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"11"),(float)720));
                        //K 7
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                        //Mg 9 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"9"),(float)370));
                        //Na 8
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"8"),(float)1500));
                        //***Fe 12
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"12"),(float)24));
                        //***Zn 13 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"13"),(float)9.5));
                        //硒 16 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"16"),(float)65));
                        //Cu 14 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"14"),(float)0.9));
                        //Mn 15 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"15"),(float)4.9));

                    }else if (user.getGestation() == 3){
                        //怀孕晚期女性 每日摄入 维生素,矿物质
                        //****VA 18
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"18"),770));
                        //VE 23
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"23"),14));
                        //***VB1 19
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"19"),(float)1.5));
                        //***VB2 20
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"20"),(float)1.5));
                        //***烟酸 VB3 21
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"21"),(float)12));
                        //VC 22
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"22"),(float)115));


                        //Ca 10
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"10"),(float)1000));
                        //P 11
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"11"),(float)720));
                        //K 7
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                        //Mg 9 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"9"),(float)370));
                        //Na 8
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"8"),(float)1500));
                        //***Fe 12
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"12"),(float)29));
                        //***Zn 13 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"13"),(float)9.5));
                        //硒 16 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"16"),(float)65));
                        //Cu 14 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"14"),(float)0.9));
                        //Mn 15 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"15"),(float)4.9));
                    }
                }else if(user.isLactation()){
                    //乳母的每日摄入 维生素,矿物质
                    //****VA 18
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"18"),1300));
                    //VE 23
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"23"),17));
                    //***VB1 19
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"19"),(float)1.5));
                    //***VB2 20
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"20"),(float)1.5));
                    //***烟酸 VB3 21
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"21"),(float)15));
                    //VC 22
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"22"),(float)150));


                    //Ca 10
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"10"),(float)1000));
                    //P 11
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"11"),(float)720));
                    //K 7
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"7"),(float)2400));
                    //Mg 9 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"9"),(float)330));
                    //Na 8
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"8"),(float)1500));
                    //***Fe 12
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"12"),(float)24));
                    //***Zn 13 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"13"),(float)12));
                    //硒 16 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"16"),(float)78));
                    //Cu 14 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"14"),(float)1.4));
                    //Mn 15 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"15"),(float)4.8));

                }else{
                    //普通女性每日摄入 维生素,矿物质
                    //****VA 18
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"18"),700));
                    //VE 23
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"23"),14));
                    //***VB1 19
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"19"),(float)1.2));
                    //***VB2 20
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"20"),(float)1.2));
                    //***烟酸 VB3 21
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"21"),(float)12));
                    //VC 22
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"22"),(float)100));


                    //Ca 10
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"10"),(float)800));
                    //P 11
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"11"),(float)720));
                    //K 7
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                    //Mg 9 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"9"),(float)330));
                    //Na 8
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"8"),(float)1500));
                    //***Fe 12
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"12"),(float)20));
                    //***Zn 13 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"13"),(float)7.5));
                    //硒 16 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"16"),(float)60));
                    //Cu 14 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"14"),(float)0.8));
                    //Mn 15 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"15"),(float)4.5));
                }

                if (user.getManualWork() == 1) {
                    if (user.isPregnant()) {
                        if (user.getGestation() == 2) {
                            //添加能量的每日摄入量
                            nutritionMinContents.add(new User_Nutrition_Min(
                                    new UserNutritionUionPK(user.getId(), "5"), 2100));

                        } else if (user.getGestation() == 3) {
                            //添加能量的每日摄入量
                            nutritionMinContents.add(new User_Nutrition_Min(
                                    new UserNutritionUionPK(user.getId(), "5"), 2250));

                        }
                    } else if (user.isLactation()) {
                        //添加能量的每日摄入量
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(), "5"), 2300));

                    }else {
                        //添加能量的每日摄入量
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(), "5"), 1800));
                    }

                } else if (user.getManualWork() == 2) {
                    if (user.isPregnant()) {
                        if (user.getGestation() == 2) {
                            //添加能量的每日摄入量
                            nutritionMinContents.add(new User_Nutrition_Min(
                                    new UserNutritionUionPK(user.getId(), "5"), 2400));

                        } else if (user.getGestation() == 3) {
                            //添加能量的每日摄入量
                            nutritionMinContents.add(new User_Nutrition_Min(
                                    new UserNutritionUionPK(user.getId(), "5"), 2550));

                        }
                    } else if (user.isLactation()) {
                        //添加能量的每日摄入量
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(), "5"), 2600));

                    }else {
                        //添加能量的每日摄入量
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(), "5"), 2100));
                    }


                } else if (user.getManualWork() == 3) {
                    if (user.isPregnant()) {
                        if (user.getGestation() == 2) {
                            //添加能量的每日摄入量
                            nutritionMinContents.add(new User_Nutrition_Min(
                                    new UserNutritionUionPK(user.getId(), "5"), 2700));

                        } else if (user.getGestation() == 3) {
                            //添加能量的每日摄入量
                            nutritionMinContents.add(new User_Nutrition_Min(
                                    new UserNutritionUionPK(user.getId(), "5"), 2850));
                        }
                    } else if (user.isLactation()) {
                        //添加能量的每日摄入量
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(), "5"), 2400+500));
                    }else {
                        //添加能量的每日摄入量
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(), "5"), 2400));
                    }

                }
            }
        } else if (age >= 50 && age < 65) {
            if (user.getSex().trim().equals("男")) {
                //男性的 每日摄入 维生素,矿物质
                //****VA 18
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"18"),800));
                //VE 23
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"23"),14));
                //***VB1 19
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"19"),(float)1.4));
                //***VB2 20
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"20"),(float)1.4));
                //***烟酸 VB3 21
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"21"),(float)14));
                //VC 22
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"22"),(float)100));
                //Ca 10
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"10"),(float)1000));
                //P 11
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"11"),(float)720));
                //K 7
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                //Mg 9 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"9"),(float)330));
                //Na 8
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                //***Fe 12
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"12"),(float)12));
                //***Zn 13 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"13"),(float)12.5));
                //硒 16 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"16"),(float)60));
                //Cu 14 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"14"),(float)0.8));
                //Mn 15 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"15"),(float)4.5));
                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2100));

                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2450));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2800));

                }
            } else {
                //女性
                if(user.isPregnant()){
                    //怀孕女性
                    if(user.getGestation() == 1){
                        //怀孕初期女性的 每日摄入 维生素,矿物质
                        //****VA 18
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"18"),700));
                        //VE 23
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"23"),14));
                        //***VB1 19
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"19"),(float)1.2));
                        //***VB2 20
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"20"),(float)1.2));
                        //***烟酸 VB3 21
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"21"),(float)12));
                        //VC 22
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"22"),(float)100));


                        //Ca 10
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"10"),(float)1000));
                        //P 11
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"11"),(float)720));
                        //K 7
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                        //Mg 9 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"9"),(float)370));
                        //Na 8
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                        //***Fe 12
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"12"),(float)20));
                        //***Zn 13 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"13"),(float)9.5));
                        //硒 16 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"16"),(float)65));
                        //Cu 14 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"14"),(float)0.9));
                        //Mn 15 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"15"),(float)4.9));

                    }else if(user.getGestation() == 2){
                        //怀孕中期女性 每日摄入 维生素,矿物质
                        //****VA 18
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"18"),770));
                        //VE 23
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"23"),14));
                        //***VB1 19
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"19"),(float)1.4));
                        //***VB2 20
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"20"),(float)1.4));
                        //***烟酸 VB3 21
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"21"),(float)12));
                        //VC 22
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"22"),(float)115));


                        //Ca 10
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"10"),(float)1200));
                        //P 11
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"11"),(float)720));
                        //K 7
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                        //Mg 9 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"9"),(float)370));
                        //Na 8
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                        //***Fe 12
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"12"),(float)24));
                        //***Zn 13 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"13"),(float)9.5));
                        //硒 16 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"16"),(float)65));
                        //Cu 14 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"14"),(float)0.9));
                        //Mn 15 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"15"),(float)4.9));

                    }else if (user.getGestation() == 3){
                        //怀孕晚期女性 每日摄入 维生素,矿物质
                        //****VA 18
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"18"),770));
                        //VE 23
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"23"),14));
                        //***VB1 19
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"19"),(float)1.5));
                        //***VB2 20
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"20"),(float)1.5));
                        //***烟酸 VB3 21
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"21"),(float)12));
                        //VC 22
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"22"),(float)115));


                        //Ca 10
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"10"),(float)1200));
                        //P 11
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"11"),(float)720));
                        //K 7
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                        //Mg 9 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"9"),(float)370));
                        //Na 8
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                        //***Fe 12
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"12"),(float)29));
                        //***Zn 13 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"13"),(float)9.5));
                        //硒 16 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"16"),(float)65));
                        //Cu 14 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"14"),(float)0.9));
                        //Mn 15 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"15"),(float)4.9));
                    }
                }else if(user.isLactation()){
                    //乳母的每日摄入 维生素,矿物质
                    //****VA 18
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"18"),1300));
                    //VE 23
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"23"),17));
                    //***VB1 19
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"19"),(float)1.5));
                    //***VB2 20
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"20"),(float)1.5));
                    //***烟酸 VB3 21
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"21"),(float)15));
                    //VC 22
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"22"),(float)150));


                    //Ca 10
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"10"),(float)1200));
                    //P 11
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"11"),(float)720));
                    //K 7
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"7"),(float)2400));
                    //Mg 9 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"9"),(float)330));
                    //Na 8
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                    //***Fe 12
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"12"),(float)24));
                    //***Zn 13 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"13"),(float)12));
                    //硒 16 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"16"),(float)78));
                    //Cu 14 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"14"),(float)1.4));
                    //Mn 15 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"15"),(float)4.8));

                }else{
                    //普通女性每日摄入 维生素,矿物质
                    //****VA 18
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"18"),700));
                    //VE 23
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"23"),14));
                    //***VB1 19
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"19"),(float)1.2));
                    //***VB2 20
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"20"),(float)1.2));
                    //***烟酸 VB3 21
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"21"),(float)12));
                    //VC 22
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"22"),(float)100));


                    //Ca 10
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"10"),(float)1000));
                    //P 11
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"11"),(float)720));
                    //K 7
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                    //Mg 9 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"9"),(float)330));
                    //Na 8
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                    //***Fe 12
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"12"),(float)20));
                    //***Zn 13 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"13"),(float)7.5));
                    //硒 16 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"16"),(float)60));
                    //Cu 14 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"14"),(float)0.8));
                    //Mn 15 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"15"),(float)4.5));
                }

                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1750));

                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2050));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2750));

                }
            }
        } else if (age >= 65 && age < 80) {
            if (user.getSex().trim().equals("男")) {
                //男性的 每日摄入 维生素,矿物质
                //****VA 18
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"18"),800));
                //VE 23
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"23"),14));
                //***VB1 19
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"19"),(float)1.4));
                //***VB2 20
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"20"),(float)1.4));
                //***烟酸 VB3 21
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"21"),(float)14));
                //VC 22
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"22"),(float)100));


                //Ca 10
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"10"),(float)1000));
                //P 11
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"11"),(float)700));
                //K 7
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                //Mg 9 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"9"),(float)320));
                //Na 8
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                //***Fe 12
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"12"),(float)12));
                //***Zn 13 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"13"),(float)12.5));
                //硒 16 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"16"),(float)60));
                //Cu 14 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"14"),(float)0.8));
                //Mn 15 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"15"),(float)4.5));

                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2050));

                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2350));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2350));
                }
            } else {

                //女性
                if(user.isPregnant()){
                    //怀孕女性
                    if(user.getGestation() == 1){
                        //怀孕初期女性的 每日摄入 维生素,矿物质
                        //****VA 18
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"18"),700));
                        //VE 23
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"23"),14));
                        //***VB1 19
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"19"),(float)1.2));
                        //***VB2 20
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"20"),(float)1.2));
                        //***烟酸 VB3 21
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"21"),(float)11));
                        //VC 22
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"22"),(float)100));


                        //Ca 10
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"10"),(float)1000));
                        //P 11
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"11"),(float)700));
                        //K 7
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                        //Mg 9 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"9"),(float)360));
                        //Na 8
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                        //***Fe 12
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"12"),(float)20));
                        //***Zn 13 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"13"),(float)9.5));
                        //硒 16 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"16"),(float)65));
                        //Cu 14 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"14"),(float)0.9));
                        //Mn 15 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"15"),(float)4.9));

                    }else if(user.getGestation() == 2){
                        //怀孕中期女性 每日摄入 维生素,矿物质
                        //****VA 18
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"18"),770));
                        //VE 23
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"23"),14));
                        //***VB1 19
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"19"),(float)1.4));
                        //***VB2 20
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"20"),(float)1.4));
                        //***烟酸 VB3 21
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"21"),(float)11));
                        //VC 22
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"22"),(float)115));


                        //Ca 10
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"10"),(float)1200));
                        //P 11
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"11"),(float)700));
                        //K 7
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                        //Mg 9 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"9"),(float)360));
                        //Na 8
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                        //***Fe 12
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"12"),(float)24));
                        //***Zn 13 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"13"),(float)9.5));
                        //硒 16 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"16"),(float)65));
                        //Cu 14 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"14"),(float)0.9));
                        //Mn 15 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"15"),(float)4.9));

                    }else if (user.getGestation() == 3){
                        //怀孕晚期女性 每日摄入 维生素,矿物质
                        //****VA 18
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"18"),770));
                        //VE 23
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"23"),14));
                        //***VB1 19
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"19"),(float)1.5));
                        //***VB2 20
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"20"),(float)1.5));
                        //***烟酸 VB3 21
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"21"),(float)11));
                        //VC 22
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"22"),(float)115));


                        //Ca 10
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"10"),(float)1200));
                        //P 11
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"11"),(float)700));
                        //K 7
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                        //Mg 9 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"9"),(float)360));
                        //Na 8
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                        //***Fe 12
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"12"),(float)29));
                        //***Zn 13 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"13"),(float)9.5));
                        //硒 16 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"16"),(float)65));
                        //Cu 14 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"14"),(float)0.9));
                        //Mn 15 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"15"),(float)4.9));
                    }
                }else if(user.isLactation()){
                    //乳母的每日摄入 维生素,矿物质
                    //****VA 18
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"18"),1300));
                    //VE 23
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"23"),17));
                    //***VB1 19
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"19"),(float)1.5));
                    //***VB2 20
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"20"),(float)1.5));
                    //***烟酸 VB3 21
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"21"),(float)14));
                    //VC 22
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"22"),(float)150));


                    //Ca 10
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"10"),(float)1200));
                    //P 11
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"11"),(float)700));
                    //K 7
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"7"),(float)2400));
                    //Mg 9 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"9"),(float)320));
                    //Na 8
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                    //***Fe 12
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"12"),(float)24));
                    //***Zn 13 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"13"),(float)12));
                    //硒 16 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"16"),(float)78));
                    //Cu 14 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"14"),(float)1.4));
                    //Mn 15 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"15"),(float)4.8));

                }else{
                    //普通女性每日摄入 维生素,矿物质
                    //****VA 18
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"18"),700));
                    //VE 23
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"23"),14));
                    //***VB1 19
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"19"),(float)1.2));
                    //***VB2 20
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"20"),(float)1.2));
                    //***烟酸 VB3 21
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"21"),(float)11));
                    //VC 22
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"22"),(float)100));


                    //Ca 10
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"10"),(float)1000));
                    //P 11
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"11"),(float)700));
                    //K 7
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                    //Mg 9 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"9"),(float)320));
                    //Na 8
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                    //***Fe 12
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"12"),(float)20));
                    //***Zn 13 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"13"),(float)7.5));
                    //硒 16 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"16"),(float)60));
                    //Cu 14 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"14"),(float)0.8));
                    //Mn 15 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"15"),(float)4.5));
                }

                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1700));

                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1900));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1900));

                }
            }
        } else if (age >= 80) {
            if (user.getSex().trim().equals("男")) {
                //男性的 每日摄入 维生素,矿物质
                //****VA 18
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"18"),800));
                //VE 23
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"23"),14));
                //***VB1 19
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"19"),(float)1.4));
                //***VB2 20
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"20"),(float)1.4));
                //***烟酸 VB3 21
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"21"),(float)13));
                //VC 22
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"22"),(float)100));


                //Ca 10
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"10"),(float)1000));
                //P 11
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"11"),(float)670));
                //K 7
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                //Mg 9 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"9"),(float)310));
                //Na 8
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"8"),(float)1300));
                //***Fe 12
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"12"),(float)12));
                //***Zn 13 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"13"),(float)12.5));
                //硒 16 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"16"),(float)60));
                //Cu 14 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"14"),(float)0.8));
                //Mn 15 初期
                nutritionMinContents.add(new User_Nutrition_Min(
                        new UserNutritionUionPK(user.getId(),"15"),(float)4.5));

                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1900));

                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2200));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),2200));

                }
            } else {

                //女性
                if(user.isPregnant()){
                    //怀孕女性
                    if(user.getGestation() == 1){
                        //怀孕初期女性的 每日摄入 维生素,矿物质
                        //****VA 18
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"18"),700));
                        //VE 23
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"23"),14));
                        //***VB1 19
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"19"),(float)1.2));
                        //***VB2 20
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"20"),(float)1.2));
                        //***烟酸 VB3 21
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"21"),(float)10));
                        //VC 22
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"22"),(float)100));


                        //Ca 10
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"10"),(float)1000));
                        //P 11
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"11"),(float)670));
                        //K 7
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                        //Mg 9 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"9"),(float)350));
                        //Na 8
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                        //***Fe 12
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"12"),(float)20));
                        //***Zn 13 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"13"),(float)9.5));
                        //硒 16 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"16"),(float)65));
                        //Cu 14 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"14"),(float)0.9));
                        //Mn 15 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"15"),(float)4.9));

                    }else if(user.getGestation() == 2){
                        //怀孕中期女性 每日摄入 维生素,矿物质
                        //****VA 18
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"18"),770));
                        //VE 23
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"23"),14));
                        //***VB1 19
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"19"),(float)1.4));
                        //***VB2 20
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"20"),(float)1.4));
                        //***烟酸 VB3 21
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"21"),(float)10));
                        //VC 22
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"22"),(float)115));


                        //Ca 10
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"10"),(float)1200));
                        //P 11
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"11"),(float)670));
                        //K 7
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                        //Mg 9 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"9"),(float)350));
                        //Na 8
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                        //***Fe 12
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"12"),(float)24));
                        //***Zn 13 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"13"),(float)9.5));
                        //硒 16 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"16"),(float)65));
                        //Cu 14 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"14"),(float)0.9));
                        //Mn 15 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"15"),(float)4.9));

                    }else if (user.getGestation() == 3){
                        //怀孕晚期女性 每日摄入 维生素,矿物质
                        //****VA 18
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"18"),770));
                        //VE 23
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"23"),14));
                        //***VB1 19
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"19"),(float)1.5));
                        //***VB2 20
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"20"),(float)1.5));
                        //***烟酸 VB3 21
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"21"),(float)10));
                        //VC 22
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"22"),(float)115));


                        //Ca 10
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"10"),(float)1200));
                        //P 11
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"11"),(float)670));
                        //K 7
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                        //Mg 9 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"9"),(float)350));
                        //Na 8
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                        //***Fe 12
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"12"),(float)29));
                        //***Zn 13 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"13"),(float)9.5));
                        //硒 16 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"16"),(float)65));
                        //Cu 14 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"14"),(float)0.9));
                        //Mn 15 初期
                        nutritionMinContents.add(new User_Nutrition_Min(
                                new UserNutritionUionPK(user.getId(),"15"),(float)4.9));
                    }
                }else if(user.isLactation()){
                    //乳母的每日摄入 维生素,矿物质
                    //****VA 18
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"18"),1300));
                    //VE 23
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"23"),17));
                    //***VB1 19
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"19"),(float)1.5));
                    //***VB2 20
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"20"),(float)1.5));
                    //***烟酸 VB3 21
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"21"),(float)13));
                    //VC 22
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"22"),(float)150));


                    //Ca 10
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"10"),(float)1200));
                    //P 11
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"11"),(float)670));
                    //K 7
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"7"),(float)2400));
                    //Mg 9 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"9"),(float)310));
                    //Na 8
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                    //***Fe 12
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"12"),(float)24));
                    //***Zn 13 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"13"),(float)12));
                    //硒 16 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"16"),(float)78));
                    //Cu 14 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"14"),(float)1.4));
                    //Mn 15 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"15"),(float)4.8));

                }else{
                    //普通女性每日摄入 维生素,矿物质
                    //****VA 18
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"18"),700));
                    //VE 23
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"23"),14));
                    //***VB1 19
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"19"),(float)1.2));
                    //***VB2 20
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"20"),(float)1.2));
                    //***烟酸 VB3 21
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"21"),(float)10));
                    //VC 22
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"22"),(float)100));


                    //Ca 10
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"10"),(float)1000));
                    //P 11
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"11"),(float)670));
                    //K 7
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"7"),(float)2000));
                    //Mg 9 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"9"),(float)310));
                    //Na 8
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"8"),(float)1400));
                    //***Fe 12
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"12"),(float)20));
                    //***Zn 13 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"13"),(float)7.5));
                    //硒 16 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"16"),(float)60));
                    //Cu 14 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"14"),(float)0.8));
                    //Mn 15 初期
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"15"),(float)4.5));
                }

                if (user.getManualWork() == 1) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1500));

                } else if (user.getManualWork() == 2) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1750));

                } else if (user.getManualWork() == 3) {
                    //添加能量的每日摄入量
                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"5"),1750));

                }
            }
        }

        //添加4岁以上碳水化合物,脂肪 摄入量
        if(age>=4){
            for (int i = 0; i<nutritionMinContents.size();i++){
                if( nutritionMinContents.get(i).getUionPK().getNutritionId().equals("5")){

                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"3")
                            ,(float)0.2*nutritionMinContents.get(i).getContent()));

                    nutritionMinContents.add(new User_Nutrition_Min(
                            new UserNutritionUionPK(user.getId(),"4")
                            ,(float)0.5*nutritionMinContents.get(i).getContent()));
                }
            }
        }


        return nutritionMinContents;
    }

    /**
     * 根据用户信息,计算各营养元素每日最大摄入量
     *
     * @param user
     * @return
     */
    public List<User_Nutrition_Max> getMaxNutrition(User user) {
        List<User_Nutrition_Max> nutritionMaxContents = new ArrayList<>();

        java.util.Date date = new Date();
        java.util.Date mydate = user.getBirthday();
        float age = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000) + 1;
                /*
                判断0~1岁 还是 1~
                 */
        if (age < 0.5) {
            if (user.getSex().trim().equals("男")) {

            } else {

            }
        } else if (age >= 0.5 && age < 1) {
            if (user.getSex().trim().equals("男")) {

            } else {

            }
        } else if (age >= 1 && age < 2) {
            if (user.getSex().trim().equals("男")) {

            } else {

            }
        } else if (age >= 2 && age < 3) {
            if (user.getSex().trim().equals("男")) {

            } else {

            }
        } else if (age >= 3 && age < 4) {
            if (user.getSex().trim().equals("男")) {

            } else {

            }
        } else if (age >= 4 && age < 5) {
            if (user.getSex().trim().equals("男")) {

            } else {

            }
        } else if (age >= 5 && age < 6) {
            if (user.getSex().trim().equals("男")) {

            } else {

            }
        } else if (age >= 6 && age < 7) {
            if (user.getSex().trim().equals("男")) {

            } else {

            }
        } else if (age >= 7 && age < 8) {
            if (user.getSex().trim().equals("男")) {

            } else {

            }
        } else if (age >= 9 && age < 10) {
            if (user.getSex().trim().equals("男")) {

            } else {

            }
        } else if (age >= 10 && age < 11) {
            if (user.getSex().trim().equals("男")) {

            } else {

            }
        } else if (age >= 11 && age < 14) {
            if (user.getSex().trim().equals("男")) {

            } else {

            }
        } else if (age >= 14 && age < 18) {
            if (user.getSex().trim().equals("男")) {

            } else {

            }
        } else if (age >= 18 && age < 50) {
            if (user.getSex().trim().equals("男")) {

            } else {

            }
        } else if (age >= 50 && age < 65) {
            if (user.getSex().trim().equals("男")) {

            } else {

            }
        } else if (age >= 65 && age < 80) {
            if (user.getSex().trim().equals("男")) {

            } else {

            }
        } else if (age >= 80) {
            if (user.getSex().trim().equals("男")) {

            } else {

            }
        }


        if (user.getSex().trim().equals("女")) {
            if (user.isPregnant()) {
                if (user.getGestation() == 1) {

                } else if (user.getGestation() == 2) {

                } else if (user.getGestation() == 3) {

                }
            } else if (user.isLactation()) {

            }
        }


        return nutritionMaxContents;
    }
}




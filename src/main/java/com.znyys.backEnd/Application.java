package com.znyys.backEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * Created by wangshuai on 2016/4/4.
 */

@Configuration//配置控制
@EnableAutoConfiguration//启用自动配置
@ComponentScan//组件扫描

//@EnableCaching

public class Application {
        public static void main(String[] args) throws Exception {
            //启动Spring Boot项目的唯一入口
            SpringApplication.run(Application.class, args);
        }
}
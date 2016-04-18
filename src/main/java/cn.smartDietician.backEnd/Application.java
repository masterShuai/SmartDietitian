package cn.smartDietician.backEnd;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wangshuai on 2016/4/4.
 */

@EnableAutoConfiguration//启用自动配置
@Configuration//配置控制
@ComponentScan//组件扫描
@EnableCaching
@SpringBootApplication//与@Configuration @EnableAutoConfiguration @ComponentScan相同]

public class Application {
    public static void main(String[] args) throws Throwable {
        //启动Spring Boot项目的唯一入口
        SpringApplication.run(Application.class, args);
    }
}
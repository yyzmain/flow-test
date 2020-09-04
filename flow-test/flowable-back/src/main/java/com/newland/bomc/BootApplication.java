package com.newland.bomc;

import com.newland.bomc.config.AppDispatcherServletConfiguration;
import com.newland.bomc.config.ApplicationConfiguration;
import org.flowable.ui.modeler.conf.DatabaseConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@Import(value={
        // 引入修改的配置
        ApplicationConfiguration.class,
        AppDispatcherServletConfiguration.class,
        // 引入 DatabaseConfiguration 表更新转换
        DatabaseConfiguration.class
       })
// Eureka 客户端
@ComponentScan(basePackages = {"com.newland.bomc.*"})
//非数据层应用，使用该注释去掉默认的数据库配置
//@SpringBootApplication(exclude={RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, SecurityAutoConfiguration.class})
//数据层应用，框架已默认引入数据库层依赖，只要在配置文件配置数据连接即可
@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
//可以feign
@EnableFeignClients
//开启eureka
@EnableDiscoveryClient
//开启断路器
@EnableCircuitBreaker
public class BootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }


}

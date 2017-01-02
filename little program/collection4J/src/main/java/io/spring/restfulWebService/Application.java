package io.spring.restfulWebService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 注：get请求发送的url为：http://localhost:8080/greeting?name=yourName，如果不传name参数则默认World
 *
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}

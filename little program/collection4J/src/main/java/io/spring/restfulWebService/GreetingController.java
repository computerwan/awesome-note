package io.spring.restfulWebService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @RestController 本身使用了@ResponseBody和@Controller
 * @RequestBody 将HTTP请求正文转换为适合的HttpMessageConverter对象。
 * @ResponseBody 将内容或对象作为 HTTP 响应正文返回，并调用适合HttpMessageConverter的Adapter转换对象，写入输出流。
 * 加入jackson的jar包后，HttpMessageConverter将会初始化7个转换器，用于转换为json的是MappingJackson2HttpMessageConverter。
 */
@RestController
public class GreetingController {
    private static final String template = "Hello,%s";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }


}

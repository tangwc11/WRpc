package com.example;

import com.example.service.DrinkService;
import com.example.service.SayService;
import com.wentry.wrpc.exchange.Sync2AsyncHelper;
import com.wentry.wrpc.proxy.ProxyFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: tangwc
 */
@RestController
@SpringBootApplication(scanBasePackages = {"com.example.**", "com.wentry.wrpc"})
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class);
    }


    @RequestMapping("/testSay")
    public String say() {
        SayService sayService = ProxyFactory.getProxy(SayService.class);
        return sayService.sayHello();
    }

    @RequestMapping("/testDrink")
    public String drink(){
        return ProxyFactory.getProxy(DrinkService.class).drink();
    }

    @RequestMapping("/testRate")
    public String test(int times) {
        long start = System.nanoTime();
        for (int i = 0; i < times; i++) {
            ProxyFactory.getProxy(SayService.class).sayHello();
        }
        long total = System.nanoTime() - start;
        long average = total / times;
        return times + "次调用，total: " + total + " ns, average:" + average + " ns";
    }
}

package com;

import com.service.OrderService;

public class Test {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        OrderService service = (OrderService) context.getBean("service");
        service.query();
    }
}

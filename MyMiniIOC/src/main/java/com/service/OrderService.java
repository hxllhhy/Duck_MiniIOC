package com.service;

import com.dao.OrderDao;

public class OrderService {
    private OrderDao dao;

    public OrderService(OrderDao dao) {
        this.dao = dao;
    }

    public void query() {
        System.out.println("service query...");
        dao.query();
    }
}

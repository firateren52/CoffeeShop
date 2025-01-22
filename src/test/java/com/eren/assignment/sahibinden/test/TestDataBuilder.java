package com.eren.assignment.sahibinden.test;

import java.util.Date;

import com.eren.assignment.sahibinden.entity.Beverage;
import com.eren.assignment.sahibinden.entity.Condiment;
import com.eren.assignment.sahibinden.entity.OrderItem;
import com.eren.assignment.sahibinden.entity.OrderItemDetail;
import com.eren.assignment.sahibinden.entity.User;
import com.eren.assignment.sahibinden.service.model.EntityStats;

public class TestDataBuilder {
    
    public static Beverage createBeverage(Long id, String name, int cost) {
        Beverage beverage = new Beverage();
        beverage.setId(id);
        beverage.setName(name);
        beverage.setCost(cost);
        beverage.setCdate(new Date());
        return beverage;
    }
    
    public static Condiment createCondiment(Long id, String name, int cost) {
        Condiment condiment = new Condiment();
        condiment.setId(id);
        condiment.setName(name);
        condiment.setCost(cost);
        condiment.setCdate(new Date());
        return condiment;
    }
    
    public static OrderItem createOrderItem(Long id, Beverage beverage) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(id);
        orderItem.setBeverage(beverage);
        orderItem.setCdate(new Date());
        return orderItem;
    }
    
    public static OrderItemDetail createOrderItemDetail(Long id, OrderItem orderItem, Condiment condiment, int count) {
        OrderItemDetail detail = new OrderItemDetail();
        detail.setId(id);
        detail.setOrderItem(orderItem);
        detail.setCondiment(condiment);
        detail.setCount(count);
        return detail;
    }
    
    public static User createUser(Long id, String username, String name, String surname, String userType) {
        User user = new User();
        user.setId(id);
        user.setUserName(username);
        user.setName(name);
        user.setSurname(surname);
        user.setUserType(userType);
        return user;
    }
    
    public static EntityStats createEntityStats(String name, long totalCost, long reducedCost) {
        EntityStats stats = new EntityStats();
        stats.setName(name);
        stats.setTotalCost(totalCost);
        stats.setReducedCost(reducedCost);
        return stats;
    }
}

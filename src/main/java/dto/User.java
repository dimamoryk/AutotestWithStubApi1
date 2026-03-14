package dto;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String name;

    private List<Order> orders;

    public User(String name, List<Order> orders) {
        this.name = name;
        this.orders = new ArrayList<>(orders);
    }

    public String getName() {
        return name;
    }

    public List<Order> getOrders() {
        return orders;
    }
}

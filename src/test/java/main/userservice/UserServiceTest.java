package main.userservice;

import dto.Order;
import dto.User;
import org.junit.jupiter.api.Test;
import services.IUserRepository;
import services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Test
    public void testGetOrdersByUser() {

        IUserRepository mockUserRepository = mock(IUserRepository.class);

        List<Order> orders = new ArrayList<>();

        orders.add(new Order("data1"));

        orders.add(new Order("data2"));
        User user = new User("Vasya", orders);
        when(mockUserRepository.findByName("Vasya")).thenReturn(user);
        UserService userService = new UserService(mockUserRepository);


        List<String> ordersIds = userService.getUserOrderIds("Vasya");
        assertEquals(orders.stream().map(Order::getOrderId).collect(Collectors.toList()), ordersIds);
        verify(mockUserRepository).findByName("Vasya");

        ordersIds = userService.getUserOrderIds("Dima");
        assertTrue(ordersIds.isEmpty());

    }
}

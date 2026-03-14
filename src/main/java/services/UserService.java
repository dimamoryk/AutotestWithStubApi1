package services;

import dto.Order;
import dto.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    protected IUserRepository userRepository;


    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<String> getUserOrderIds(String userName) {
        User user = userRepository.findByName(userName);
        if (user == null || user.getOrders() == null) {
            return Collections.emptyList();
        }
        return user.getOrders().stream()
                .map(Order::getOrderId)
                .collect(Collectors.toList());
    }
}

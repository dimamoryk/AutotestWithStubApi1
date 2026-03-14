package services;

import dto.User;

public interface IUserRepository {

    User findByName(String userName);
}

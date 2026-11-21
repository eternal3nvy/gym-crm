package com.epam.gym.dao;

import com.epam.gym.model.User;

import java.util.Set;

public interface UserDao<T extends User> extends Dao<T> {

    Set<String> findUsernamesStartingWith(String prefix);
}

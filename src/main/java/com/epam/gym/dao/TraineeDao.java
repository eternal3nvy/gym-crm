package com.epam.gym.dao;

import com.epam.gym.model.Trainee;

public interface TraineeDao extends UserDao<Trainee> {
    boolean deleteById(Long id);
}

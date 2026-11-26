package com.epam.gym.dao.memory;

import com.epam.gym.dao.TrainingDao;
import com.epam.gym.model.Training;
import com.epam.gym.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDaoImpl extends AbstractInMemoryDao<Training> implements TrainingDao {

    public TrainingDaoImpl() {
        super("training", Training::getId, Training::setId);
    }

}

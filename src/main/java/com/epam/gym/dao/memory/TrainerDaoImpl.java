package com.epam.gym.dao.memory;

import com.epam.gym.dao.TrainerDao;
import com.epam.gym.model.Trainer;
import com.epam.gym.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class TrainerDaoImpl extends AbstractInMemoryDao<Trainer> implements TrainerDao {

    public TrainerDaoImpl() {
        super("trainer", Trainer::getId, Trainer::setId);
    }

    @Override
    public Set<String> findUsernamesStartingWith(final String prefix) {
        return findAll().stream()
                .map(Trainer::getUsername)
                .filter(username -> username != null && username.startsWith(prefix))
                .collect(Collectors.toSet());
    }
}

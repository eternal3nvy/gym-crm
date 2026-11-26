package com.epam.gym.dao.memory;

import com.epam.gym.dao.TraineeDao;
import com.epam.gym.model.Trainee;
import com.epam.gym.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class TraineeDaoImpl extends AbstractInMemoryDao<Trainee> implements TraineeDao {

    public TraineeDaoImpl() {
        super("trainee", Trainee::getId, Trainee::setId);
    }

    @Override
    public boolean deleteById(final Long id) {
        final var deleted = storage().remove(id);
        log.debug("Deleted trainee id {}: {}", id, deleted);
        return deleted;
    }

    @Override
    public Set<String> findUsernamesStartingWith(final String prefix) {
        return findAll().stream()
                .map(Trainee::getUsername)
                .filter(username -> username != null && username.startsWith(prefix))
                .collect(Collectors.toSet());
    }
}

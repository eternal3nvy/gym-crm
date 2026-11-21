package com.epam.gym.service;

import com.epam.gym.dao.TraineeDao;
import com.epam.gym.exception.EntityNotFoundException;
import com.epam.gym.model.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger log = LoggerFactory.getLogger(TraineeServiceImpl.class);

    private final TraineeDao traineeDao;
    private final ProfileCredentialGenerator credentialGenerator;

    public TraineeServiceImpl(final TraineeDao traineeDao, final ProfileCredentialGenerator credentialGenerator) {
        this.traineeDao = traineeDao;
        this.credentialGenerator = credentialGenerator;
    }

    @Override
    public Trainee create(final Trainee trainee) {
        validateName(trainee);
        trainee.setUsername(credentialGenerator.generateUsername(
                trainee.getFirstName(), trainee.getLastName()));
        trainee.setPassword(credentialGenerator.generatePassword());
        final var saved = traineeDao.save(trainee);
        log.info("Created trainee id={} username={}", saved.getId(), saved.getUsername());
        return saved;
    }

    @Override
    public Trainee update(final Trainee trainee) {
        validateName(trainee);
        if (trainee.getId() == null) {
            throw new IllegalArgumentException("Trainee id must not be null for update");
        }
        final var existing = traineeDao.findById(trainee.getId())
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found: " + trainee.getId()));
        trainee.setUsername(existing.getUsername());
        trainee.setPassword(existing.getPassword());
        final var saved = traineeDao.save(trainee);
        log.info("Updated trainee id={}", saved.getId());
        return saved;
    }

    @Override
    public boolean delete(final Long id) {
        final var deleted = traineeDao.deleteById(id);
        log.info("Deleted trainee id={} result={}", id, deleted);
        return deleted;
    }

    @Override
    public Optional<Trainee> findById(final Long id) {
        return traineeDao.findById(id);
    }

    @Override
    public List<Trainee> findAll() {
        return traineeDao.findAll();
    }

    private void validateName(final Trainee trainee) {
        if (trainee == null
                || trainee.getFirstName() == null || trainee.getFirstName().isBlank()
                || trainee.getLastName() == null || trainee.getLastName().isBlank()) {
            throw new IllegalArgumentException("Trainee first name and last name must not be blank");
        }
    }
}

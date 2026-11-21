package com.epam.gym.service;

import com.epam.gym.dao.TrainerDao;
import com.epam.gym.exception.EntityNotFoundException;
import com.epam.gym.model.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger log = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private final TrainerDao trainerDao;
    private final ProfileCredentialGenerator credentialGenerator;

    public TrainerServiceImpl(final TrainerDao trainerDao, final ProfileCredentialGenerator credentialGenerator) {
        this.trainerDao = trainerDao;
        this.credentialGenerator = credentialGenerator;
    }

    @Override
    public Trainer create(final Trainer trainer) {
        validateName(trainer);
        trainer.setUsername(credentialGenerator.generateUsername(
                trainer.getFirstName(), trainer.getLastName()));
        trainer.setPassword(credentialGenerator.generatePassword());
        final var saved = trainerDao.save(trainer);
        log.info("Created trainer id={} username={}", saved.getId(), saved.getUsername());
        return saved;
    }

    @Override
    public Trainer update(final Trainer trainer) {
        validateName(trainer);
        if (trainer.getId() == null) {
            throw new IllegalArgumentException("Trainer id must not be null for update");
        }
        final var existing = trainerDao.findById(trainer.getId())
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found: " + trainer.getId()));
        trainer.setUsername(existing.getUsername());
        trainer.setPassword(existing.getPassword());
        final var saved = trainerDao.save(trainer);
        log.info("Updated trainer id={}", saved.getId());
        return saved;
    }

    @Override
    public Optional<Trainer> findById(final Long id) {
        return trainerDao.findById(id);
    }

    @Override
    public List<Trainer> findAll() {
        return trainerDao.findAll();
    }

    private void validateName(final Trainer trainer) {
        if (trainer == null
                || trainer.getFirstName() == null || trainer.getFirstName().isBlank()
                || trainer.getLastName() == null || trainer.getLastName().isBlank()) {
            throw new IllegalArgumentException("Trainer first name and last name must not be blank");
        }
    }
}

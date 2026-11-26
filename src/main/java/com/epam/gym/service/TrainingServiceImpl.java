package com.epam.gym.service;

import com.epam.gym.dao.TrainingDao;
import com.epam.gym.model.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {
    private static final Logger log = LoggerFactory.getLogger(TrainingServiceImpl.class);
    private final TrainingDao trainingDao;

    public TrainingServiceImpl(final TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    @Override
    public Training create(final Training training) {
        if (training == null) {
            throw new IllegalArgumentException("Training must not be null");
        }
        final var saved = trainingDao.save(training);
        log.info("Created training id={}", saved.getId());
        return saved;
    }

    @Override
    public Optional<Training> findById(final Long id) {
        return trainingDao.findById(id);
    }

    @Override
    public List<Training> findAll() {
        return trainingDao.findAll();
    }
}

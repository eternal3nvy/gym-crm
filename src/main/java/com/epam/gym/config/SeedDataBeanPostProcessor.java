package com.epam.gym.config;

import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.storage.InMemoryStorage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SeedDataBeanPostProcessor implements BeanPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(SeedDataBeanPostProcessor.class);

    private final String seedFile;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    private JsonNode seedData;

    public SeedDataBeanPostProcessor(
            @Value("${storage.seed-file}") final String seedFile,
            final ResourceLoader resourceLoader) {
        this.seedFile = seedFile;
        this.resourceLoader = resourceLoader;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean,
                                                 final String beanName) throws BeansException {
        if (bean instanceof InMemoryStorage<?> storage) {
            switch (storage.getName()) {
                case "trainee" -> loadTrainees(storage);
                case "trainer" -> loadTrainers(storage);
                case "training" -> loadTrainings(storage);
                default -> throw new RuntimeException();
            }
        }
        return bean;
    }

    private synchronized JsonNode getSeedData() {
        if (seedData != null) {
            return seedData;
        }

        final var resource = resourceLoader.getResource(seedFile);
        if (!resource.exists()) {
            throw new IllegalStateException("Seed data file does not exist: " + seedFile);
        }

        try (final var input = resource.getInputStream()) {
            seedData = objectMapper.readTree(input);
            return seedData;
        } catch (final IOException exception) {
            throw new IllegalStateException("Could not read seed data file: " + seedFile, exception);
        }
    }

    private void loadTrainees(final InMemoryStorage<?> rawStorage) {
        final var records = getSeedData().path("trainees");
        final var storage = castStorage(rawStorage);
        if (!records.isArray()) {
            return;
        }
        for (final var record : records) {
            try {
                final var trainee = objectMapper.treeToValue(record, Trainee.class);
                final var id = trainee.getId() != null ? trainee.getId() : storage.nextId();
                trainee.setId(id);
                storage.put(id, trainee);
            } catch (final IOException exception) {
                throw new IllegalStateException("Could not deserialize trainee seed record", exception);
            }
        }
        log.info("Loaded {} trainee seed records", records.size());
    }

    private void loadTrainers(final InMemoryStorage<?> rawStorage) {
        final var records = getSeedData().path("trainers");
        final var storage = castStorage(rawStorage);
        if (!records.isArray()) {
            return;
        }
        for (final var record : records) {
            try {
                final var trainer = objectMapper.treeToValue(record, Trainer.class);
                final var id = trainer.getId() != null ? trainer.getId() : storage.nextId();
                trainer.setId(id);
                storage.put(id, trainer);
            } catch (final IOException exception) {
                throw new IllegalStateException("Could not deserialize trainer seed record", exception);
            }
        }
        log.info("Loaded {} trainer seed records", records.size());
    }

    private void loadTrainings(final InMemoryStorage<?> rawStorage) {
        final var records = getSeedData().path("trainings");
        final var storage = castStorage(rawStorage);
        if (!records.isArray()) {
            return;
        }
        for (final var record : records) {
            try {
                final var training = objectMapper.treeToValue(record, Training.class);
                final var id = training.getId() != null ? training.getId() : storage.nextId();
                training.setId(id);
                storage.put(id, training);
            } catch (final IOException exception) {
                throw new IllegalStateException("Could not deserialize training seed record", exception);
            }
        }
        log.info("Loaded {} training seed records", records.size());
    }

    @SuppressWarnings("unchecked")
    private <T> InMemoryStorage<T> castStorage(final InMemoryStorage<?> storage) {
        return (InMemoryStorage<T>) storage;
    }
}

package com.epam.gym.config;

import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.storage.InMemoryStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {
    @Bean
    public InMemoryStorage<Trainee> traineeStorage() {
        return new InMemoryStorage<>("trainee");
    }

    @Bean
    public InMemoryStorage<Trainer> trainerStorage() {
        return new InMemoryStorage<>("trainer");
    }

    @Bean
    public InMemoryStorage<Training> trainingStorage() {
        return new InMemoryStorage<>("training");
    }
}

package com.epam.gym.service;

import com.epam.gym.dao.TraineeDao;
import com.epam.gym.dao.TrainerDao;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Objects;

@Component
public class ProfileCredentialGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final SecureRandom random = new SecureRandom();
    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;

    public ProfileCredentialGenerator(final TraineeDao traineeDao,
                                      final TrainerDao trainerDao) {
        this.traineeDao = Objects.requireNonNull(traineeDao, "traineeDao must not be null");
        this.trainerDao = Objects.requireNonNull(trainerDao, "trainerDao must not be null");
    }

    public String generateUsername(final String firstName, final String lastName) {
        Objects.requireNonNull(firstName, "firstName must not be null");
        Objects.requireNonNull(lastName, "lastName must not be null");
        if (firstName.isBlank() || lastName.isBlank()) {
            throw new IllegalArgumentException("First name and last name must not be blank");
        }

        final var base = firstName.trim() + "." + lastName.trim();
        final var existingUsernames = new HashSet<>(
                traineeDao.findUsernamesStartingWith(base));
        existingUsernames.addAll(trainerDao.findUsernamesStartingWith(base));

        if (!existingUsernames.contains(base)) {
            return base;
        }

        int suffix = 1;
        while (existingUsernames.contains(base + suffix)) {
            suffix++;
        }
        return base + suffix;
    }

    public String generatePassword() {
        final var password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }
}

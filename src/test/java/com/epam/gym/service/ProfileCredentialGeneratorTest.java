package com.epam.gym.service;

import com.epam.gym.dao.TraineeDao;
import com.epam.gym.dao.TrainerDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileCredentialGeneratorTest {
    @Mock
    private TraineeDao traineeDao;
    @Mock
    private TrainerDao trainerDao;

    private ProfileCredentialGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new ProfileCredentialGenerator(traineeDao, trainerDao);
    }

    @Test
    void generatesBaseUsernameWhenBothDaosHaveNoMatches() {
        when(traineeDao.findUsernamesStartingWith("John.Smith")).thenReturn(Set.of());
        when(trainerDao.findUsernamesStartingWith("John.Smith")).thenReturn(Set.of());

        assertEquals("John.Smith", generator.generateUsername("John", "Smith"));
        verify(traineeDao).findUsernamesStartingWith("John.Smith");
        verify(trainerDao).findUsernamesStartingWith("John.Smith");
    }

    @Test
    void generatesFirstFreeSuffixAcrossBothDaos() {
        when(traineeDao.findUsernamesStartingWith("John.Smith"))
                .thenReturn(Set.of("John.Smith", "John.Smith1"));
        when(trainerDao.findUsernamesStartingWith("John.Smith"))
                .thenReturn(Set.of("John.Smith3"));

        assertEquals("John.Smith2", generator.generateUsername("John", "Smith"));
    }

    @Test
    void generatesTenCharacterPassword() {
        final var password = generator.generatePassword();

        assertEquals(10, password.length());
        assertTrue(password.matches("[A-Za-z0-9]{10}"));
    }

    @Test
    void rejectsBlankNamesWithoutQueryingDaos() {
        assertThrows(IllegalArgumentException.class,
                () -> generator.generateUsername(" ", "Smith"));
    }
}

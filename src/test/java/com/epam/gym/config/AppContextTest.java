package com.epam.gym.config;

import com.epam.gym.facade.GymFacade;
import com.epam.gym.storage.InMemoryStorage;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppContextTest {
    @Test
    void startsAndLoadsThreeStorageBeans() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {
            assertNotNull(context.getBean(GymFacade.class));
            final var storages =
                    context.getBeansOfType(InMemoryStorage.class);
            assertEquals(3, storages.size());
            assertEquals(2, context.getBean("traineeStorage", InMemoryStorage.class).size());
            assertEquals(2, context.getBean("trainerStorage", InMemoryStorage.class).size());
            assertEquals(2, context.getBean("trainingStorage", InMemoryStorage.class).size());
        }
    }
}

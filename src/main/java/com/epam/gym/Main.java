package com.epam.gym;

import com.epam.gym.config.AppConfig;
import com.epam.gym.facade.GymFacade;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(final String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {
            final var facade = context.getBean(GymFacade.class);
        }
    }
}

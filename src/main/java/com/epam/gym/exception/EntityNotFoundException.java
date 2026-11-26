package com.epam.gym.exception;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(final String m) {
        super(m);
    }
}

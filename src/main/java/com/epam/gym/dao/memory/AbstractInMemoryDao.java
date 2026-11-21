package com.epam.gym.dao.memory;

import com.epam.gym.dao.Dao;
import com.epam.gym.storage.InMemoryStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class AbstractInMemoryDao<T> implements Dao<T> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final String entityName;
    private final Function<T, Long> idGetter;
    private final BiConsumer<T, Long> idSetter;
    private InMemoryStorage<T> storage;

    protected AbstractInMemoryDao(final String entityName, final Function<T, Long> idGetter,
                                  final BiConsumer<T, Long> idSetter) {
        this.entityName = entityName;
        this.idGetter = idGetter;
        this.idSetter = idSetter;
    }

    @Autowired
    protected void setStorage(final InMemoryStorage<T> storage) {
        this.storage = Objects.requireNonNull(storage, "storage must not be null");
    }

    protected InMemoryStorage<T> storage() {
        return storage;
    }

    @Override
    public T save(final T entity) {
        Objects.requireNonNull(entity, entityName + " must not be null");

        if (idGetter.apply(entity) == null) {
            idSetter.accept(entity, storage.nextId());
        }
        final var id = idGetter.apply(entity);
        storage.put(id, entity);
        log.debug("Saved {} with id {}", entityName, id);
        return entity;
    }

    @Override
    public Optional<T> findById(final Long id) {
        return storage.get(id);
    }

    @Override
    public List<T> findAll() {
        return storage.values();
    }
}

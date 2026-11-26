package com.epam.gym.storage;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryStorage<T>{
    private static final Logger log = LoggerFactory.getLogger(InMemoryStorage.class);

    @Getter
    private final String name;
    private final Map<Long, T> data = new ConcurrentHashMap<>();
    private final AtomicLong lastId = new AtomicLong();

    public InMemoryStorage(final String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
    }

    public Long nextId() {
        final var id = lastId.incrementAndGet();
        log.debug("Storage '{}': generated id {}", name, id);
        return id;
    }

    public T put(final Long id, final T value) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(value, "value must not be null");

        lastId.accumulateAndGet(id, Math::max);
        data.put(id, value);
        log.debug("Storage '{}': stored entity with id {}", name, id);
        return value;
    }

    public Optional<T> get(final Long id) {
        Objects.requireNonNull(id, "id must not be null");

        final var result = Optional.ofNullable(data.get(id));
        log.debug("Storage '{}': lookup id {} -> {}", name, id, result.isPresent() ? "found" : "not found");
        return result;
    }

    public List<T> values() {
        final var result = List.copyOf(data.values());
        log.debug("Storage '{}': listed {} entities", name, result.size());
        return result;
    }

    public boolean remove(final Long id) {
        Objects.requireNonNull(id, "id must not be null");

        final var removed = data.remove(id) != null;
        log.debug("Storage '{}': remove id {} -> {}", name, id, removed ? "removed" : "nothing to remove");
        return removed;
    }

    public void clear() {
        data.clear();
        lastId.set(0);
        log.debug("Storage '{}': cleared", name);
    }

    public int size() {
        return data.size();
    }
}

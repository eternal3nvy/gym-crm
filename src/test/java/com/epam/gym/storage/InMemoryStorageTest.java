package com.epam.gym.storage;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryStorageTest {
    @Test
    void supportsIsolationLookupRemovalClearAndNextIds() {
        final var storage = new InMemoryStorage<>("trainee");

        assertEquals("trainee", storage.getName());
        assertEquals(1L, storage.nextId());
        assertEquals("Jane", storage.put(2L, "Jane"));
        assertEquals(Optional.of("Jane"), storage.get(2L));
        assertEquals(1, storage.size());
        assertTrue(storage.remove(2L));
        assertFalse(storage.remove(2L));

        storage.put(5L, "John");
        storage.clear();
        assertEquals(0, storage.size());
        assertEquals(1L, storage.nextId());
    }

    @Test
    void rejectsNullKeysAndValues() {
        final var storage = new InMemoryStorage<>("test");

        assertThrows(NullPointerException.class, () -> storage.put(null, "value"));
        assertThrows(NullPointerException.class, () -> storage.put(1L, null));
        assertThrows(NullPointerException.class, () -> storage.get(null));
        assertThrows(NullPointerException.class, () -> storage.remove(null));
    }

    @Test
    void rejectsBlankNamespace() {
        assertThrows(IllegalArgumentException.class, () -> new InMemoryStorage<>(" "));
    }
}

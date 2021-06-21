package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private final Map<Integer, User> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public User save(User user) {
        if (user.isNew()) {
            log.info("save new user");
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        log.info("save user with id = {}", user.getId());
        // handle case: update, but not present in storage
        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete user with id = {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User get(int id) {
        log.info("get user with id = {}", id);
        return repository.get(id);
    }

    @Override
    public User getByEmail(String email) {
        log.info("get user with email = {}", email);
        return repository.values().stream()
                .filter(user -> user.getEmail().compareToIgnoreCase(email) == 0)
                .findFirst().orElse(null);
    }

    @Override
    public List<User> getAll() {
        log.info("get all users");
        return repository.values().stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }
}

package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.util.Id;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public Object createUser(@RequestBody User user) {
        String email = user.getEmail();

        if (email == null || email.isBlank()) {
            throw new ConditionsNotMetException("Email должен быть указан");
        }

        if (!Pattern.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
                user.getEmail())
        ) {
            throw new ConditionsNotMetException("Email должен быть валидным");
        }

        for (User value : users.values()) {
            if (value.getEmail().equals(email)) {
                throw new DuplicatedDataException("Этот email уже используется");
            }
        }

        user.setId(Id.getNextId(users));
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);

        return user;
    }

    public User updateUser(@RequestBody User user) {
        Long id = user.getId();
        String email = user.getEmail();
        String username = user.getUsername();
        String password = user.getPassword();

        if (id == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        if (users.containsKey(id)) {
            User user1 = users.get(id);
            String email1 = user1.getEmail();

            if (!email1.equals(email)) {
                for (User value : users.values()) {
                    if (value.getEmail().equals(email)) {
                        throw new DuplicatedDataException("Этот email уже используется");
                    }
                }
            }

            if (email == null || username == null || password == null) {
                return user;
            }

            user1.setEmail(email);
            user1.setUsername(username);
            user1.setPassword(password);

            return user1;
        }

        return null;
    }

    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public User getUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new ConditionsNotMetException(String.format("Post с id=%s не найден", id));
        }

        return users.get(id);
    }
}

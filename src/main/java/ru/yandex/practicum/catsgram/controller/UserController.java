package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.util.Id;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
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

    @PutMapping
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
}

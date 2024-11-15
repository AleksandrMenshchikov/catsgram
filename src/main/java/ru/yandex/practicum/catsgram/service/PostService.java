package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ru.yandex.practicum.catsgram.util.Id.getNextId;

@Service
@RequiredArgsConstructor
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();
    private final UserService userService;

    public Collection<Post> findAll() {
        return posts.values();
    }

    public Post create(Post post) {
        Optional<User> userById = userService.findUserById(post.getAuthorId());

        if (userById.isEmpty()) {
            throw new ConditionsNotMetException(String.format("«Автор с id=%s не найден»", post.getAuthorId()));
        }

        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        post.setId(getNextId(posts));
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    public Post getPostById(Long id) {
        if (!posts.containsKey(id)) {
            throw new ConditionsNotMetException(String.format("Post с id=%s не найден", id));
        }

        return posts.get(id);
    }
}

package ru.yandex.practicum.catsgram.util;

import java.util.Map;

public class Id {
    public static long getNextId(Map<Long, ?> map) {
        long currentMaxId = map.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

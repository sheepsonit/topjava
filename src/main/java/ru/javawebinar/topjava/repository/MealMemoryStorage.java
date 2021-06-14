package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MealMemoryStorage implements MealStorage {
    private static final AtomicInteger counter = new AtomicInteger(0);

    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();

    public MealMemoryStorage() {

    }

    public void add(Meal meal) {
        mealsMap.put(counter.incrementAndGet(), meal);
    }

    public void delete(Integer id) {
        mealsMap.remove(id);
    }

    public void update(Meal meal) {
        mealsMap.replace(meal.getId(), meal);
    }

    public List<Meal> getAll() {
        return mealsMap.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime)).collect(Collectors.toList());
    }

    public Meal getById(Integer id) {
        return mealsMap.get(id);
    }
}

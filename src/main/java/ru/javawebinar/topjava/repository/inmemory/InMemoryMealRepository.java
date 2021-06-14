package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.getUserId() != authUserId()) return null;

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id) {
        Meal meal = repository.remove(id);
        return meal != null && meal.getUserId() == authUserId();
    }

    @Override
    public Meal get(int id) {
        Meal meal = repository.get(id);
        if (meal == null) return null;
        if (meal.getUserId() != authUserId()) return null;
        return meal;
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values().stream()
                .filter(meal -> meal.getUserId().equals(authUserId()))
                .sorted(Comparator.comparing(Meal::getDateTime))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}


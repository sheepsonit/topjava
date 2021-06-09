package ru.javawebinar.topjava.model;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MealMemoryStorage implements MealDao {
    private static final AtomicInteger counter = new AtomicInteger(0);

    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();

    public MealMemoryStorage(List<Meal> mealToList) {
        mealToList.forEach(this::addMeal);
    }

    public void addMeal(Meal meal) {
        mealsMap.put(counter.incrementAndGet(), meal);
    }

    public void deleteMeal(Integer id) {
        mealsMap.remove(id);
    }

    public void updateMeal(Meal meal) {
        mealsMap.replace(meal.getId(), meal);
    }

    public List<Meal> getAllMeals() {
        return mealsMap.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime)).collect(Collectors.toList());
    }

    public Meal getMealById(Integer id) {
        return mealsMap.getOrDefault(id, null);
    }
}

package ru.javawebinar.topjava.model;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MealListStorage implements MealDao {

    private final Map<Integer, MealTo> mealsMap = new ConcurrentHashMap<>();

    public MealListStorage(List<MealTo> mealToList) {
        mealToList.forEach(this::addMeal);
    }

    public void addMeal(MealTo mealTo) {
        mealsMap.put(mealTo.getMealId().get(), mealTo);
    }

    public void deleteMeal(Integer id) {
        mealsMap.remove(id);
    }

    public void updateMeal(MealTo mealTo) {
        mealsMap.putIfAbsent(mealTo.getMealId().get(), mealTo);
    }

    public Collection<MealTo> getAllMeals() {
        return mealsMap.values().stream()
                .sorted(Comparator.comparing(MealTo::getDateTime)).collect(Collectors.toList());
    }

    public MealTo getMealById(Integer id) {
        return mealsMap.getOrDefault(id, null);
    }
}

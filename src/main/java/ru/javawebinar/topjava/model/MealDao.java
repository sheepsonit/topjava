package ru.javawebinar.topjava.model;

import java.util.List;

public interface MealDao {
    void addMeal(Meal meal);

    void deleteMeal(Integer id);

    void updateMeal(Meal meal);

    List<Meal> getAllMeals();

    Meal getMealById(Integer id);
}

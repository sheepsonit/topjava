package ru.javawebinar.topjava.model;

import java.util.Collection;

public interface MealDao {
    void addMeal(MealTo mealTo);

    void deleteMeal(Integer id);

    void updateMeal(MealTo mealTo);

    Collection<MealTo> getAllMeals();

    MealTo getMealById(Integer id);
}

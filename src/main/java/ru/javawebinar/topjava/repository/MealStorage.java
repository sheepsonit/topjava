package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealStorage {
    void add(Meal meal);

    void delete(Integer id);

    void update(Meal meal);

    List<Meal> getAll();

    Meal getById(Integer id);
}

package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        if (meals.isEmpty())
            return userMealWithExcessList;

        Map<Integer, Integer> caloriesMap = getCaloriesMap(meals);

        meals.forEach(userMeal -> {
            LocalDateTime dateTime = userMeal.getDateTime();
            if (TimeUtil.isBetweenHalfOpen(dateTime.toLocalTime(), startTime, endTime)) {
                userMealWithExcessList.add(new UserMealWithExcess(dateTime,
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        caloriesMap.get(dateTime.getDayOfMonth()) <= caloriesPerDay));
            }
        });

        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<Integer, Integer> caloriesMap = getCaloriesMap(meals);

        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(),
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        caloriesMap.get(userMeal.getDateTime().getDayOfMonth()) <= caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static Map<Integer, Integer> getCaloriesMap(List<UserMeal> meals) {
        Map<Integer, Integer> caloriesMap = new HashMap<>();

        meals.forEach(userMeal -> {
            int day = userMeal.getDateTime().getDayOfMonth();
            if (caloriesMap.containsKey(day)) {
                caloriesMap.replace(day, caloriesMap.get(day) + userMeal.getCalories());
            } else {
                caloriesMap.put(day, userMeal.getCalories());
            }
        });
        return caloriesMap;
    }
}

package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealDao;
import ru.javawebinar.topjava.model.MealMemoryStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    private MealDao mealStorage;

    @Override
    public void init() throws ServletException {
        mealStorage = new MealMemoryStorage(MealsUtil.meals);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");

        String action = req.getParameter("action");
        if (action == null) {
            req.setAttribute("mealsTo", MealsUtil.filteredByStreams(mealStorage.getAllMeals(), LocalTime.MIN, LocalTime.MAX, 2000));
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
            return;
        }

        String id = req.getParameter("id");
        int mealId = -1;
        if (id != null && !id.isEmpty()) {
            mealId = Integer.parseInt(id);
        }

        Meal editMeal = null;
        switch (action) {
            case "delete":
                mealStorage.deleteMeal(mealId);
                resp.sendRedirect("meals");
                return;
            case "update":
                editMeal = mealStorage.getMealById(mealId);
                break;
            case "add":
                editMeal = new Meal();
                break;
        }
        req.setAttribute("editMeal", editMeal);
        req.getRequestDispatcher("/edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String strId = req.getParameter("id").trim();
        int id = Integer.parseInt(strId);
        if (id > -1) {
            if (mealStorage.getMealById(id) != null) {
                mealStorage.deleteMeal(id);
            }
        }

        String dateTime = req.getParameter("dateTime");
        int calories = Integer.parseInt(req.getParameter("calories"));
        String description = req.getParameter("description");
        mealStorage.addMeal(new Meal(id, LocalDateTime.parse(dateTime), description, calories));
        resp.sendRedirect("meals");
    }
}

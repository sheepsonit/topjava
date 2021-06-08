package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealListStorage;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    private MealListStorage mealListStorage;

    public MealServlet() {
        mealListStorage = new MealListStorage(MealsUtil.filteredByStreams(MealsUtil.meals, LocalTime.MIN, LocalTime.MAX, 2000));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");

        String action = req.getParameter("action");
        if (action == null) {
            req.setAttribute("mealsTo", mealListStorage.getAllMeals());
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
            return;
        }

        String id = req.getParameter("id").trim();
        int mealId = -1;
        if (!id.isEmpty()) {
            mealId = Integer.parseInt(id);
        }
        MealTo mealTo = null;
        switch (action) {
            case "delete":
                mealListStorage.deleteMeal(mealId);
                resp.sendRedirect("meals");
                return;
            case "update":
                mealTo = mealListStorage.getMealById(mealId);
                break;
            case "add":
                mealTo = new MealTo();
                break;
        }
        req.setAttribute("mealTo", mealTo);
        req.getRequestDispatcher("/edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String strId = req.getParameter("id").trim();
        String dateTime = req.getParameter("dateTime");
        int calories = Integer.parseInt(req.getParameter("calories"));
        String description = req.getParameter("description");

        int id = Integer.parseInt(strId);
        if (id > -1) {
            if (mealListStorage.getMealById(id) != null) {
                mealListStorage.deleteMeal(id);
            }
        }
        mealListStorage.addMeal(new MealTo(new AtomicInteger(id), LocalDateTime.parse(dateTime), description, calories, false));
        resp.sendRedirect("meals");
    }
}

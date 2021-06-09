package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);

    private MealDao mealStorage;

    @Override
    public void init() throws ServletException {
        mealStorage = new MealMemoryStorage(MealsUtil.meals);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String action = req.getParameter("action");
        if (action == null) {
            LOG.debug("redirect to meals");
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
                LOG.debug("delete meal");
                mealStorage.deleteMeal(mealId);
                resp.sendRedirect("meals");
                return;
            case "update":
                LOG.debug("update meal");
                editMeal = mealStorage.getMealById(mealId);
                break;
            case "add":
                LOG.debug("add new meal");
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
        String dateTime = req.getParameter("dateTime");
        int calories = Integer.parseInt(req.getParameter("calories"));
        String description = req.getParameter("description");
        Meal editMeal = new Meal(id, LocalDateTime.parse(dateTime), description, calories);
        if (id > -1) {
            mealStorage.updateMeal(editMeal);
        } else {
            mealStorage.addMeal(editMeal);
        }
        resp.sendRedirect("meals");
    }
}

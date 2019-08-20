package szklimek.diettracker.diet.meals;

import android.support.annotation.Nullable;

import java.util.ArrayList;

import szklimek.diettracker.BasePresenter;
import szklimek.diettracker.BaseView;
import szklimek.diettracker.data.model.BaseMeal;

/**
 * Contract between MealsFragment View and Presenter
 */

public interface MealsContract {

    interface View extends BaseView<Presenter> {

        void updateMealsView(ArrayList<BaseMeal> meals, int totalCalories, int caloriesLimit);

        void updateMealCalories(int position, int caloriesPercent, int mealCalories, int totalMealsCalories);

        void updateMealName(int position, int nameId, @Nullable String name);

        void addMealToList(int position);

        void removeMealFromList(int position);

        void showNextFragment();

        void showPreviousFragment();

    }

    interface Presenter extends BasePresenter {

        void onBackClicked();

        void onNextClicked();

        void onDestroy();

        void getMeals();

        void onMealCaloriesChanged(int position, int caloriesPercent);

        void onMealNameChanged(int position, int nameId);

        void onMealNameInputChanged(int position, String nameInput);
    }

    interface Model {

        void getMeals(GetMealsCallback callback);

        void updateMealCaloriesPercent(UpdateMealCaloriesCallback callback, int position, int caloriesPercent);

        void updateMealName(int position, int nameId);

        void updateMealInput(int position, String name);

        interface GetMealsCallback {

            void onMealsLoaded(ArrayList<BaseMeal> mealsList, int totalCalories, int caloriesLimit);

        }

        interface UpdateMealCaloriesCallback {

            void onMealCaloriesUpdated(int position, int caloriesPercent, int mealCalories, int totalMealCalories);

        }

    }
}



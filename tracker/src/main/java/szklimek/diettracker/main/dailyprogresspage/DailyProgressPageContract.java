package szklimek.diettracker.main.dailyprogresspage;

import szklimek.diettracker.BasePresenter;
import szklimek.diettracker.BaseView;
import szklimek.diettracker.data.model.DietProgress;
import szklimek.diettracker.data.model.Meal;

/**
 * Contract between DailyMeals Model, View and Presenter
 */

public interface DailyProgressPageContract {

    interface View extends BaseView<Presenter> {

        void showDailyDietProgress(DietProgress data);

        void showAddFood(int mealNumber);

        void showMealDetails();

    }

    interface Presenter extends BasePresenter {

        void getDietProgress(long date);

        void onDateLoaded(long date);

        void onAddFoodClicked(int mealNumber);

        void onDetailsClicked(Meal meal);

    }

    interface Model {

        void getDietProgress(GetDietProgressCallback callback, long date);

        interface GetDietProgressCallback{
            void onDietProgressLoaded(DietProgress dietProgress);
        }

        void setClickedMeal(Meal meal);

    }
}

package szklimek.diettracker.main.mealdetails;

import szklimek.diettracker.BasePresenter;
import szklimek.diettracker.BaseView;
import szklimek.diettracker.data.model.Meal;

/**
 * Contract between MealDetailsFragment Model, View and Presenter
 */

public interface MealDetailsContract {

    interface View extends BaseView<Presenter> {

        void showMealData(Meal meal);

    }

    interface Presenter extends BasePresenter {

        void getMeal();

    }

    interface Model {

        void getClickedMeal(GetClickedMealCallback callback);

        interface GetClickedMealCallback {

            void onMealLoaded(Meal meal);

        }

    }
}

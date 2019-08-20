package szklimek.diettracker.addfood;

import szklimek.diettracker.BaseNavigationActivityPresenter;
import szklimek.diettracker.BasePresenter;
import szklimek.diettracker.BaseView;
import szklimek.diettracker.data.model.Food;

/**
 * Contract between AddActivity View, Presenter and Model
 */

interface AddActivityContract {
    interface View extends BaseView<Presenter> {

        void showDishes();

        void showProducts();

        void showAddFoodDialog();

    }

    interface Presenter extends BasePresenter {

        void onIntentLoaded(int mealNumber);

    }

    interface Model {

        void setMealId(int mealId);

    }

}

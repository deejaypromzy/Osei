package szklimek.diettracker.main;

import szklimek.diettracker.BaseNavigationActivityPresenter;
import szklimek.diettracker.BaseNavigationActivityView;
import szklimek.diettracker.data.model.Diet;

/**
 * Contract between MainActivity View, Presenter and Model
 */

public interface MainContract {

    interface View extends BaseNavigationActivityView<Presenter> {

        void showNewDiet();

        void showAddFood(int mealNumber);

        void showDailyMeals(Diet diet);

        void showMealDetails();

    }

    interface Presenter extends BaseNavigationActivityPresenter {

        void getDiet();

    }

    interface Model {

        void checkDatabaseState();

        void getDiet(GetDietCallback callback);

        interface GetDietCallback {

            void onDietLoaded(Diet diet);

            void onDietNotSet();

        }

    }
}

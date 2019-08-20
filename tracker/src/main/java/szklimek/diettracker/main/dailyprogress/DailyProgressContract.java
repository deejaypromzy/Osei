package szklimek.diettracker.main.dailyprogress;

import java.util.Calendar;

import szklimek.diettracker.BasePresenter;
import szklimek.diettracker.BaseView;
import szklimek.diettracker.data.model.DietProgress;
import szklimek.diettracker.data.model.Meal;

/**
 * Contract between DailyMeals Model, View and Presenter
 */

public interface DailyProgressContract {

    interface View extends BaseView<Presenter> {

        void showDayOfWeek(int dayOfWeek);

    }

    interface Presenter extends BasePresenter {

        void getDate();

    }

    interface Model {

        void getDate(GetDateCallback callback);

        interface GetDateCallback {
            void onDateLoaded(Calendar calendar);
        }

    }
}

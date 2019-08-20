package szklimek.diettracker.diet.summary;

import java.util.ArrayList;

import szklimek.diettracker.BasePresenter;
import szklimek.diettracker.BaseView;
import szklimek.diettracker.data.model.BaseMeal;
import szklimek.diettracker.data.model.Diet;

/**
 * Contract class between SummaryFragment View and Presenter
 */

public interface SummaryContract {

    interface View extends BaseView<Presenter> {

        void updateView(Diet diet);

        void showPreviousFragment();

        void saveDiet();

    }

    interface Presenter extends BasePresenter {

        void getStartValues();

        void onBackClicked();

        void onSaveClicked();

    }

    interface Model {

        void getDietSummary(GetDietSummaryCallback callback);

        interface GetDietSummaryCallback {

            void onSummaryLoaded(Diet diet);

        }

    }

}

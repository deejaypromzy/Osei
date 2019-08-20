package szklimek.diettracker.diet.ratio;

import szklimek.diettracker.BasePresenter;
import szklimek.diettracker.BaseView;

/**
 * Contract class between RatioFragment View and Presenter
 */

public interface RatioContract {

    interface View extends BaseView<RatioContract.Presenter> {

        void updateView(int type, int caloriesLimit, int totalWeight, int percentSum,
                        int progress, int calories, int weight);

        void showPreviousFragment();

        void showNextFragment();

    }

    interface Presenter extends BasePresenter {

        void getStartValues();

        void onRatioChanged(int type, int progress);

        void onBackClicked();

        void onNextClicked();

        void onDestroy();

    }

    interface Model {

        void updateRatioValue(GetRatioValueCallback callback, int type, int progress);

        void getRatioValue(int type, GetRatioValueCallback callback);

        interface GetRatioValueCallback {
            void onRatioValueLoaded(int caloriesLimit, int totalWeight, int percentSum,
                                    int progress, int calories, int weight);
        }

    }
}

package szklimek.diettracker.main.dailyprogress;

import android.support.annotation.NonNull;

import szklimek.diettracker.data.model.DietProgress;
import szklimek.diettracker.data.model.Meal;
import szklimek.diettracker.main.MainModel;

/**
 * Implementation DailyProgressFragment presenter
 */

class DailyProgressPresenter implements DailyProgressContract.Presenter {

    @NonNull
    private MainModel mActivityModel;

    @NonNull
    private DailyProgressContract.View mDailyMealsView;

    DailyProgressPresenter(@NonNull MainModel model,
                           @NonNull DailyProgressContract.View view) {
        mDailyMealsView = view;
        mActivityModel = model;

        mDailyMealsView.setPresenter(this);
    }

    @Override
    public void start() {

    }


    @Override
    public void getDate() {

    }

}

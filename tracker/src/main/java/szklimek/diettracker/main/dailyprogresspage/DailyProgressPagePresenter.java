package szklimek.diettracker.main.dailyprogresspage;

import android.support.annotation.NonNull;

import szklimek.diettracker.data.model.DietProgress;
import szklimek.diettracker.data.model.Meal;
import szklimek.diettracker.main.MainModel;

/**
 * Implementation DailyProgressPageFragment presenter
 */

class DailyProgressPagePresenter implements DailyProgressPageContract.Presenter {

    @NonNull
    private MainModel mActivityModel;

    @NonNull
    private DailyProgressPageContract.View mDailyMealsView;

    DailyProgressPagePresenter(@NonNull MainModel model,
                               @NonNull DailyProgressPageContract.View view) {
        mDailyMealsView = view;
        mActivityModel = model;

        mDailyMealsView.setPresenter(this);
    }

    @Override
    public void start() {

    }


    @Override
    public void getDietProgress(long date) {
        DailyProgressPageContract.Model.GetDietProgressCallback callback =
                new DailyProgressPageContract.Model.GetDietProgressCallback() {
            @Override
            public void onDietProgressLoaded(DietProgress dietProgress) {
                mDailyMealsView.showDailyDietProgress(dietProgress);
            }
        };

        mActivityModel.getDietProgress(callback, date);
    }

    @Override
    public void onDateLoaded(long date) {
        getDietProgress(date);
    }

    @Override
    public void onAddFoodClicked(int mealNumber) {
        mDailyMealsView.showAddFood(mealNumber);
    }

    @Override
    public void onDetailsClicked(Meal meal) {
        mActivityModel.setClickedMeal(meal);
        mDailyMealsView.showMealDetails();
    }

}

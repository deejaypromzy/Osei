package szklimek.diettracker.diet.general;

import android.support.annotation.NonNull;

import szklimek.diettracker.diet.DietModel;

/**
 * Implementation of GeneralFragment presenter
 */

class GeneralPresenter implements GeneralContract.Presenter {

    @NonNull
    private DietModel mActivityModel;

    @NonNull
    private GeneralContract.View mGeneralView;

    GeneralPresenter(@NonNull DietModel model,
                     @NonNull GeneralContract.View view) {
        mGeneralView = view;
        mActivityModel = model;

        mGeneralView.setPresenter(this);
    }

    @Override
    public void start() {
        getStartValues();
    }

    @Override
    public void getStartValues() {
        mActivityModel.getGeneralValues(new GeneralContract.Model.GetGeneralValuesCallback() {
            @Override
            public void onGeneralValuesLoaded(int caloriesLimit, int mealsNumber) {
                mGeneralView.showStartValues(caloriesLimit, mealsNumber);
            }
        });
    }

    @Override
    public void onMealsNumberChanged(int mealsNumber) {
        mActivityModel.setMealsNumber(mealsNumber);
    }

    @Override
    public void onCaloriesLimitChanged(String caloriesLimit) {
        if (mActivityModel.isCaloriesLimitProper(caloriesLimit)) {
            mGeneralView.hideCaloriesError();
            mGeneralView.hideSoftInput();
            mActivityModel.setCaloriesLimit(Integer.parseInt(caloriesLimit));
        } else {
            mGeneralView.hideSoftInput();
            mGeneralView.showCaloriesError();
        }
    }

    @Override
    public void onNextClicked() {
        mGeneralView.showNextFragment();
    }
}

package szklimek.diettracker.diet;

import android.support.annotation.NonNull;

/**
 * Implementation of DietActivity presenter
 */

class DietPresenter implements DietContract.Presenter {

    @NonNull
    private DietModel mActivityModel;

    @NonNull
    private DietContract.View mActivityView;

    DietPresenter(@NonNull DietModel model,
                  @NonNull DietContract.View view) {
        mActivityView = view;
        mActivityModel = model;

        mActivityView.setPresenter(this);
    }

    @Override
    public void start() {
        getSummaryState();
    }

    @Override
    public void getSummaryState() {
        if(mActivityModel.isFragmentSummaryShown())mActivityView.hideViewPager();
        else mActivityView.showViewPager();
    }

    @Override
    public void onBackClicked() {
        if(mActivityModel.isFragmentSummaryShown()) {
            mActivityView.hideSummary();
            mActivityView.showViewPager();
            mActivityModel.setFragmentSummaryShown(false);
        }
        else mActivityView.showPreviousFragment();

    }

    @Override
    public void onNextClicked(int currentItem) {
        if(currentItem == DietActivity.FRAGMENT_TIME) {
            mActivityView.hideViewPager();
            mActivityView.showSummary();
            mActivityModel.setFragmentSummaryShown(true);
        }
        else mActivityView.showNextFragment();
    }

    @Override
    public void onSaveClicked() {
        mActivityModel.checkDietPlan(new DietContract.Model.CheckPlanCallback() {
            @Override
            public void onDietOk() {
                mActivityModel.saveDiet();
                mActivityModel.setFragmentSummaryShown(false);
                mActivityView.showSuccessAndFinish();
            }

            @Override
            public void onRatioNotFullError() {
                mActivityView.showRatioError();
                onBackClicked();
            }

            @Override
            public void onMealCaloriesZeroError() {
                mActivityView.showMealCaloriesError();
                onBackClicked();
            }

            @Override
            public void onMealCaloriesSumNotFullError() {
                mActivityView.showMealCaloriesSumError();
                onBackClicked();
            }

            @Override
            public void onMealNameEmptyError() {
                mActivityView.showMealNameError();
                onBackClicked();
            }
        });
    }
}

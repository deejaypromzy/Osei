package szklimek.diettracker.diet.ratio;

import android.support.annotation.NonNull;

import szklimek.diettracker.data.model.Diet;
import szklimek.diettracker.diet.DietContract;
import szklimek.diettracker.diet.DietModel;

/**
 * Implementation of RatioFragment presenter
 */

class RatioPresenter implements RatioContract.Presenter,
        DietContract.Model.OnCaloriesLimitChangeListener {

    @NonNull
    private DietModel mActivityModel;

    @NonNull
    private RatioContract.View mRatioView;

    RatioPresenter(@NonNull DietModel model,
                   @NonNull RatioContract.View view) {
        mRatioView = view;
        mActivityModel = model;

        mRatioView.setPresenter(this);
    }

    @Override
    public void start() {
        getStartValues();
        mActivityModel.addCaloriesChangeListener(this);
    }

    public void onDestroy() {
        mActivityModel.removeCaloriesChangeListener(this);
    }

    @Override
    public void getStartValues() {
        mActivityModel.getRatioValue(Diet.PROTEIN_PERCENT, new RatioContract.Model.GetRatioValueCallback() {
            @Override
            public void onRatioValueLoaded(int caloriesLimit, int totalWeight, int percentSum, int progress, int calories, int weight) {
                mRatioView.updateView(Diet.PROTEIN_PERCENT, caloriesLimit, totalWeight, percentSum, progress, calories, weight);
            }
        });

        mActivityModel.getRatioValue(Diet.CARBO_PERCENT, new RatioContract.Model.GetRatioValueCallback() {
            @Override
            public void onRatioValueLoaded(int caloriesLimit, int totalWeight, int percentSum, int progress, int calories, int weight) {
                mRatioView.updateView(Diet.CARBO_PERCENT, caloriesLimit, totalWeight, percentSum, progress, calories, weight);
            }
        });

        mActivityModel.getRatioValue(Diet.FAT_PERCENT, new RatioContract.Model.GetRatioValueCallback() {
            @Override
            public void onRatioValueLoaded(int caloriesLimit, int totalWeight, int percentSum, int progress, int calories, int weight) {
                mRatioView.updateView(Diet.FAT_PERCENT, caloriesLimit, totalWeight, percentSum, progress, calories, weight);
            }
        });


    }

    @Override
    public void onRatioChanged(final int type, int progress) {


        RatioContract.Model.GetRatioValueCallback callback = new RatioContract.Model.GetRatioValueCallback() {
            @Override
            public void onRatioValueLoaded(int caloriesLimit, int totalWeight, int percentSum,
                                           int progress, int calories, int weight) {
                mRatioView.updateView(type, caloriesLimit, totalWeight, percentSum, progress, calories, weight);
            }
        };
        mActivityModel.updateRatioValue(callback, type, progress);
    }

    @Override
    public void onBackClicked() {
        mRatioView.showPreviousFragment();
    }

    @Override
    public void onNextClicked() {
        mRatioView.showNextFragment();
    }

    @Override
    public void onCaloriesLimitChange(int caloriesLimit) {
        getStartValues();
    }
}

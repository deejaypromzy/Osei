package szklimek.diettracker.diet.summary;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import szklimek.diettracker.data.model.BaseMeal;
import szklimek.diettracker.data.model.Diet;
import szklimek.diettracker.diet.DietModel;

/**
 * Implementation of SummaryFragment presenter
 */

class SummaryPresenter implements SummaryContract.Presenter {

    @NonNull
    private DietModel mActivityModel;

    @NonNull
    private SummaryContract.View mSummaryView;

    SummaryPresenter(@NonNull DietModel model,
                     @NonNull SummaryContract.View view) {
        mSummaryView = view;
        mActivityModel = model;

        mSummaryView.setPresenter(this);
    }

    @Override
    public void start() {
        getStartValues();
    }

    @Override
    public void getStartValues() {
        mActivityModel.getDietSummary(new SummaryContract.Model.GetDietSummaryCallback() {
            @Override
            public void onSummaryLoaded(Diet diet) {
                mSummaryView.updateView(diet);
            }
        });
    }

    @Override
    public void onBackClicked() {
        mSummaryView.showPreviousFragment();
    }

    @Override
    public void onSaveClicked() {
        mSummaryView.saveDiet();
    }
}

package szklimek.diettracker.addfood;

import android.support.annotation.NonNull;

/**
 * Implementation of AddActivity presenter
 */

class AddActivityPresenter implements AddActivityContract.Presenter {

    private AddActivityContract.View mAddActivityView;

    private AddActivityModel mModel;

    AddActivityPresenter(@NonNull AddActivityContract.View addActivityView,
                         @NonNull AddActivityModel model) {
        mAddActivityView = addActivityView;
        mModel = model;

        mAddActivityView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void onIntentLoaded(int mealNumber) {
        mModel.setMealId(mealNumber);
    }

}

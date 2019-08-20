package szklimek.diettracker.browse;

import android.support.annotation.NonNull;

/**
 * Implementation of BrowseActivity presenter
 */

public class BrowsePresenter implements BrowseContract.Presenter {

    @NonNull
    private BrowseContract.Model mActivityModel;

    @NonNull
    private BrowseContract.View mActivityView;

    BrowsePresenter(@NonNull BrowseContract.Model model,
                    @NonNull BrowseContract.View view) {
        mActivityView = view;
        mActivityModel = model;

        mActivityView.setPresenter(this);
    }

    @Override
    public void start() {
        mActivityView.showMenu();
    }

    @Override
    public void onProductClicked() {
        mActivityView.showProductDetails();
    }

    @Override
    public void onDailyPlanMenuClicked() {
        mActivityView.showDailyPlan();
    }

    @Override
    public void onReportsMenuClicked() {
        mActivityView.showReports();
    }

    @Override
    public void onCounterMenuClicked() {
        mActivityView.showCounter();
    }

    @Override
    public void onFoodBrowseMenuClicked() {
        mActivityView.showFoodBrowse();
    }

    @Override
    public void onDietPlanMenuClicked() {
        mActivityView.showDietPlan();
    }

    @Override
    public void onSettingsMenuClicked() {
        mActivityView.showSettings();
    }

    @Override
    public void onHelpMenuClicked() {
        mActivityView.showHelp();
    }

    @Override
    public void onFeedbackMenuClicked() {
        mActivityView.showFeedback();
    }
}

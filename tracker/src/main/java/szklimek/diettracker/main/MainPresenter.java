package szklimek.diettracker.main;

import android.support.annotation.NonNull;

import szklimek.diettracker.data.model.Diet;

/**
 * Implementation of MainActivity presenter
 */

public class MainPresenter implements MainContract.Presenter {

    @NonNull
    private MainContract.Model mActivityModel;

    @NonNull
    private MainContract.View mActivityView;

    MainPresenter(@NonNull MainContract.Model model,
                  @NonNull MainContract.View view) {
        mActivityView = view;
        mActivityModel = model;
        mActivityView.setPresenter(this);
    }

    @Override
    public void start() {
        getDiet();
    }

    @Override
    public void getDiet() {
        mActivityModel.getDiet(new MainContract.Model.GetDietCallback() {
            @Override
            public void onDietLoaded(Diet diet) {
                mActivityView.showDailyMeals(diet);
            }

            @Override
            public void onDietNotSet() {
                mActivityView.showNewDiet();
            }
        });
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

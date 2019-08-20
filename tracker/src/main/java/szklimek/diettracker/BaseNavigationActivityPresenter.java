package szklimek.diettracker;

import szklimek.diettracker.data.model.Product;

/**
 * Base template for activities' with navigation presenters
 */

public interface BaseNavigationActivityPresenter extends BasePresenter {

    void onDailyPlanMenuClicked();

    void onReportsMenuClicked();

    void onCounterMenuClicked();

    void onFoodBrowseMenuClicked();

    void onDietPlanMenuClicked();

    void onSettingsMenuClicked();

    void onHelpMenuClicked();

    void onFeedbackMenuClicked();

}

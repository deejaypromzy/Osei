package szklimek.diettracker;

import android.content.Intent;

import szklimek.diettracker.browse.BrowseActivity;
import szklimek.diettracker.diet.DietActivity;
import szklimek.diettracker.main.MainActivity;

/**
 * Interface which all activities with navigation drawer should implement
 */

public interface BaseNavigationActivityView<T> extends BaseView<T> {

    void showDailyPlan();

    void showReports();

    void showCounter();

    void showFoodBrowse();

    void showDietPlan();

    void showSettings();

    void showHelp();

    void showFeedback();

}

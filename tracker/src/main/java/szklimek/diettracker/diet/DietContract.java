package szklimek.diettracker.diet;

import szklimek.diettracker.BasePresenter;
import szklimek.diettracker.BaseView;
import szklimek.diettracker.data.model.BaseMeal;

/**
 * Contract between DietActivity View, Presenter and Model
 */

public interface DietContract {

    interface View extends BaseView<Presenter> {

        void showPreviousFragment();

        void showNextFragment();

        void showSummary();

        void hideSummary();

        void showRatioError();

        void showMealCaloriesSumError();

        void showMealCaloriesError();

        void showMealNameError();

        void hideViewPager();

        void showViewPager();

        void showSuccessAndFinish();

        void onNavButtonClicked(int buttonType);

        void showTimePicker(int position,
                            int currentHour, int currentMinutes,
                            int afterHour, int afterMinutes,
                            int beforeHour, int beforeMinutes);

    }

    interface Presenter extends BasePresenter {

        void getSummaryState();

        void onBackClicked();

        void onNextClicked(int currentItem);

        void onSaveClicked();

    }

    interface Model {

        void saveDiet();

        void checkDietPlan(CheckPlanCallback callback);

        interface CheckPlanCallback {
            void onDietOk();

            void onRatioNotFullError();

            void onMealCaloriesZeroError();

            void onMealCaloriesSumNotFullError();

            void onMealNameEmptyError();
        }

        void addMealToList(int currentSize, int newSize);

        void removeMealFromList(int currentSize, int newSize);

        void loadDietFromPreferences();

        void loadDefaultDiet();

        void setFragmentSummaryShown(boolean isDisplayed);

        boolean isFragmentSummaryShown();

        void addCaloriesChangeListener(OnCaloriesLimitChangeListener listener);

        void removeCaloriesChangeListener(OnCaloriesLimitChangeListener listener);

        void addMealsNumberChangeListener(OnMealsNumberChangeListener listener);

        void removeMealsNumberChangeListener(OnMealsNumberChangeListener listener);

        void addMealValueChangeListener(OnMealValuesChangeListener listener);

        void removeMealValueChangeListener(OnMealValuesChangeListener listener);

        void notifyCaloriesLimitChange(int caloriesLimit);

        void notifyMealsNumberIncreased(int addedMealPosition);

        void notifyMealsNumberDecreased(int removedMealPosition);

        void notifyMealValuesChange(int mealPosition);

        interface OnCaloriesLimitChangeListener {

            void onCaloriesLimitChange(int caloriesLimit);

        }

        interface OnMealsNumberChangeListener {

            void onMealsNumberIncrease(int addedMealPosition);

            void onMealsNumberDecrease(int removedMealPosition);

        }

        interface OnMealValuesChangeListener {

            void onMealValuesChange(int position);

        }

        void updateMealTime(int position, int hour, int minutes);

    }
}

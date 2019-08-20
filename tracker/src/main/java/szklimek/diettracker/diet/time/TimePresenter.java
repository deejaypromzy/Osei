package szklimek.diettracker.diet.time;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import szklimek.diettracker.data.model.BaseMeal;
import szklimek.diettracker.diet.DietContract;
import szklimek.diettracker.diet.DietModel;
import szklimek.diettracker.diet.meals.MealsContract;

/**
 * Implementation of TimeFragment presenter
 */

class TimePresenter implements TimeContract.Presenter,
        DietContract.Model.OnMealsNumberChangeListener,
        DietContract.Model.OnMealValuesChangeListener {

    @NonNull
    private DietModel mActivityModel;

    @NonNull
    private TimeContract.View mTimeView;

    TimePresenter(@NonNull DietModel model,
                  @NonNull TimeContract.View view) {
        mTimeView = view;
        mActivityModel = model;

        mTimeView.setPresenter(this);
    }
    @Override
    public void start() {
        getMeals();
        mActivityModel.addMealsNumberChangeListener(this);
        mActivityModel.addMealValueChangeListener(this);
    }

    @Override
    public void onDestroy() {
        mActivityModel.removeMealsNumberChangeListener(this);
        mActivityModel.removeMealValueChangeListener(this);
    }

    @Override
    public void onBackClicked() {
        mTimeView.showPreviousFragment();
    }

    @Override
    public void onNextClicked() {
        mTimeView.showNextFragment();
    }

    @Override
    public void getMeals() {
        mActivityModel.getMeals(new MealsContract.Model.GetMealsCallback() {
            @Override
            public void onMealsLoaded(ArrayList<BaseMeal> mealsList, int totalCalories, int caloriesLimit) {
                mTimeView.updateMealsView(mealsList);
            }
        });
    }

    @Override
    public void onMealTimeClicked(final int position) {
        TimeContract.Model.GetMealsTimeCallback callback = new TimeContract.Model.GetMealsTimeCallback() {
            @Override
            public void onMealsTimeLoaded(int currentHour, int currentMinutes,
                                          int afterHour, int afterMinutes,
                                          int beforeHour, int beforeMinutes) {
                mTimeView.showTimePicker(position, currentHour, currentMinutes,
                        afterHour, afterMinutes, beforeHour, beforeMinutes);
            }
        };
        mActivityModel.getNeighbouringMealsTime(callback, position);
    }

    @Override
    public void onMealValuesChange(int position) {
        TimeContract.Model.GetMealCallback callback = new TimeContract.Model.GetMealCallback() {
            @Override
            public void onMealLoaded(int position, BaseMeal meal) {
                mTimeView.updateMeal(position, meal);
            }
        };
        mActivityModel.getMeal(callback, position);
    }

    @Override
    public void onMealsNumberIncrease(int addedMealPosition) {
        mTimeView.addMealToList(addedMealPosition);
    }

    @Override
    public void onMealsNumberDecrease(int removedMealPosition) {
        mTimeView.removeMealFromList(removedMealPosition);
    }
}

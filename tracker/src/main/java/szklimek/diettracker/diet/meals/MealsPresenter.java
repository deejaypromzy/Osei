package szklimek.diettracker.diet.meals;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import szklimek.diettracker.data.model.BaseMeal;
import szklimek.diettracker.diet.DietContract;
import szklimek.diettracker.diet.DietModel;

/**
 * Implementation of MealsFragment presenter
 */

class MealsPresenter implements MealsContract.Presenter,
        DietContract.Model.OnMealsNumberChangeListener,
        DietContract.Model.OnCaloriesLimitChangeListener {

    @NonNull
    private DietModel mActivityModel;

    @NonNull
    private MealsContract.View mMealsView;

    MealsPresenter(@NonNull DietModel model,
                   @NonNull MealsContract.View view) {
        mMealsView = view;
        mActivityModel = model;

        mMealsView.setPresenter(this);
    }

    @Override
    public void start() {
        getMeals();
        mActivityModel.addMealsNumberChangeListener(this);
        mActivityModel.addCaloriesChangeListener(this);
    }

    @Override
    public void onDestroy() {
        mActivityModel.removeMealsNumberChangeListener(this);
        mActivityModel.removeCaloriesChangeListener(this);
    }

    @Override
    public void onBackClicked() {
        mMealsView.showPreviousFragment();
    }

    @Override
    public void onNextClicked() {
        mMealsView.showNextFragment();
    }

    @Override
    public void getMeals() {
        mActivityModel.getMeals(new MealsContract.Model.GetMealsCallback() {
            @Override
            public void onMealsLoaded(ArrayList<BaseMeal> mealsList, int totalCalories, int caloriesLimit) {
                mMealsView.updateMealsView(mealsList, totalCalories, caloriesLimit);
            }
        });
    }

    @Override
    public void onMealCaloriesChanged(int position, int caloriesPercent) {
        MealsContract.Model.UpdateMealCaloriesCallback callback =
                new MealsContract.Model.UpdateMealCaloriesCallback() {
                    @Override
                    public void onMealCaloriesUpdated(int position, int caloriesPercent, int mealCalories, int totalCalories) {
                        mMealsView.updateMealCalories(position, caloriesPercent, mealCalories, totalCalories);
                    }
                };
        mActivityModel.updateMealCaloriesPercent(callback, position, caloriesPercent);
    }

    @Override
    public void onMealNameChanged(int position, int nameId) {
        mActivityModel.updateMealName(position, nameId);
        mMealsView.updateMealName(position, nameId, null);
    }

    @Override
    public void onMealNameInputChanged(int position, String nameInput) {
        mActivityModel.updateMealInput(position, nameInput);
    }

    @Override
    public void onCaloriesLimitChange(int caloriesLimit) {
        getMeals();
    }

    @Override
    public void onMealsNumberIncrease(int addedMealPosition) {
        mMealsView.addMealToList(addedMealPosition);
    }

    @Override
    public void onMealsNumberDecrease(int removedMealPosition) {
        mMealsView.removeMealFromList(removedMealPosition);
    }
}

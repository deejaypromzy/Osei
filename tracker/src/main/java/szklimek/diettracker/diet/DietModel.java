package szklimek.diettracker.diet;

import android.support.annotation.NonNull;


import java.util.ArrayList;

import szklimek.diettracker.data.model.BaseMeal;
import szklimek.diettracker.data.model.Diet;
import szklimek.diettracker.data.local.SharedPreferencesSource;
import szklimek.diettracker.diet.general.GeneralContract;
import szklimek.diettracker.diet.meals.MealsContract;
import szklimek.diettracker.diet.ratio.RatioContract;
import szklimek.diettracker.diet.summary.SummaryContract;
import szklimek.diettracker.diet.time.TimeContract;

import static szklimek.diettracker.reusablefragments.TimePickerFragment.NOT_SET;

/**
 * Model class which controls data flow in DietActivity and store cached data
 */

public class DietModel implements
        DietContract.Model,
        GeneralContract.Model,
        RatioContract.Model,
        MealsContract.Model,
        TimeContract.Model,
        SummaryContract.Model {

    private SharedPreferencesSource mPreferencesManager;

    private static DietModel INSTANCE = null;

    private static final int DEFAULT_CALORIES = 2000;
    private static final int DEFAULT_PROTEIN = 20;
    private static final int DEFAULT_CARBO = 55;
    private static final int DEFAULT_FAT = 25;
    private static final int MINIMAL_CALORIES = 500;

    private Diet mDiet;

    private boolean isSummaryDisplayed;

    private ArrayList<OnCaloriesLimitChangeListener> mCaloriesChangeListeners = new ArrayList<>();

    private ArrayList<OnMealsNumberChangeListener> mMealsNumberChangeListeners = new ArrayList<>();

    private ArrayList<OnMealValuesChangeListener> mMealValueChangeListeners = new ArrayList<>();

    /**
     * Singleton pattern
     *
     * @param sharedPreferencesManager - source of meal data stored in SharedPreferences
     */
    private DietModel(@NonNull SharedPreferencesSource sharedPreferencesManager) {
        mPreferencesManager = sharedPreferencesManager;
        loadDietFromPreferences();
    }

    static DietModel getInstance(@NonNull SharedPreferencesSource sharedPreferencesManager) {
        if (INSTANCE == null) {
            INSTANCE = new DietModel(sharedPreferencesManager);
        }
        return INSTANCE;
    }


    /**
     * Add Meal with default parameters to meals list
     *
     * @param currentSize - current meals number
     * @param newSize - new meals number
     */
    @Override
    public void addMealToList(int currentSize, int newSize) {
        ArrayList<BaseMeal> mMeals = mDiet.getMeals();

        while (newSize > currentSize) {
            int nameIdToSet = mMeals.get(currentSize - 1).getNameId() + 1;
            int hourToSet = mMeals.get(currentSize - 1).getTimeHour() + 2;
            if (hourToSet > 24) hourToSet = 24;
            if (nameIdToSet > 8) nameIdToSet = 8;
            mMeals.add(new BaseMeal(
                    currentSize + 1, // meal number
                    nameIdToSet, // id of name one further (ex. breakfast -> nd breakfast)
                    hourToSet, // 2 hours later than previous meal
                    mMeals.get(currentSize - 1).getTimeMinutes(), //
                    0 // default calories percent
            ));
            currentSize++;
        }
    }

    /**
     * Remove last Meal, when meals number is being reduced
     *
     * @param currentSize - current meals number
     * @param newSize - new meals number
     */
    @Override
    public void removeMealFromList(int currentSize, int newSize) {
        while (currentSize > newSize) {
            mDiet.getMeals().remove(currentSize - 1);
            currentSize--;
        }
    }

    /**
     * Saves Diet in SharedPreferences file
     */
    @Override
    public void saveDiet() {

        mPreferencesManager.saveDiet(mDiet);

    }

    /**
     * Check if diet plan is correctly filled
     */
    @Override
    public void checkDietPlan(CheckPlanCallback callback) {

        // Check if sum of all nutrients are equal to 100
        if (mDiet.getRatioSum() != 100) {
            callback.onRatioNotFullError();
        }

        // Check if all daily calories are used in filled in meals
        // and if there is a meal with 0 calories
        if (mDiet.isMealCaloriesPercentNull()) {
            callback.onMealCaloriesZeroError();
            return;
        }

        if (mDiet.getMealsCaloriesPercentSum() > 100) {
            callback.onMealCaloriesSumNotFullError();
            return;
        }

        // Check if user gave name to meal with nameId = Type.OTHER
        if (mDiet.isMealNameNotSet()) {
            callback.onMealNameEmptyError();
            return;
        }

        callback.onDietOk();
    }

    /**
     * Loads diet from SharedPreferences file and converts to local data
     */
    @Override
    public void loadDietFromPreferences() {
        mPreferencesManager.getDiet(new SharedPreferencesSource.GetDietCallback() {
            @Override
            public void onDietLoaded(Diet diet) {
                mDiet = diet;
            }

            @Override
            public void onDietNotSet() {
                loadDefaultDiet();
            }
        });
    }

    /**
     * Load default diet plan
     */
    @Override
    public void loadDefaultDiet() {

        ArrayList<BaseMeal> mMeals = new ArrayList<>();

        mMeals.add(new BaseMeal(1, BaseMeal.TYPE_BREAKFAST, 8, 0, 30)); // 8:00 breakfast 30% calories
        mMeals.add(new BaseMeal(2, BaseMeal.TYPE_LUNCH, 13, 0, 30)); // 13:00 lunch 30% calories
        mMeals.add(new BaseMeal(3, BaseMeal.TYPE_DINNER, 18, 0, 40)); // 18:00 dinner 40% calories

        mDiet = new Diet(mMeals, DEFAULT_CALORIES, DEFAULT_PROTEIN, DEFAULT_CARBO, DEFAULT_FAT);
        mDiet.updateMealsCaloriesLimit();

    }

    @Override
    public void setFragmentSummaryShown(boolean isDisplayed) {
        isSummaryDisplayed = isDisplayed;
    }

    @Override
    public boolean isFragmentSummaryShown() {
        return isSummaryDisplayed;
    }

    @Override
    public void addCaloriesChangeListener(OnCaloriesLimitChangeListener listener) {
        mCaloriesChangeListeners.add(listener);
    }

    @Override
    public void removeCaloriesChangeListener(OnCaloriesLimitChangeListener listener) {
        mCaloriesChangeListeners.remove(listener);
    }

    @Override
    public void addMealsNumberChangeListener(OnMealsNumberChangeListener listener) {
        mMealsNumberChangeListeners.add(listener);
    }

    @Override
    public void removeMealsNumberChangeListener(OnMealsNumberChangeListener listener) {
        mMealsNumberChangeListeners.remove(listener);
    }

    @Override
    public void addMealValueChangeListener(OnMealValuesChangeListener listener) {
        mMealValueChangeListeners.add(listener);
    }

    @Override
    public void removeMealValueChangeListener(OnMealValuesChangeListener listener) {
        mMealValueChangeListeners.remove(listener);
    }

    @Override
    public void notifyCaloriesLimitChange(int caloriesLimit) {
        for (OnCaloriesLimitChangeListener listener : mCaloriesChangeListeners) {
            listener.onCaloriesLimitChange(caloriesLimit);
        }
    }

    @Override
    public void notifyMealsNumberIncreased(int addedMealPosition) {
        for (OnMealsNumberChangeListener listener : mMealsNumberChangeListeners) {
            listener.onMealsNumberIncrease(addedMealPosition);
        }
    }

    @Override
    public void notifyMealsNumberDecreased(int removedMealPosition) {
        for (OnMealsNumberChangeListener listener : mMealsNumberChangeListeners) {
            listener.onMealsNumberDecrease(removedMealPosition);
        }
    }

    @Override
    public void notifyMealValuesChange(int mealPosition) {
        for (OnMealValuesChangeListener listener : mMealValueChangeListeners) {
            listener.onMealValuesChange(mealPosition);
        }
    }

    // General methods

    /**
     * Checking method for calories limit input
     *
     * @param caloriesLimit - input
     * @return - true if calories are over MINIMAL_CALORIES and not empty
     */
    @Override
    public boolean isCaloriesLimitProper(String caloriesLimit) {
        if (caloriesLimit.isEmpty() || Integer.valueOf(caloriesLimit) < MINIMAL_CALORIES)
            return false;
        return true;
    }

    @Override
    public void setCaloriesLimit(int caloriesLimit) {
        mDiet.setDailyCaloriesLimit(caloriesLimit);
        notifyCaloriesLimitChange(caloriesLimit);
    }

    @Override
    public void setMealsNumber(int newSize) {

        int currentSize = mDiet.getMeals().size();
        if (newSize < currentSize) {
            // Increasing meals number
            removeMealFromList(currentSize, newSize);
            notifyMealsNumberDecreased(newSize);

        } else if (newSize > currentSize) {

            // Decreasing meals number
            addMealToList(currentSize, newSize);
            notifyMealsNumberIncreased(newSize);

        }
    }

    /**
     * Retrieve data needed in general fragment
     *
     * @param callback - fired after data is returned
     */
    @Override
    public void getGeneralValues(GetGeneralValuesCallback callback) {

        callback.onGeneralValuesLoaded(mDiet.getDailyCaloriesLimit(), mDiet.getMeals().size());

    }

    // Nutrients ratio methods
    @Override
    public void getRatioValue(@Diet.NutrientRatioType int type, GetRatioValueCallback callback) {

        switch (type) {
            case Diet.PROTEIN_PERCENT:
                callback.onRatioValueLoaded(
                        mDiet.getDailyCaloriesLimit(), // calories limit
                        mDiet.getTotalNutrientWeight(), // total weight of nutrients
                        mDiet.getRatioSum(), // percent sum of all nutrients
                        mDiet.getProteinRatio(),  // protein ratio
                        mDiet.getProteinCalories(), // total calories from protein
                        mDiet.getProteinWeight()); // weight of protein in g
                break;

            case Diet.CARBO_PERCENT:
                callback.onRatioValueLoaded(
                        mDiet.getDailyCaloriesLimit(), // calories limit
                        mDiet.getTotalNutrientWeight(), // total weight of nutrients
                        mDiet.getRatioSum(), // percent sum of all nutrients
                        mDiet.getCarboRatio(),  //  carbohydrates ratio
                        mDiet.getCarboCalories(), // total calories from carbohydrates
                        mDiet.getCarboWeight()); // weight of carbohydrates in g
                break;

            case Diet.FAT_PERCENT:
                callback.onRatioValueLoaded(
                        mDiet.getDailyCaloriesLimit(), // calories limit
                        mDiet.getTotalNutrientWeight(), // total weight of nutrients
                        mDiet.getRatioSum(), // percent sum of all nutrients
                        mDiet.getFatRatio(),  // fat ratio
                        mDiet.getFatCalories(), // total calories from fat
                        mDiet.getFatWeight()); // weight of fat in g
                break;
        }
    }

    @Override
    public void updateRatioValue(GetRatioValueCallback callback,
                                 @Diet.NutrientRatioType int type, int progress) {

        setRatioValue(type, progress);
        getRatioValue(type, callback);

    }

    private void setRatioValue(@Diet.NutrientRatioType int type, int ratioProgress) {

        switch (type) {
            case Diet.PROTEIN_PERCENT:
                mDiet.setProteinRatio(ratioProgress);
                break;

            case Diet.CARBO_PERCENT:
                mDiet.setCarboRatio(ratioProgress);
                break;

            case Diet.FAT_PERCENT:
                mDiet.setFatRatio(ratioProgress);
                break;
        }

        // If overall percent sum is over 100 percent - set max possible value
        if (mDiet.getRatioSum() > 100) {
            int percentOverLimit = mDiet.getRatioSum() - 100;
            setRatioValue(type, ratioProgress - percentOverLimit);
        }

    }


    // Meals methods
    @Override
    public void getMeals(GetMealsCallback callback) {

        callback.onMealsLoaded(
                mDiet.getMeals(),
                mDiet.getTotalCaloriesInMeals(),
                mDiet.getDailyCaloriesLimit());

    }

    @Override
    public void updateMealName(int position, int nameId) {

        mDiet.getMeal(position).setNameId(nameId);
        notifyMealValuesChange(position);

    }

    @Override
    public void updateMealInput(int position, String name) {

        mDiet.getMeal(position).setName(name);
        notifyMealValuesChange(position);

    }

    @Override
    public void updateMealCaloriesPercent(UpdateMealCaloriesCallback callback, int position, int newPercent) {

        BaseMeal meal = mDiet.getMeal(position);

        meal.setPercentDailyLimit(newPercent);
        if (mDiet.getMealsCaloriesPercentSum() > 100) {
            int percentOverLimit = mDiet.getMealsCaloriesPercentSum() - 100;
            meal.setPercentDailyLimit(newPercent - percentOverLimit);
        }

        meal.setCaloriesLimit(mDiet.getDailyCaloriesLimit());
        callback.onMealCaloriesUpdated(
                position, // meal position
                meal.getPercentDailyLimit(), // new daily calories limit percent value
                meal.getMealCaloriesLimit(mDiet.getDailyCaloriesLimit()), // number of calories in meal
                mDiet.getMealsCaloriesSum()); // total number of calories of all meals


    }

    // Meals time methods
    @Override
    public void getMeal(GetMealCallback callback, int position) {

        if (mDiet.getMeal(position) != null) {
            callback.onMealLoaded(position, mDiet.getMeal(position));
        }
    }

    @Override
    public void getNeighbouringMealsTime(GetMealsTimeCallback callback, int position) {

        ArrayList<BaseMeal> mMeals = mDiet.getMeals();
        int currentHour = mMeals.get(position).getTimeHour();
        int currentMinutes = mMeals.get(position).getTimeMinutes();
        int afterHour = NOT_SET;
        int afterMinutes = NOT_SET;
        int beforeHour = NOT_SET;
        int beforeMinutes = NOT_SET;

        // If it is not first meal unable user to set meal time earlier than previous meal
        if (position > 0) {
            afterHour = mMeals.get(position - 1).getTimeHour();
            afterMinutes = mMeals.get(position - 1).getTimeMinutes();
        }

        // If it is not the last meal unable user to set meal time later than the next meal
        if (position + 1 < mMeals.size()) {
            beforeHour = mMeals.get(position + 1).getTimeHour();
            beforeMinutes = mMeals.get(position + 1).getTimeMinutes();
        }

        callback.onMealsTimeLoaded(
                currentHour, currentMinutes, // meal time
                afterHour, afterMinutes, // previous meal time
                beforeHour, beforeMinutes // next meal time
        );
    }

    @Override
    public void updateMealTime(int position, int hour, int minutes) {

        mDiet.getMeal(position).setTimeHour(hour);
        mDiet.getMeal(position).setTimeMinutes(minutes);
        notifyMealValuesChange(position);

    }

    // Diet summary methods
    @Override
    public void getDietSummary(GetDietSummaryCallback callback) {

        callback.onSummaryLoaded(mDiet);

    }

}


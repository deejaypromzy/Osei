package szklimek.diettracker.data.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import szklimek.diettracker.Utilities;

/**
 * Class which represents user's diet objective.
 */

public class Diet {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PROTEIN_PERCENT, CARBO_PERCENT, FAT_PERCENT})
    public @interface NutrientRatioType {
    }

    public static final int PROTEIN_PERCENT = Nutrient.PROTEIN;
    public static final int CARBO_PERCENT = Nutrient.CARBOHYDRATE;
    public static final int FAT_PERCENT = Nutrient.FAT;

    private ArrayList<BaseMeal> mMeals;
    private int mDailyCaloriesLimit;
    private int mProteinRatio;
    private int mCarboRatio;
    private int mFatRatio;

    /**
     * Use this constructor to create Diet class to save in SharedPreferences file
     *
     * @param meals              - list of meals properties
     * @param dailyCaloriesLimit - limit of calories per day
     * @param proteinRatio       - percentage of protein per day
     * @param carboRatio         - percentage of carbs per day
     * @param fatRatio           - percentage of fat per day
     */
    public Diet(ArrayList<BaseMeal> meals, int dailyCaloriesLimit, int proteinRatio,
                int carboRatio, int fatRatio) {
        mMeals = meals;
        mDailyCaloriesLimit = dailyCaloriesLimit;
        mProteinRatio = proteinRatio;
        mCarboRatio = carboRatio;
        mFatRatio = fatRatio;
    }

    public ArrayList<BaseMeal> getMeals() {
        return mMeals;
    }

    public int getDailyCaloriesLimit() {
        return mDailyCaloriesLimit;
    }

    public int getProteinRatio() {
        return mProteinRatio;
    }

    public int getCarboRatio() {
        return mCarboRatio;
    }

    public int getFatRatio() {
        return mFatRatio;
    }

    public void setMeals(ArrayList<BaseMeal> mMeals) {
        this.mMeals = mMeals;
    }

    public void setDailyCaloriesLimit(int mDailyCaloriesLimit) {
        this.mDailyCaloriesLimit = mDailyCaloriesLimit;
    }

    public void setProteinRatio(int mProteinRatio) {
        this.mProteinRatio = mProteinRatio;
    }

    public void setCarboRatio(int mCarboRatio) {
        this.mCarboRatio = mCarboRatio;
    }

    public void setFatRatio(int mFatRatio) {
        this.mFatRatio = mFatRatio;
    }

    public void updateMealsCaloriesLimit(){
        for (BaseMeal meal : mMeals){
            meal.setCaloriesLimit(mDailyCaloriesLimit);
        }
    }

    /*
    Helper methods with creating or editing diet plan
     */

    public int getRatioSum() {
        return mProteinRatio + mCarboRatio + mFatRatio;
    }

    public int getRatioTypeWeightLimit(@NutrientRatioType int nutrientRatioType){
        switch (nutrientRatioType){
            case PROTEIN_PERCENT:
                return Utilities.kcalToProteinWeight(getProteinCalories());

            case CARBO_PERCENT:
                return Utilities.kcalToCarbohydratesWeight(getCarboCalories());

            case FAT_PERCENT:
                return Utilities.kcalToFatWeight(getFatCalories());

            default:
                return 0;
        }
    }

    public int getProteinCalories() {
        return (mProteinRatio * mDailyCaloriesLimit) / 100;
    }

    public int getProteinWeight() {
        return Utilities.kcalToProteinWeight(getProteinCalories());
    }

    public int getCarboCalories() {
        return (mCarboRatio * mDailyCaloriesLimit) / 100;
    }

    public int getCarboWeight() {
        return Utilities.kcalToCarbohydratesWeight(getCarboCalories());
    }

    public int getFatCalories() {
        return (mFatRatio * mDailyCaloriesLimit) / 100;
    }

    public int getFatWeight() {
        return Utilities.kcalToFatWeight(getFatCalories());
    }

    public int getTotalNutrientWeight() {
        return getProteinWeight() + getCarboWeight() + getFatWeight();
    }

    public BaseMeal getMeal(int position) {
        return mMeals.get(position);
    }

    public int getMealsCaloriesPercentSum() {
        int caloriesSum = 0;
        for (BaseMeal meal : mMeals) {
            caloriesSum += meal.getPercentDailyLimit();
        }
        return caloriesSum;
    }

    public int getMealsCaloriesSum() {
        return getMealsCaloriesPercentSum() * mDailyCaloriesLimit/100;
    }

    public int getTotalCaloriesInMeals() {
        return (getMealsCaloriesPercentSum() * getDailyCaloriesLimit()) / 100;
    }

    public boolean isMealCaloriesPercentNull() {
        for (BaseMeal meal : mMeals) {
            if (meal.getPercentDailyLimit() == 0) return true;
        }
        return false;
    }

    public boolean isMealNameNotSet() {
        for (BaseMeal meal : mMeals) {
            if (meal.getNameId() == BaseMeal.TYPE_OTHER) {
                if (meal.getName() == null) {
                    return true;
                } else if (meal.getName().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

}

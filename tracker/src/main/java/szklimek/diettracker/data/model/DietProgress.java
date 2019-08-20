package szklimek.diettracker.data.model;

import java.util.ArrayList;
import java.util.List;

import szklimek.diettracker.Utilities;

/**
 * Class representing single day progress of diet.
 */

public class DietProgress extends Diet {

    private List<Meal> mMeals;

    public DietProgress(Diet dietPlan) {
        super(
                dietPlan.getMeals(),
                dietPlan.getDailyCaloriesLimit(),
                dietPlan.getProteinRatio(),
                dietPlan.getCarboRatio(),
                dietPlan.getFatRatio()
        );

        mMeals = new ArrayList<>();
        for (BaseMeal baseMeal : getMeals()) {
            mMeals.add(new Meal(baseMeal));
        }

    }

    public List<Meal> getMealsData(){
        return mMeals;
    }

    public void addFoodToMeal(int mealId, Food food){
        mMeals.get(mealId).addFood(food);
    }

    public double getTotalNutrientValue(@Nutrient.NutrientType int nutrientId){
        double nutrientSum = 0;
        for (Meal meal : mMeals){
            nutrientSum += meal.getTotalNutrientValue(nutrientId);
        }
        return nutrientSum;
    }

    public int getRatioTypePercent(@NutrientRatioType int nutrientRatioId){

        // Counts sum of calories from macronutrients
        int totalMacronutrientsSum =
                Utilities.proteinWeightToKcal((int) getTotalNutrientValue(Nutrient.PROTEIN)) +
                        + Utilities.proteinWeightToKcal((int) getTotalNutrientValue(Nutrient.CARBOHYDRATE))
                        + Utilities.proteinWeightToKcal((int) getTotalNutrientValue(Nutrient.FAT));
        if(totalMacronutrientsSum == 0) return 0;

        int nutrientCalories = 0;
        int nutrientValue = (int) getTotalNutrientValue(nutrientRatioId);
        switch (nutrientRatioId){
            case PROTEIN_PERCENT:
                nutrientCalories = Utilities.proteinWeightToKcal(nutrientValue);
                break;

            case CARBO_PERCENT:
                nutrientCalories = Utilities.carbohydratesWeightToKcal(nutrientValue);
                break;

            case FAT_PERCENT:
                nutrientCalories = Utilities.fatWeightToKcal(nutrientValue);
                break;
        }
        return (nutrientCalories * 100/totalMacronutrientsSum);
    }

    /**
     * Counts percent of used calories of daily calories limit
     *
     * @return percent of calories eaten from daily calories limit
     */
    public int getUsedCaloriesPercent(){

        return (int) getTotalNutrientValue(Nutrient.ENERGY) * 100/getTotalCaloriesInMeals();

    }

    /**
     * Counts percent of limit of eaten NutrientRatio (protein, carbs or fat)
     *
     * @return percent of weight from protein, carbs or fat from the limit
     */
    public int getUsedRatioTypePercent(@NutrientRatioType int nutrientRatioId){
       return (int) getTotalNutrientValue(nutrientRatioId) * 100/ getRatioTypeWeightLimit(nutrientRatioId);
    }

}

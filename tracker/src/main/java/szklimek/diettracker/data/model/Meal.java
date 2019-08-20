package szklimek.diettracker.data.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing single meal, which contains all meal properties and additionally
 * list of added food. Used to count and track nutrients values of whole meal.
 */

public class Meal extends BaseMeal {

    private List<Food> foods;

    // Constructor which get BaseMeal as parameter to copy all data
    public Meal(BaseMeal meal){
        super(meal.getNumber(), meal.getNameId(), meal.getName(),
                meal.getTimeHour(), meal.getTimeMinutes(),
                meal.getPercentDailyLimit(), meal.getCaloriesLimit());
        foods = new ArrayList<>();
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public void addFood(Food food){
        foods.add(food);
    }

    /**
     * Count chosen nutrient value in whole meal
     *
     * @param nutrientId - id of nutrient
     * @return sum of chosen nutrient weight in grams in whole meal
     */
    public double getTotalNutrientValue(@Nutrient.NutrientType int nutrientId){
        double nutrientSum = 0;
        for(Food food : foods){
            nutrientSum += food.getNutrient(nutrientId);
        }
        return nutrientSum;
    }

    /**
     * Count percent of calories used of calories limit set to this meal
     *
     * @return percent of used calories of this meal
     */
    public int getCaloriesUsedPercent(){
        if (getCaloriesLimit() == 0) return 0;
        return (int) getTotalNutrientValue(Nutrient.ENERGY) * 100/ getCaloriesLimit();
    }

}

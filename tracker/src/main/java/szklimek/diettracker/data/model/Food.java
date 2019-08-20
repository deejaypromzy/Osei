package szklimek.diettracker.data.model;
import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Class representing food
 */

public abstract class Food {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FOOD_PRODUCT, FOOD_DISH})
    public @interface FoodType {
    }
    public static final int FOOD_PRODUCT = 0;
    public static final int FOOD_DISH = 1;

    private long id; // Id of food from database
    private int foodType; // Type of food
    private int weight; // Weight of food
    private int mealId; // (Optional) Id of meal to which food is added
    private long addDate; // (Optional) date of addition
    private String name; // Name of food
    private String description; // Food description


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFoodType() {
        return foodType;
    }

    public void setFoodType(@FoodType int foodType) {
        this.foodType = foodType;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getAddDate() {
        return addDate;
    }

    public void setAddDate(long addDate) {
        this.addDate = addDate;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public double getNutrient(@Nutrient.NutrientType int nutrientId){
        return (getNutrientPer100g(nutrientId) * weight)/100;
    }

    public abstract int getType();

    public abstract double getNutrientPer100g(@Nutrient.NutrientType int nutrientId);

    public abstract double getNutrientPerWeight(@Nutrient.NutrientType int nutrientId, int weight);

}

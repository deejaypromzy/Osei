package szklimek.diettracker.data.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * Class representing single dish
 */

public class Dish extends Food {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DISH_1, DISH_2, DISH_3, DISH_4, DISH_5,
            DISH_6,DISH_7,DISH_8, DISH_9, DISH_10,
            DISH_11, DISH_12, DISH_13, DISH_14, DISH_15,
            DISH_16, DISH_17})
    public @interface DishType {
    }

    // Possible dish type values TODO Create possible values
    public static final int DISH_ALL = 0;
    public static final int DISH_1 = 1;
    public static final int DISH_2 = 2;
    public static final int DISH_3 = 3;
    public static final int DISH_4 = 4;
    public static final int DISH_5 = 5;
    public static final int DISH_6 = 6;
    public static final int DISH_7 = 7;
    public static final int DISH_8 = 8;
    public static final int DISH_9 = 9;
    public static final int DISH_10 = 10;
    public static final int DISH_11 = 11;
    public static final int DISH_12 = 12;
    public static final int DISH_13 = 13;
    public static final int DISH_14 = 14;
    public static final int DISH_15 = 15;
    public static final int DISH_16 = 16;
    public static final int DISH_17 = 17;

    private int dishType;  // Type of dish
    private ArrayList<Product> dishProducts; // List of products from which dish consists of

    /**
     * Use this constructor to build Dish object with all fields
     *
     * @param dishId - id of dish from dishes table
     * @param dishType - type of dish
     * @param dishName - name of dish
     */
    public Dish(long dishId, int dishType, String dishName, String dishDescription) {
        setId(dishId);
        setName(dishName);
        setDescription(dishDescription);
        setFoodType(FOOD_DISH);
        this.dishType = dishType;
        dishProducts = new ArrayList<>();
    }

    /**
     * Used only to build new empty dish cache in creating new dish
     */
    public Dish(){
        dishProducts = new ArrayList<>();
        setFoodType(FOOD_DISH);
    }


    public void setType(@DishType int type) {
        this.dishType = type;
    }

    public int getType() {
        return dishType;
    }

    public void addProduct(Product product){
        dishProducts.add(product);
    }

    public void removeProduct(int index){
        dishProducts.remove(index);
    }

    public void updateProduct(int index, Product product){
        dishProducts.remove(index);
        dishProducts.add(index, product);
    }

    public ArrayList<Product> getDishProductsList(){
        return dishProducts;
    }

    public int getTotalDishProductsWeight() {
        int totalWeight = 0;
        for(Product product : dishProducts){
            totalWeight += product.getWeight();
        }
        return totalWeight;
    }

    public double getTotalNutrientValue(@Nutrient.NutrientType int nutrientType){
        double nutrientValue = 0;
        if(dishProducts != null){
            for(Product product : dishProducts){
                nutrientValue += product.getNutrientPerWeight(nutrientType, product.getWeight());
            }
        }
        return nutrientValue;
    }

    @Override
    public double getNutrientPer100g(@Nutrient.NutrientType int nutrientId) {
        double totalNutrientValue = getTotalNutrientValue(nutrientId);
        return (totalNutrientValue * 100)/ getTotalDishProductsWeight();
    }

    @Override
    public double getNutrientPerWeight(@Nutrient.NutrientType int nutrientId, int weight) {
        return (getNutrientPer100g(nutrientId) * weight)/ 100;
    }

}

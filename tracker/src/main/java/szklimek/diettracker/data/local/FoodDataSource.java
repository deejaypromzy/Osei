package szklimek.diettracker.data.local;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import szklimek.diettracker.addfood.menu.AddMenuContract;
import szklimek.diettracker.data.model.Dish;
import szklimek.diettracker.data.model.Food;
import szklimek.diettracker.data.model.Meal;
import szklimek.diettracker.data.model.Nutrient;
import szklimek.diettracker.data.model.NutrientCategory;
import szklimek.diettracker.data.model.Product;

/**
 * Entry point for accessing food data.
 */

public interface FoodDataSource {

    // Dishes methods
    interface GetDishesCallback {

        void onDishesLoaded(List<Dish> dishesList);

        void onDataNotAvailable();
    }

    interface GetDishCallback {

        void onDishLoaded(Dish dish);

        void onDataNotAvailable();
    }

    interface AddDishCallback {

        void onDishAdded();

    }

    interface UpdateDishCallback {

        void onDishUpdated();

    }

    void getDishes(GetDishesCallback callback);

    void getDish(GetDishCallback callback, long dishId);

    void addDish(AddDishCallback callback, Dish dish);

    void updateDish(UpdateDishCallback callback, Dish dish);

    void deleteDishes(ArrayList<String> dishesIds);

    void deleteHiddenDishes();

    void setDishHidden(Dish dish);

    void setDishShown(Dish dish);


    // Products method
    interface GetProductsCallback {

        void onProductsLoaded(List<Product> productsList);

        void onDataNotAvailable();
    }

    void getProducts(GetProductsCallback callback, String nameQuery, int productCategory,
                     SparseArray<NutrientCategory> nutrients);

    // Nutrients method
    interface GetNutrientsCallback {
        void onNutrientsLoaded(SparseArray<Nutrient> nutrients);
    }

    void getNutrients(GetNutrientsCallback callback);

    // Used Food methods
    interface GetUsedFoodCallback{

        void onUsedFoodLoaded(List<Food> foods);

        void onDataNotAvailable();

    }

    void getUsedFood(GetUsedFoodCallback callback, long date);

    interface GetRecentlyAddedFoodCallback {
        void onFoodLoaded(List<Food> foods);

        void onDataNotAvailable();
    }

    void getRecentlyAddedFood(GetRecentlyAddedFoodCallback callback);

    void addToUsedFood(int mealId, Food food);

}

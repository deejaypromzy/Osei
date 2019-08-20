package szklimek.diettracker.main;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import szklimek.diettracker.data.local.ProductsDataSource;
import szklimek.diettracker.data.local.SharedPreferencesSource;
import szklimek.diettracker.data.local.FoodDataSource;
import szklimek.diettracker.data.model.Diet;
import szklimek.diettracker.data.model.DietProgress;
import szklimek.diettracker.data.model.Food;
import szklimek.diettracker.data.model.Meal;
import szklimek.diettracker.main.dailyprogress.DailyProgressContract;
import szklimek.diettracker.main.dailyprogresspage.DailyProgressPageContract;
import szklimek.diettracker.main.mealdetails.MealDetailsContract;

/**
 * Model class which controls data flow in MainActivity, get data from FoodRepository
 * and SharedPreferencesManager, and store cached data
 */

public class MainModel implements
        MainContract.Model,
        DailyProgressContract.Model,
        DailyProgressPageContract.Model,
        MealDetailsContract.Model{

    private static MainModel INSTANCE = null;

    private FoodDataSource mFoodDataSource;

    private ProductsDataSource mProductsDataManager;

    private SharedPreferencesSource mSharedPreferencesManager;

    private Diet mDiet;

    private Meal mClickedMeal;

    private Calendar mDisplayedDate;

    private MainModel(@NonNull FoodDataSource foodDataSource,
                      @NonNull SharedPreferencesSource manager,
                      @NonNull ProductsDataSource productsDataSource) {

        mFoodDataSource = foodDataSource;
        mSharedPreferencesManager = manager;
        mProductsDataManager = productsDataSource;

        checkDatabaseState();
        mDisplayedDate = Calendar.getInstance();
        mDisplayedDate.setTimeInMillis(System.currentTimeMillis());
    }

    static MainModel getInstance(@NonNull FoodDataSource foodDataSource,
                                 @NonNull SharedPreferencesSource manager,
                                 @NonNull ProductsDataSource productsDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MainModel(foodDataSource, manager, productsDataSource);
        }
        return INSTANCE;
    }

    /**
     * If it is first launch, products table needs to be populated from products file
     */
    @Override
    public void checkDatabaseState() {
        if(mSharedPreferencesManager.isFirstLaunch()) {
            mProductsDataManager.insertProductsDataToDatabase();
        }
    }

    /**
     * Used to get diet from SharedPreferencesSource and inform activity if user had set
     * his/her diet plan.
     *
     * @param callback - called after getting diet plan from SharedPreferences
     */
    @Override
    public void getDiet(final MainContract.Model.GetDietCallback callback) {
        mSharedPreferencesManager.getDiet(new SharedPreferencesSource.GetDietCallback() {
            @Override
            public void onDietLoaded(@NonNull Diet diet) {
                mDiet = diet;
                callback.onDietLoaded(diet);
            }

            @Override
            public void onDietNotSet() {
                callback.onDietNotSet();
            }
        });
    }


    /**
     * Used in DailyProgressFragment to get information about diet.
     *
     * @param callback - used to send back diet plan
     */
    @Override
    public void getDietProgress(final GetDietProgressCallback callback, long date) {
        FoodDataSource.GetUsedFoodCallback usedFoodCallback = new FoodDataSource.GetUsedFoodCallback() {
            @Override
            public void onUsedFoodLoaded(List<Food> foods) {
                DietProgress dailyDietProgress = new DietProgress(mDiet);
                for(Food food : foods){
                    dailyDietProgress.addFoodToMeal(food.getMealId(), food);
                }
                callback.onDietProgressLoaded(dailyDietProgress);
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("Error", "Daily meals data is not available");
            }
        };
        mFoodDataSource.getUsedFood(usedFoodCallback, date);
    }

    @Override
    public void setClickedMeal(Meal meal) {
        mClickedMeal = meal;
    }

    @Override
    public void getClickedMeal(GetClickedMealCallback callback) {
        callback.onMealLoaded(mClickedMeal);
    }

    @Override
    public void getDate(GetDateCallback callback) {
        callback.onDateLoaded(mDisplayedDate);
    }
}

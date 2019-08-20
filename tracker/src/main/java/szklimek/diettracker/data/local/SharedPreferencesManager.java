package szklimek.diettracker.data.local;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import szklimek.diettracker.data.model.Diet;

/**
 * Implementation of SharedPreferencesSource
 */

public class SharedPreferencesManager implements SharedPreferencesSource {

    private static final String MAX_FOOD_WEIGHT_KEY = "max_amount";

    private static final String DIET_VALUES_KEY = "diet_values";

    private static final String FIRST_LAUNCH_KEY = "first_launch";

    private static SharedPreferencesManager INSTANCE = null;

    private SharedPreferences mSharedPreferences;

    // Singleton
    private SharedPreferencesManager(@NonNull SharedPreferences sharedPreferences) {
       mSharedPreferences = sharedPreferences;
    }

    public static SharedPreferencesManager getInstance(@NonNull SharedPreferences sharedPreferences) {
        if (INSTANCE == null) {
            INSTANCE = new SharedPreferencesManager(sharedPreferences);
        }
        return INSTANCE;
    }

    /**
     * Check SharedPreferences if application is launched for the first time
     *
     * @return true if it is first launch
     */
    @Override
    public boolean isFirstLaunch() {
        boolean isFirstLaunch = mSharedPreferences.getBoolean(FIRST_LAUNCH_KEY, true);
        if(isFirstLaunch){
            SharedPreferences.Editor shPrefEditor = mSharedPreferences.edit();
            shPrefEditor.putBoolean(FIRST_LAUNCH_KEY, false);
            shPrefEditor.apply();
        }
        return isFirstLaunch;
    }

    /**
     * Get max weight of food from SharedPreferences file
     *
     * @return max food weight to add set by user
     */
    @Override
    public int getMaxFoodWeight(){
        return mSharedPreferences.getInt(MAX_FOOD_WEIGHT_KEY, 1000);
    }

    /**
     * Saves set by user diet parameters such like calories limit, nutrient ratio
     * and configured meals
     *
     * @param diet - diet data to store
     */
    @Override
    public void saveDiet(Diet diet) {
        Gson gson = new Gson();

        SharedPreferences.Editor shPrefEditor = mSharedPreferences.edit();
        shPrefEditor.putString(DIET_VALUES_KEY, gson.toJson(diet));
        shPrefEditor.apply();
    }

    /**
     * Retrieve from SharedPreferences user diet plan
     *
     * @param callback - fired after completion of reading data and converting to diet object
     */
    @Override
    public void getDiet(GetDietCallback callback) {

        String dietString = mSharedPreferences.getString(DIET_VALUES_KEY, "");
        if(dietString.isEmpty()) callback.onDietNotSet();
        else {
            Gson gson = new Gson();
            Diet diet = gson.fromJson(dietString, Diet.class);
            callback.onDietLoaded(diet);
        }

    }
}

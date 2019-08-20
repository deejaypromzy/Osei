package szklimek.diettracker;

import android.content.Context;
import android.os.Build;
import android.text.format.Time;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import szklimek.diettracker.data.model.BaseMeal;
import szklimek.diettracker.data.model.Diet;
import szklimek.diettracker.data.model.Food;

/**
 * Helper methods connected with converting data types, date or time in different formats
 */

public class Utilities {


    /**
     * Helper methods to convert mEnergy values of proteins, carbohydrates and mFat
     * to weight in grams
     */
    public static int proteinWeightToKcal(int proteinWeightInGram) {
        return proteinWeightInGram * 4;
    }

    public static int kcalToProteinWeight(int kcalProtein) {
        return kcalProtein / 4;
    }

    public static int carbohydratesWeightToKcal(int carbohydratesWeightInGram) {
        return carbohydratesWeightInGram * 4;
    }

    public static int kcalToCarbohydratesWeight(int kcalCarbohydrates) {
        return kcalCarbohydrates / 4;
    }

    public static int fatWeightToKcal(int fatWeightInGram) {
        return fatWeightInGram * 9;
    }

    public static int kcalToFatWeight(int kcalFat) {
        return kcalFat / 9;
    }

    public static int getMealNameByNameId(int nameId) {
        switch (nameId) {
            case BaseMeal.TYPE_BREAKFAST:
                return R.string.meal_name_breakfast;
            case BaseMeal.TYPE_ND_BREAKFAST:
                return R.string.meal_name_nd_breakfast;
            case BaseMeal.TYPE_LUNCH:
                return R.string.meal_name_lunch;
            case BaseMeal.TYPE_DINNER:
                return R.string.meal_name_dinner;
            case BaseMeal.TYPE_BRUNCH:
                return R.string.meal_name_brunch;
            case BaseMeal.TYPE_CHEAT_MEAL:
                return R.string.meal_name_cheat_meal;
            case BaseMeal.TYPE_SNACK:
                return R.string.meal_name_snack;
            case BaseMeal.TYPE_SUPPER:
                return R.string.meal_name_supper;
        }
        return 0;
    }

    public static int getFoodTypeString(int foodType) {
        switch (foodType) {
            case Food.FOOD_DISH:
                return R.string.food_type_dish;
            case Food.FOOD_PRODUCT:
                return R.string.food_type_product;
            default:
                return 0;
        }
    }

    public static String getStringNutrientType(Context context, int nutrientType) {
        String[] nutrientsType = context.getResources().getStringArray(R.array.nutrients);
        return nutrientsType[nutrientType];
    }

    public static String getStringProductType(Context context, int productType) {
        String[] productTypesArray = context.getResources().getStringArray(R.array.product_types);
        return productTypesArray[productType];

    }

    public static String getStringDishType(Context context, int dishType) {
        String[] dishTypesArray = context.getResources().getStringArray(R.array.dish_types);
        return dishTypesArray[dishType];

    }

    public static String getTimeToDisplay(int hour, int minutes) {
        DateFormat simpleDateFormat = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * Given a Calendar object, returns 3 letters of month and day number
     * and if it's today then it just returns "Today"
     *
     * @param context  - in order to get access to resources
     * @param calendar - date to get day of week from
     * @return - formatted day
     */
    public static String getFormattedDay(Context context, Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d");
        Date date = new Date(calendar.getTimeInMillis());

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeInMillis(System.currentTimeMillis());

        // Check if it's today
        if (calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) // same year
                && calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) // same month
                && calendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH)) { // same day
            // it's today
            return context.getString(R.string.date_format_today);
        }

        return dateFormat.format(date);
    }

    /**
     * Given a Calendar object, returns time difference between current time with format
     * " min ago" if it is the same hour,
     * " h ago" if it is the same day,
     * day and 3 letters of month if it is the same year - ex. "7 July"
     * day, 3 letters of month and year if it is different year
     *
     * @param context in order to get access to resources
     * @param timeInMilis - date to which compare time
     * @return - formatted time
     */
    public static String getFormattedTimeDifference(Context context, long timeInMilis) {
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTimeInMillis(System.currentTimeMillis());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMilis);

        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");

        if (currentTime.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            if (currentTime.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                    && currentTime.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                // Same day
                long differenceInMilis = currentTime.getTimeInMillis() - calendar.getTimeInMillis();

                // Same hour
                if (currentTime.get(Calendar.HOUR_OF_DAY) == calendar.get(Calendar.HOUR_OF_DAY)) {

                    if (currentTime.get(Calendar.MINUTE) == calendar.get(Calendar.MINUTE)) {
                        // Same minute
                        // Format time: moment ago
                        return context.getString(R.string.time_format_moment_ago);
                    }

                    // Same hour but not minute
                    // Format time: ... min ago
                    long minutesAgo = getMinutesFromMiliseconds(differenceInMilis);
                    return context.getString(R.string.time_format_minutes_ago,
                            (int) minutesAgo);
                }


                // Same day but not same hour
                // Format time: ... h ago
                long hoursAgo = getHoursFromMiliseconds(differenceInMilis);
                return context.getString(R.string.time_format_hour_ago,
                            (int) hoursAgo);
            }

            // Same year but not day
            SimpleDateFormat sameYearFormat = new SimpleDateFormat("d MMM");
            return sameYearFormat.format(calendar.getTime());

        }
        return dateFormat.format(calendar.getTime());

    }

    public static long getMinutesFromMiliseconds(long timeInMilis){
        return timeInMilis/(1000 * 60);
    }

    public static long getHoursFromMiliseconds(long timeInMilis){
        return timeInMilis/(1000 * 60 * 60);
    }

}

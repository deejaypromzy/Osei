package szklimek.diettracker;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.Map;
import java.util.Set;

import szklimek.diettracker.data.local.database.FoodContract;
import szklimek.diettracker.data.model.Food;
import szklimek.diettracker.data.model.BaseMeal;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Helpful functions to test.
 */

public class TestUtilities {

    // Create test values to insert
    static ContentValues createProductValues() {
        ContentValues values = new ContentValues();
        values.put(FoodContract.ProductsEntry.COLUMN_NAME, "TEST");
        values.put(FoodContract.ProductsEntry.COLUMN_NDBNO, 123);
        values.put(FoodContract.ProductsEntry.COLUMN_PRODUCT_TYPE, 1);
        values.put(FoodContract.ProductsEntry.COLUMN_ENERGY, 81.1);
        values.put(FoodContract.ProductsEntry.COLUMN_PROTEIN, 71.2);
        values.put(FoodContract.ProductsEntry.COLUMN_FAT, 13.3);
        values.put(FoodContract.ProductsEntry.COLUMN_CARBOHYDRATES, 75);
        values.put(FoodContract.ProductsEntry.COLUMN_SUGARS, 65);
        values.put(FoodContract.ProductsEntry.COLUMN_WATER, 55);
        values.put(FoodContract.ProductsEntry.COLUMN_FIBER, 5.3);
        values.put(FoodContract.ProductsEntry.COLUMN_CA, 321);
        values.put(FoodContract.ProductsEntry.COLUMN_FE, 321);
        values.put(FoodContract.ProductsEntry.COLUMN_MG, 321);
        values.put(FoodContract.ProductsEntry.COLUMN_P, 321);
        values.put(FoodContract.ProductsEntry.COLUMN_NA, 321);
        values.put(FoodContract.ProductsEntry.COLUMN_K, 321);
        values.put(FoodContract.ProductsEntry.COLUMN_ZN, 321);

        return values;
    }

    static ContentValues createUsedFoodValues(long productId) {
        ContentValues values = new ContentValues();
        long date = 1495714184944L;
        values.put(FoodContract.UsedFoodEntry.COLUMN_FOOD_ID, productId);
        values.put(FoodContract.UsedFoodEntry.COLUMN_FOOD_TYPE, Food.FOOD_DISH);
        values.put(FoodContract.UsedFoodEntry.COLUMN_MEAL_ID, BaseMeal.MEAL_1);
        values.put(FoodContract.UsedFoodEntry.COLUMN_DATE, date);
        values.put(FoodContract.UsedFoodEntry.COLUMN_WEIGHT, 13.3);

        return values;
    }

    static ContentValues createDishValues() {
        ContentValues values = new ContentValues();
        long date = 1495714184944L;
        values.put(FoodContract.DishesEntry.COLUMN_NAME, "breakfast");
        values.put(FoodContract.DishesEntry.COLUMN_DISH_TYPE, 1);
        values.put(FoodContract.DishesEntry.COLUMN_DATE, date);
        values.put(FoodContract.DishesEntry.COLUMN_DISPLAYED, FoodContract.DishesEntry.DISH_SHOWN);

        return values;
    }

    static ContentValues createDishProductValues(){
        ContentValues values = new ContentValues();
        values.put(FoodContract.DishProductsEntry.COLUMN_DISH_ID, 1);
        values.put(FoodContract.DishProductsEntry.COLUMN_PRODUCT_ID, 1);
        values.put(FoodContract.DishProductsEntry.COLUMN_WEIGHT, 12.6);

        return values;
    }

    static ContentValues[] createProductsBulkInsertValues() {
                final int NUMBER_OF_VALUES_TO_INSERT = 100;
        ContentValues[] valuesArray = new ContentValues[NUMBER_OF_VALUES_TO_INSERT];
        for(int i = 0; i < NUMBER_OF_VALUES_TO_INSERT; i++){
            ContentValues values = new ContentValues();
            values.put(FoodContract.ProductsEntry.COLUMN_NAME, "TEST" + i);
            values.put(FoodContract.ProductsEntry.COLUMN_NDBNO, i);
            values.put(FoodContract.ProductsEntry.COLUMN_PRODUCT_TYPE, 1);
            values.put(FoodContract.ProductsEntry.COLUMN_ENERGY, 81.1);
            values.put(FoodContract.ProductsEntry.COLUMN_PROTEIN, 71.2);
            values.put(FoodContract.ProductsEntry.COLUMN_FAT, 13.3);
            values.put(FoodContract.ProductsEntry.COLUMN_CARBOHYDRATES, 75);
            values.put(FoodContract.ProductsEntry.COLUMN_SUGARS, 65);
            values.put(FoodContract.ProductsEntry.COLUMN_WATER, 55);
            values.put(FoodContract.ProductsEntry.COLUMN_FIBER, 5.5);
            values.put(FoodContract.ProductsEntry.COLUMN_CA, 321);
            values.put(FoodContract.ProductsEntry.COLUMN_FE, 321);
            values.put(FoodContract.ProductsEntry.COLUMN_MG, 321);
            values.put(FoodContract.ProductsEntry.COLUMN_P, 321);
            values.put(FoodContract.ProductsEntry.COLUMN_NA, 321);
            values.put(FoodContract.ProductsEntry.COLUMN_K, 321);
            values.put(FoodContract.ProductsEntry.COLUMN_ZN, 321);
            valuesArray[i] = values;
        }
        return valuesArray;
    }

    // Copied from Udacity Advanced Android Course: https://github.com/udacity/Advanced_Android_Development
    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    // Check the validity of values inside cursor with expected values
    // Copied from Udacity Advanced Android Course: https://github.com/udacity/Advanced_Android_Development
    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    // Copied from Udacity Advanced Android Course: https://github.com/udacity/Advanced_Android_Development
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public boolean waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.

            try {
                mHT.wait(5000);
            } catch (Exception e){
                e.printStackTrace();
            }

            mHT.quit();
            return mContentChanged;
        }
    }

    // Copied from Udacity Advanced Android Course: https://github.com/udacity/Advanced_Android_Development
    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}

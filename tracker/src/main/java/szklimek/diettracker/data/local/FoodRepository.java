package szklimek.diettracker.data.local;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import szklimek.diettracker.R;
import szklimek.diettracker.addfood.menu.AddMenuContract;
import szklimek.diettracker.data.model.Dish;
import szklimek.diettracker.data.model.Food;
import szklimek.diettracker.data.model.Nutrient;
import szklimek.diettracker.data.model.NutrientCategory;
import szklimek.diettracker.data.model.Product;
import szklimek.diettracker.data.local.database.FoodContract;

/**
 * Concrete implementation of FoodDataSource
 */

public class FoodRepository implements FoodDataSource {

    private static final int DISHES_LOADER = 0;
    private static final int DISH_LOADER = 1;
    private static final int DISHES_HIDDEN_LOADER = 2;
    private static final int PRODUCTS_WITH_QUERY_LOADER = 3;
    private static final int USED_FOOD_LOADER = 4;

    private static FoodRepository INSTANCE = null;

    private WeakReference<Context> mContext;

    private FoodRepository(@NonNull Context context) {
        mContext = new WeakReference<>(context);
    }

    public static FoodRepository getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FoodRepository(context);
        }
        return INSTANCE;
    }

    /*
    Implementation of dishes methods
     */
    @Override
    public void getDishes(final GetDishesCallback callback) {

        android.content.CursorLoader dishesCursorLoader = new android.content.CursorLoader(mContext.get(),
                FoodContract.DishesEntry.CONTENT_URI_WITH_PRODUCTS,
                null, // all columns
                null, // no selection statements
                null, // no selection args
                null // no sort order
        );

        dishesCursorLoader.registerListener(DISHES_LOADER, new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
                if (data == null || !data.moveToFirst()) {
                    callback.onDataNotAvailable();
                    return;
                }
                List<Dish> dishes = FoodValues.getDishesFromCursor(data);
                callback.onDishesLoaded(dishes);
            }
        });
        dishesCursorLoader.startLoading();
    }

    @Override
    public void getDish(final GetDishCallback callback, final long dishId) {

        final android.content.CursorLoader dishCursorLoader = new android.content.CursorLoader(mContext.get(),
                FoodContract.DishesEntry.CONTENT_URI_WITH_PRODUCTS,
                null, // all columns
                new String(FoodContract.DishProductsEntry.COLUMN_DISH_ID + "=? "), // WHERE dish_id =?
                new String[]{String.valueOf(dishId)}, // id of expected dish
                null // no sort order
        );

        dishCursorLoader.registerListener(DISH_LOADER, new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
                if (data == null) {
                    callback.onDataNotAvailable();
                    return;
                }
                List<Dish> dishes = FoodValues.getDishesFromCursor(data);
                callback.onDishLoaded(dishes.get(0));
                dishCursorLoader.unregisterListener(this);
            }
        });
        dishCursorLoader.startLoading();
    }

    @Override
    public void addDish(AddDishCallback callback, Dish dish) {

        Uri insertedDishId = mContext.get().getContentResolver().insert(
                FoodContract.DishesEntry.CONTENT_URI,
                FoodValues.createDishValues(dish)
        );

        long insertedRowId = ContentUris.parseId(insertedDishId);

        mContext.get().getContentResolver().bulkInsert(
                FoodContract.DishProductsEntry.CONTENT_URI,
                FoodValues.createDishProductsValues(insertedRowId, dish.getDishProductsList())
        );

        callback.onDishAdded();
    }

    @Override
    public void updateDish(UpdateDishCallback callback, Dish dish) {

        // Update data in dishes table
        mContext.get().getContentResolver().update(
                FoodContract.DishesEntry.buildDishesUri(dish.getId()),
                FoodValues.createDishValues(dish),
                null,
                null
        );

        // Delete all products currently connected with deleted dish
        String deleteSelection = FoodContract.DishProductsEntry.COLUMN_DISH_ID + "=?";
        String[] deleteSelectionArgs = new String[]{String.valueOf(dish.getId())};
        mContext.get().getContentResolver().delete(
                FoodContract.DishProductsEntry.CONTENT_URI,
                deleteSelection,
                deleteSelectionArgs
        );

        // Add new product lists from updated dish
        mContext.get().getContentResolver().bulkInsert(
                FoodContract.DishProductsEntry.CONTENT_URI,
                FoodValues.createDishProductsValues(dish.getId(), dish.getDishProductsList())
        );

        callback.onDishUpdated();

    }

    @Override
    public void deleteDishes(ArrayList<String> dishesIds) {

        StringBuilder selectionDish = new StringBuilder();
        StringBuilder selectionDishProducts = new StringBuilder();
        selectionDish.append(FoodContract.DishesEntry._ID + " IN (");
        selectionDishProducts.append(FoodContract.DishProductsEntry.COLUMN_DISH_ID + " IN (");

        for (String dishId : dishesIds) {
            selectionDish.append("?,");
            selectionDishProducts.append("?,");
        }
        selectionDish.replace(selectionDish.length() - 1, selectionDish.length(), ")");
        selectionDishProducts.replace(selectionDishProducts.length() - 1, selectionDishProducts.length(), ")");

        String[] selectionArgs = dishesIds.toArray(new String[dishesIds.size()]);

        // Delete all dishes with matches provided ids
        mContext.get().getContentResolver().delete(
                FoodContract.DishesEntry.CONTENT_URI, // uri to all dishes
                selectionDish.toString(), // WHERE id IN (?...?) - as many as ids
                selectionArgs  // ids
        );

        // Delete all products connected with deleted dishes
        mContext.get().getContentResolver().delete(
                FoodContract.DishProductsEntry.CONTENT_URI, // uri to all dish products
                selectionDishProducts.toString(), // WHERE dish_id IN (?...?) - as many as ids
                selectionArgs // dish ids
        );


    }

    @Override
    public void deleteHiddenDishes() {

        final ArrayList<String> hiddenDishesIdArray = new ArrayList<>();

        // Get ids of all hidden dishes
        final android.content.CursorLoader dishesCursorLoader = new android.content.CursorLoader(mContext.get(),
                FoodContract.DishesEntry.CONTENT_URI,
                new String[]{FoodContract.DishesEntry._ID},
                FoodContract.DishesEntry.COLUMN_DISPLAYED + "=?",
                new String[]{String.valueOf(FoodContract.DishesEntry.DISH_HIDDEN)},
                null // no sort order
        );


        dishesCursorLoader.registerListener(DISHES_HIDDEN_LOADER, new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        long dishId = cursor.getLong(
                                cursor.getColumnIndex(FoodContract.DishesEntry._ID)
                        );
                        hiddenDishesIdArray.add(String.valueOf(dishId));
                    }
                }
                if (!hiddenDishesIdArray.isEmpty()) {
                    deleteDishes(hiddenDishesIdArray);
                }

                dishesCursorLoader.unregisterListener(this);
                dishesCursorLoader.stopLoading();
            }
        });
        dishesCursorLoader.startLoading();
    }

    @Override
    public void setDishHidden(Dish dish) {
        ContentValues values = new ContentValues();

        values.put(FoodContract.DishesEntry.COLUMN_DISPLAYED, FoodContract.DishesEntry.DISH_HIDDEN);
        mContext.get().getContentResolver().update(
                FoodContract.DishesEntry.buildDishesUri(dish.getId()),
                values,
                null,
                null
        );
    }

    @Override
    public void setDishShown(Dish dish) {
        ContentValues values = new ContentValues();

        values.put(FoodContract.DishesEntry.COLUMN_DISPLAYED, FoodContract.DishesEntry.DISH_SHOWN);
        mContext.get().getContentResolver().update(
                FoodContract.DishesEntry.buildDishesUri(dish.getId()),
                values,
                null,
                null
        );
    }

    /*
    Implementation of products methods
     */
    @Override
    public void getProducts(final GetProductsCallback callback, String nameQuery,
                            int productCategory, final SparseArray<NutrientCategory> nutrients) {

        int argsSize = 0;
        String andSeparator = " AND ";
        String selection = "";
        ArrayList<String> selectionArgs = new ArrayList<>();

        if(!nameQuery.isEmpty()) {
            selection += andSeparator + FoodContract.ProductsFTS.FTS_VIRTUAL_TABLE + " MATCH ? ";
            selectionArgs.add("^" + nameQuery.replace(' ', '*') + "*");
        }

        if (productCategory != Product.PRODUCT_ALL) {
            selection += andSeparator + FoodContract.ProductsEntry.COLUMN_PRODUCT_TYPE + " = ? ";
            selectionArgs.add(String.valueOf(productCategory));
            argsSize++;
        }

        if (nutrients != null) {
            int nutrientsSize = nutrients.size();
            // Iterate over each entry and if nutrient category is checked - add to selection
            for (int key = 0; key < nutrientsSize; key++) {

                String columnName = Nutrient.NUTRIENT_COLUMNS.get(key);

                NutrientCategory nutrient = nutrients.get(key);

                if (nutrient.isChecked()) {

                    if (nutrient.isMinimumValueChecked()) {
                        selection += andSeparator + columnName + " > ? ";
                        selectionArgs.add(String.valueOf(nutrient.getMinValue()));
                        argsSize++;
                    }
                    if (nutrient.isMaximumValueChecked()) {
                        selection += andSeparator + columnName + " < ? ";
                        selectionArgs.add(String.valueOf(nutrient.getMaxValue()));
                        argsSize++;
                    }
                }
            }
        }

        // Remove first "AND" from selection statement
        selection = selection.replaceFirst(andSeparator, "WHERE ");

        android.content.CursorLoader productsCursorLoader = new android.content.CursorLoader(mContext.get(),
                FoodContract.ProductsFTS.CONTENT_URI,
                null, // all columns
                selection, // selection based upon user's query, nutrients list and product category
                selectionArgs.toArray(new String[argsSize]),
                FoodContract.ProductsEntry.COLUMN_NAME // sort order by product name
        );

        productsCursorLoader.registerListener(PRODUCTS_WITH_QUERY_LOADER, new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
                if (data == null || !data.moveToFirst()) {
                    callback.onDataNotAvailable();
                    return;
                }

                int columnsLength = data.getColumnCount();
                HashMap<String, Integer> columnsMap = new HashMap<>(columnsLength);

                for(int i = 0; i < columnsLength; i++){
                    columnsMap.put(data.getColumnName(i), i);
                }

                List<Product> mProductsList = new ArrayList<>();

                data.moveToFirst();
                do {
                    Product.ProductBuilder productBuilder = new Product.ProductBuilder();

                    String nameValue = data.getString(
                            columnsMap.get(FoodContract.ProductsEntry.COLUMN_NAME)
                    );
                    String[] nameParts = nameValue.split(";");
                    if (nameParts.length > 1) {
                        productBuilder
                                .name(nameParts[0])
                                .description(nameParts[1]);
                    } else {
                        productBuilder
                                .name(nameValue);
                    }
                    productBuilder
                            .id(data.getLong(
                                    columnsMap.get(FoodContract.ProductsEntry._ID)))
                            .type(data.getInt(
                                    columnsMap.get(FoodContract.ProductsEntry.COLUMN_PRODUCT_TYPE)))
                            .ndbnoIndex(data.getInt(
                                    columnsMap.get(FoodContract.ProductsEntry.COLUMN_NDBNO)));

                    // Number of nutrients columns in database
                    int nutrientsColumnsSize = Nutrient.NUTRIENT_COLUMNS.size();

                    for (int index = 0; index < nutrientsColumnsSize; index++) {
                        productBuilder.nutrient(
                                index,
                                data.getDouble(
                                        columnsMap.get(
                                                Nutrient.NUTRIENT_COLUMNS.get(index))
                                )
                        );
                    }
                    mProductsList.add(productBuilder.build());
                } while (data.moveToNext());
                callback.onProductsLoaded(mProductsList);
            }
        });
        productsCursorLoader.startLoading();

    }

    /*
    Implementation of nutrients methods
     */
    @Override
    public void getNutrients(GetNutrientsCallback callback) {
        String[] nutrientsNames = mContext.get().getResources().getStringArray(R.array.nutrients);
        int[] nutrientsIds = Nutrient.NUTRIENTS_IDS;
        int nutrientsSize = nutrientsIds.length;
        SparseArray<Nutrient> nutrients = new SparseArray<>(nutrientsSize);
        for (int i = 0; i < nutrientsSize; i++) {
            nutrients.put(nutrientsIds[i], new Nutrient(nutrientsNames[i]));
        }
        callback.onNutrientsLoaded(nutrients);
    }


    /*
    Implementation of used food methods
     */
    @Override
    public void getUsedFood(final GetUsedFoodCallback callback, long date) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.setTimeInMillis(date);

        // Today at 0:00
        startDate.add(Calendar.DAY_OF_MONTH, -1);
        startDate.set(Calendar.HOUR, 12);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        // Today at 24:00
        endDate.setTimeInMillis(date);
        endDate.add(Calendar.DAY_OF_MONTH, 0);
        endDate.set(Calendar.HOUR, 12);
        endDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.SECOND, 0);
        endDate.set(Calendar.MILLISECOND, 0);

        String selection =
                FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME + "."
                        + FoodContract.UsedFoodEntry.COLUMN_DATE + " >= ? " + " AND "
                        + FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME + "."
                        + FoodContract.UsedFoodEntry.COLUMN_DATE + " < ?";
        String[] selectionArgs = new String[]{
                String.valueOf(startDate.getTimeInMillis()),
                String.valueOf(endDate.getTimeInMillis())
        };

        final android.content.CursorLoader usedFoodCursorLoader = new android.content.CursorLoader(mContext.get(),
                FoodContract.UsedFoodEntry.CONTENT_URI,
                null, // all columns
                selection, // no selection statements
                selectionArgs, // no selection args
                FoodContract.UsedFoodEntry._ID // sort by ID
        );

        usedFoodCursorLoader.registerListener(USED_FOOD_LOADER, new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
                List<Food> foods = FoodValues.getFoodFromCursor(data);
                if(foods == null) callback.onDataNotAvailable();
                else callback.onUsedFoodLoaded(foods);
                usedFoodCursorLoader.unregisterListener(this);
            }
        });

        usedFoodCursorLoader.startLoading();
    }

    @Override
    public void addToUsedFood(int mealId, Food food) {
        ContentValues foodValues = FoodValues.createUsedFoodValues(mealId, food);

        mContext.get().getContentResolver().insert(
                FoodContract.UsedFoodEntry.CONTENT_URI,
                foodValues
        );
    }

    @Override
    public void getRecentlyAddedFood(final FoodDataSource.GetRecentlyAddedFoodCallback callback) {

        final android.content.CursorLoader usedFoodCursorLoader = new android.content.CursorLoader(mContext.get(),
                FoodContract.UsedFoodEntry.CONTENT_URI,
                null, // all columns
                null, // no selection statements
                null, // no selection args
                FoodContract.UsedFoodEntry.COLUMN_DATE + " DESC" // sorting by date descending
        );

        usedFoodCursorLoader.registerListener(USED_FOOD_LOADER, new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
                List<Food> foods = FoodValues.getFoodFromCursor(data);
                if(foods == null) callback.onDataNotAvailable();
                else callback.onFoodLoaded(foods);
                usedFoodCursorLoader.unregisterListener(this);
            }
        });

        usedFoodCursorLoader.startLoading();
    }
}

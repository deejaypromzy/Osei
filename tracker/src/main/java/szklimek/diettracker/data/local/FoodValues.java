package szklimek.diettracker.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import szklimek.diettracker.data.model.Dish;
import szklimek.diettracker.data.model.Food;
import szklimek.diettracker.data.model.Nutrient;
import szklimek.diettracker.data.model.Product;
import szklimek.diettracker.data.local.database.FoodContract;

/**
 * Class containing methods to create from Food objects ContentValues object in order to
 * insert or update data in database
 */

class FoodValues {

    static ContentValues createDishValues(Dish dish) {

        // Dish name and description will be saved in one column and parted by ';'
        ContentValues dishesTableValues = new ContentValues();
        dishesTableValues.put(FoodContract.DishesEntry.COLUMN_NAME, dish.getName() + ";" + dish.getDescription());
        dishesTableValues.put(FoodContract.DishesEntry.COLUMN_DISH_TYPE, dish.getType());
        dishesTableValues.put(FoodContract.DishesEntry.COLUMN_DATE, System.currentTimeMillis());
        dishesTableValues.put(FoodContract.DishesEntry.COLUMN_DISPLAYED, FoodContract.DishesEntry.DISH_SHOWN);

        return dishesTableValues;
    }

    static ContentValues[] createDishProductsValues(long dishId, ArrayList<Product> productsList) {

        int productsNumber = productsList.size();
        ArrayList<ContentValues> contentValuesArrayList = new ArrayList<>();

        for (Product product : productsList) {
            ContentValues dishProductsTableValues = new ContentValues();
            dishProductsTableValues
                    .put(FoodContract.DishProductsEntry.COLUMN_PRODUCT_ID, product.getId());
            dishProductsTableValues
                    .put(FoodContract.DishProductsEntry.COLUMN_WEIGHT, product.getWeight());
            dishProductsTableValues
                    .put(FoodContract.DishProductsEntry.COLUMN_DISH_ID, dishId);

            contentValuesArrayList.add(dishProductsTableValues);
        }

        return contentValuesArrayList.toArray(new ContentValues[productsNumber]);
    }

    static ContentValues createUsedFoodValues(int mealId, Food food) {

        ContentValues values = new ContentValues();
        values.put(FoodContract.UsedFoodEntry.COLUMN_MEAL_ID, mealId);
        values.put(FoodContract.UsedFoodEntry.COLUMN_FOOD_TYPE, food.getFoodType());
        values.put(FoodContract.UsedFoodEntry.COLUMN_FOOD_ID, food.getId());
        values.put(FoodContract.UsedFoodEntry.COLUMN_WEIGHT, food.getWeight());
        values.put(FoodContract.UsedFoodEntry.COLUMN_DATE, food.getAddDate());

        return values;
    }

    static List<Dish> getDishesFromCursor(Cursor data){

        long previousDishId = -1;
        ArrayList<Dish> mDishList = new ArrayList<>();

        if(!data.moveToFirst()){
            return mDishList;
        }

        data.moveToFirst();
        do {
            if (data.getInt(
                    data.getColumnIndex(FoodContract.DishesEntry.COLUMN_DISPLAYED)
            ) == FoodContract.DishesEntry.DISH_HIDDEN) continue;

            long dishId = data.getLong(
                    data.getColumnIndex(FoodContract.DishProductsEntry.COLUMN_DISH_ID)
            );
            if (dishId == previousDishId) {

                Product product = getDishProductFromCursorRow(data, data.getPosition());
                mDishList.get(mDishList.size() - 1).addProduct(product);

            } else {

                String nameValue = data.getString(
                        data.getColumnIndex(FoodContract.DishesEntry.COLUMN_NAME)
                );
                String[] nameParts = nameValue.split(";");
                String dishName;
                String dishDescription = "";
                if (nameParts.length > 1) {
                    dishName = nameParts[0];
                    dishDescription = nameParts[1];

                } else dishName = nameParts[0];

                int dishType = data.getInt(
                        data.getColumnIndex(FoodContract.DishesEntry.COLUMN_DISH_TYPE)
                );
                mDishList.add(new Dish(dishId, dishType, dishName, dishDescription));

                previousDishId = dishId;

                Product product = getDishProductFromCursorRow(data, data.getPosition());
                mDishList.get(mDishList.size() - 1).addProduct(product);

            }
        } while (data.moveToNext());
        return mDishList;
    }

    private static Product getDishProductFromCursorRow(Cursor data, int position){

        data.moveToPosition(position);
        Product.ProductBuilder productBuilder = new Product.ProductBuilder();

        String nameValue = data.getString(
                data.getColumnIndex(FoodContract.ProductsEntry.COLUMN_NAME)
        );
        String[] nameParts = nameValue.split(";");
        if (nameParts.length > 1) {
            productBuilder
                    .name(nameParts[0])
                    .description(nameParts[1]);

        } else productBuilder.name(nameParts[0]);

        productBuilder
                .id(data.getLong(
                        data.getColumnIndex(FoodContract.ProductsEntry._ID)))
                .type(data.getInt(
                        data.getColumnIndex(FoodContract.ProductsEntry.COLUMN_PRODUCT_TYPE)))
                .ndbnoIndex(data.getInt(
                        data.getColumnIndex(FoodContract.ProductsEntry.COLUMN_NDBNO)))
                .weight(data.getInt(
                        data.getColumnIndex(FoodContract.DishProductsEntry.COLUMN_WEIGHT)));

        for (int index = 0; index < Nutrient.NUTRIENT_COLUMNS.size(); index++) {
            productBuilder.nutrient(
                    index,
                    data.getDouble(
                            data.getColumnIndex(
                                    Nutrient.NUTRIENT_COLUMNS.get(index)
                            )
                    )
            );
        }
        return productBuilder.build();

    }

    static List<Food> getFoodFromCursor(Cursor data) {
        if (data == null) {
            return null;
        }
        ArrayList<Food> foods = new ArrayList<>();
        long previousDate = -1;
        Dish dishToAdd;
        Product productToAdd;

        if(!data.moveToFirst()){
            return foods;
        }

        do {
            int foodType = data.getInt(
                    data.getColumnIndex(FoodContract.UsedFoodEntry.COLUMN_FOOD_TYPE)
            );

            int mealId = data.getInt(
                    data.getColumnIndex(FoodContract.UsedFoodEntry.COLUMN_MEAL_ID)
            );

            switch (foodType) {
                case Food.FOOD_PRODUCT:
                    long productAddDate = data.getLong(
                            data.getColumnIndex(FoodContract.UsedFoodEntry.COLUMN_DATE)
                    );
                    productToAdd = getProductFromCursorRow(data, data.getPosition());
                    productToAdd.setAddDate(productAddDate);
                    productToAdd.setMealId(mealId);
                    foods.add(productToAdd);
                    break;

                case Food.FOOD_DISH:

                    long addDate = data.getLong(
                            data.getColumnIndex(FoodContract.UsedFoodEntry.COLUMN_DATE)
                    );

                    if (data.getInt(
                            data.getColumnIndex(FoodContract.DishesEntry.COLUMN_DISPLAYED)
                    ) == FoodContract.DishesEntry.DISH_HIDDEN) continue;

                    long dishId = data.getLong(
                            data.getColumnIndex(FoodContract.DishProductsEntry.COLUMN_DISH_ID)
                    );

                    if (addDate == previousDate) {
                        // Product of the same dish, just add product to dish
                        productToAdd = getProductFromCursorRow(data, data.getPosition());
                        productToAdd.setWeight(
                                data.getInt(
                                        data.getColumnIndex(FoodContract.DishProductsEntry.COLUMN_WEIGHT)
                                )
                        );
                        ((Dish) foods.get(foods.size() - 1)).addProduct(productToAdd);

                    } else {
                        // It is new dish so add dish data and product which cursor is currently
                        // pointing at

                        previousDate = addDate;

                        String nameValue = data.getString(
                                data.getColumnIndex(FoodContract.DishesEntry.COLUMN_NAME)
                        );
                        String[] nameParts = nameValue.split(";");
                        String dishName;
                        String dishDescription = "";
                        if (nameParts.length > 1) {
                            dishName = nameParts[0];
                            dishDescription = nameParts[1];

                        } else dishName = nameValue;

                        int dishType = data.getInt(
                                data.getColumnIndex(FoodContract.DishesEntry.COLUMN_DISH_TYPE)
                        );
                        dishToAdd = new Dish(dishId, dishType, dishName, dishDescription);

                        // Add product to dish
                        productToAdd = getProductFromCursorRow(data, data.getPosition());
                        productToAdd.setWeight(
                                data.getInt(
                                        data.getColumnIndex(FoodContract.DishProductsEntry.COLUMN_WEIGHT)
                                )
                        );
                        dishToAdd.addProduct(productToAdd);
                        dishToAdd.setAddDate(addDate);
                        dishToAdd.setMealId(mealId);
                        dishToAdd.setWeight(data.getInt(
                                data.getColumnIndex(FoodContract.UsedFoodEntry.COLUMN_WEIGHT)));
                        foods.add(dishToAdd);
                    }
                    break;
            }
        } while (data.moveToNext());

        return foods;
    }

    private static Product getProductFromCursorRow(Cursor data, int position) {
        data.moveToPosition(position);
        Product.ProductBuilder productBuilder = new Product.ProductBuilder();

        String nameValue = data.getString(
                data.getColumnIndex(FoodContract.ProductsEntry.COLUMN_NAME)
        );
        String[] nameParts = nameValue.split(";");
        if (nameParts.length > 1) {
            productBuilder
                    .name(nameParts[0])
                    .description(nameParts[1]);

        } else productBuilder.name(nameValue);

        productBuilder
                .id(data.getLong(
                        data.getColumnIndex(FoodContract.UsedFoodEntry._ID)))
                .type(data.getInt(
                        data.getColumnIndex(FoodContract.ProductsEntry.COLUMN_PRODUCT_TYPE)))
                .ndbnoIndex(data.getInt(
                        data.getColumnIndex(FoodContract.ProductsEntry.COLUMN_NDBNO)))
                .weight(data.getInt(
                        data.getColumnIndex(FoodContract.UsedFoodEntry.COLUMN_WEIGHT)));

        for (int index = 0; index < Nutrient.NUTRIENT_COLUMNS.size(); index++) {
            productBuilder.nutrient(
                    index,
                    data.getDouble(
                            data.getColumnIndex(
                                    Nutrient.NUTRIENT_COLUMNS.get(index)
                            )
                    )
            );
        }
        return productBuilder.build();
    }

}

package szklimek.diettracker.data.local.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import szklimek.diettracker.data.local.DataUtils;
import szklimek.diettracker.data.model.Food;

/**
 * Database helper for DietTracker App.
 * Manages database creation and version management.
 */

public class FoodDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = FoodDbHelper.class.getSimpleName();

    // Name of the database file
    public static final String DATABASE_NAME = "food.db";
    private static final int DATABASE_VERSION = 1;


    public FoodDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables for products, products used by user to meals creation (or independently)
        // and meals

        // Create String which contains SQL create statements
        String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME + " (" // Table name: products
                + FoodContract.ProductsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "// _ID column
                + FoodContract.ProductsEntry.COLUMN_NAME + " TEXT NOT NULL, "  // Product name
                + FoodContract.ProductsEntry.COLUMN_NDBNO + " INTEGER NOT NULL, "// NDBNO index
                + FoodContract.ProductsEntry.COLUMN_PRODUCT_TYPE + " INTEGER NOT NULL, " // Product type
                + FoodContract.ProductsEntry.COLUMN_MEASURE_NAME + " TEXT, " // Measure name
                + FoodContract.ProductsEntry.COLUMN_MEASURE_VALUE + " INTEGER, " // Measure value
                + FoodContract.ProductsEntry.COLUMN_ENERGY + " REAL, "  // Energy (kcal)
                + FoodContract.ProductsEntry.COLUMN_PROTEIN + " REAL, " // Proteins per 100g (g)
                + FoodContract.ProductsEntry.COLUMN_FAT + " REAL, "     // Lipids per 100g (g)
                + FoodContract.ProductsEntry.COLUMN_CARBOHYDRATES + " REAL, " // Carbohydrates per 100g (g)
                + FoodContract.ProductsEntry.COLUMN_SUGARS + " REAL, "  // Sugars per 100g (g)
                + FoodContract.ProductsEntry.COLUMN_WATER + " REAL, "  // Water per 100g (g)
                + FoodContract.ProductsEntry.COLUMN_FIBER + " REAL, "  // Fiber per 100g (g)
                + FoodContract.ProductsEntry.COLUMN_CA + " REAL, "     // Calcium per 100g (mg)
                + FoodContract.ProductsEntry.COLUMN_FE + " REAL, "     // Iron per 100g (mg)
                + FoodContract.ProductsEntry.COLUMN_MG + " REAL, "     // Magnesium per 100g (mg)
                + FoodContract.ProductsEntry.COLUMN_P + " REAL, "      // Phosphorus per 100g (mg)
                + FoodContract.ProductsEntry.COLUMN_NA + " REAL, "     // Sodium per 100g (mg)
                + FoodContract.ProductsEntry.COLUMN_K + " REAL, "      // Potassium per 100g (mg)
                + FoodContract.ProductsEntry.COLUMN_ZN + " REAL "      // Zinc per 100g (mg)
                + ");";

        String SQL_CREATE_FTS_VIRTUAL_TABLE =
                "CREATE VIRTUAL TABLE " + FoodContract.ProductsFTS.FTS_VIRTUAL_TABLE +
                        " USING fts4 (content=\""
                        + FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME + "\", "
                        + FoodContract.ProductsEntry.COLUMN_NAME + ");";

        String SQL_CREATE_USED_FOOD_TABLE = "CREATE TABLE " +
                FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME + " (" // Table name: used_food
                + FoodContract.UsedFoodEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "// _ID column
                + FoodContract.UsedFoodEntry.COLUMN_FOOD_ID + " INTEGER NOT NULL, " // Id of food from products or dish table
                + FoodContract.UsedFoodEntry.COLUMN_FOOD_TYPE + " INTEGER NOT NULL, " // Type of food (product or dish)
                + FoodContract.UsedFoodEntry.COLUMN_MEAL_ID + " INTEGER NOT NULL, " // Id of meal to which food is added
                + FoodContract.UsedFoodEntry.COLUMN_DATE + " INTEGER NOT NULL, " // Date of addition
                + FoodContract.UsedFoodEntry.COLUMN_WEIGHT + " REAL NOT NULL" // Weight of food
                + ");";

        String SQL_CREATE_DISHES_TABLE = "CREATE TABLE " +
                FoodContract.DishesEntry.DISHES_TABLE_NAME + " (" // Table name: dishes
                + FoodContract.DishesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "// _ID column
                + FoodContract.DishesEntry.COLUMN_DISH_TYPE + " INTEGER NOT NULL, " // Type of dish
                + FoodContract.DishesEntry.COLUMN_DATE + " INTEGER NOT NULL, " // Date of addition
                + FoodContract.DishesEntry.COLUMN_NAME + " TEXT NOT NULL, "  // Dish name (not required)
                + FoodContract.DishesEntry.COLUMN_DISPLAYED + " INTEGER NOT NULL" // State of dish
                + ");";

        String SQL_CREATE_DISH_PRODUCTS_TABLE = "CREATE TABLE " +
                FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME + " (" // Table name: dish_product
                + FoodContract.DishProductsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " // _ID column
                + FoodContract.DishProductsEntry.COLUMN_DISH_ID + " INTEGER NOT NULL, " // Id of dish from dishes table
                + FoodContract.DishProductsEntry.COLUMN_PRODUCT_ID + " INTEGER NOT NULL, " // Id of product from products table
                + FoodContract.DishProductsEntry.COLUMN_WEIGHT + " REAL NOT NULL" // Weight of product
                + ");";

        // SQL products content table with fts table triggers

        // CREATE TRIGGER t2_bu BEFORE UPDATE ON products BEGIN DELETE FROM fts WHERE docid=old._id;
        String
                SQL_PRODUCTS_TRIGGER_BEFORE_UPDATE_DELETE = "CREATE TRIGGER t2_bu BEFORE UPDATE ON "
                + FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME + " BEGIN DELETE FROM "
                + FoodContract.ProductsFTS.FTS_VIRTUAL_TABLE + " WHERE docid=old._id;";

        // CREATE TRIGGER t2_au AFTER UPDATE ON products BEGIN INSERT INTO fts(docid, name) VALUES(new._id, new.name);
        String SQL_PRODUCTS_TRIGGER_AFTER_UPDATE_INSERT = "CREATE TRIGGER t2_au AFTER UPDATE ON "
                + FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME + " BEGIN INSERT INTO "
                + FoodContract.ProductsFTS.FTS_VIRTUAL_TABLE + "(docid, "
                + FoodContract.ProductsEntry.COLUMN_NAME + ") VALUES(new._id, new."
                + FoodContract.ProductsEntry.COLUMN_NAME + ");";

        // CREATE TRIGGER t2_bd BEFORE DELETE ON products BEGIN DELETE FROM fts WHERE docid=old._id;
        String SQL_PRODUCTS_TRIGGER_BEFORE_DELETE_DELETE = "CREATE TRIGGER t2_bd BEFORE DELETE ON "
                + FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME + " BEGIN DELETE FROM "
                + FoodContract.ProductsFTS.FTS_VIRTUAL_TABLE + " WHERE docid=old._id;";

        // CREATE TRIGGER t2_ai AFTER INSERT ON products BEGIN INSERT INTO fts(docid, name)
        // VALUES(new._id, new.name);
        String SQL_PRODUCTS_TRIGGER_AFTER_INSERT_INSERT = "CREATE TRIGGER t2_ai AFTER INSERT ON "
                + FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME + " BEGIN INSERT INTO "
                + FoodContract.ProductsFTS.FTS_VIRTUAL_TABLE + "(docid, "
                + FoodContract.ProductsEntry.COLUMN_NAME + ") VALUES(new._id, new."
                + FoodContract.ProductsEntry.COLUMN_NAME + ");";
        String SQL_END_STATEMENT = "\nEND;";

        // Execute all create statements
        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
        db.execSQL(SQL_CREATE_USED_FOOD_TABLE);
        db.execSQL(SQL_CREATE_DISHES_TABLE);
        db.execSQL(SQL_CREATE_FTS_VIRTUAL_TABLE);
        db.execSQL(SQL_CREATE_DISH_PRODUCTS_TABLE);

        // Execute all triggers statements
        db.execSQL(SQL_PRODUCTS_TRIGGER_BEFORE_UPDATE_DELETE + SQL_END_STATEMENT);
        db.execSQL(SQL_PRODUCTS_TRIGGER_AFTER_UPDATE_INSERT + SQL_END_STATEMENT);
        db.execSQL(SQL_PRODUCTS_TRIGGER_BEFORE_DELETE_DELETE + SQL_END_STATEMENT);
        db.execSQL(SQL_PRODUCTS_TRIGGER_AFTER_INSERT_INSERT + SQL_END_STATEMENT);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Database is in the version 1, no need to make any upgrades.
    }


}


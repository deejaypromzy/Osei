package szklimek.diettracker.data.local.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * API Contract for the DietTracker app.
 */

public class FoodContract {

    // Empty constructor to unable instantiating contract class
    private FoodContract() {
    }

    // Content authority - name of the content provider
    public static final String CONTENT_AUTHORITY = "szklimek.diettracker.provider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths
    public static final String PATH_PRODUCTS = "products";
    public static final String PATH_USED_FOOD = "used_products";
    public static final String PATH_DISHES = "dishes";
    public static final String PATH_PRODUCTS_FTS = "products_fts";
    public static final String PATH_DISH_PRODUCTS = "dish_products";
    public static final String PATH_DISH_WITH_PRODUCTS = "dish_with_products";


    /**
     * Inner class that defines constant values for the product database table.
     * Each entry in the table represents a single food product.
     */
    public static final class ProductsEntry implements BaseColumns {

        // The content URI to access the products data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);


        // The MIME type of the {@link #CONTENT_URI} for a list of products
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        // The MIME type of the {@link #CONTENT_URI} for a single product
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        // Name of database table for products
        public static final String PRODUCTS_TABLE_NAME = "products";

        // Unique ID number for the product (only for use in the database table).
        // Type: INTEGER
        public static final String _ID = BaseColumns._ID;

        // Unique ID number for the product with table name prefix to use with queries with more
        // than one table.
        // Type: INTEGER
        public static final String FULL_ID = PRODUCTS_TABLE_NAME + "." + BaseColumns._ID;

        // Product name
        // Type: TEXT
        public static final String COLUMN_NAME = "product_name";

        // Index of product from USDA database
        // Type: INTEGER
        public static final String COLUMN_NDBNO = "ndbno_id";

        // Product type
        // Type: INTEGER
        public static final String COLUMN_PRODUCT_TYPE = "product_type";

        // Measure name
        // Type: TEXT
        public static final String COLUMN_MEASURE_NAME = "measure_name";

        // Measure value
        // Type: INTEGER
        public static final String COLUMN_MEASURE_VALUE = "measure_value";

        // Amount of calories in unit (e.g. 200kcal/100g)
        // Type: REAL
        public static final String COLUMN_ENERGY = "energy";

        // Amount of proteins in unit (e.g. 36g/100g)
        // Type: REAL
        public static final String COLUMN_PROTEIN = "protein";

        // Amount of lipids (fat) in unit
        // Type: REAL
        public static final String COLUMN_FAT = "fat";

        // Amount of carbohydrates in unit
        // Type: REAL
        public static final String COLUMN_CARBOHYDRATES = "carbo";

        // Amount of sugars in unit
        // Type: REAL
        public static final String COLUMN_SUGARS = "sugars";

        // Amount of water in unit
        // Type: REAL
        public static final String COLUMN_WATER = "water";

        // Amount of fiber in unit
        // Type: REAL
        public static final String COLUMN_FIBER = "fiber";

        // Amount of calcium in unit
        // Type: REAL
        public static final String COLUMN_CA = "ca";

        // Amount of iron in unit
        // Type: REAL
        public static final String COLUMN_FE = "fe";

        // Amount of magnesium in unit
        // Type: REAL
        public static final String COLUMN_MG = "mg";

        // Amount of phosphorus in unit
        // Type: REAL
        public static final String COLUMN_P = "p";

        // Amount of sodium in unit
        // Type: REAL
        public static final String COLUMN_NA = "na";

        // Amount of potassium in unit
        // Type: REAL
        public static final String COLUMN_K = "k";

        // Amount of zinc in unit
        // Type: REAL
        public static final String COLUMN_ZN = "zn";

        public static Uri buildProductsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /**
     * Inner class that defines constant values for the food used by users (product or dish)
     * Each entry in the table represents a single product or dish added at specified time.
     */
    public static final class UsedFoodEntry implements BaseColumns {

        // The content URI to access the food data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USED_FOOD);


        // The MIME type of the {@link #CONTENT_URI} for a list of foods
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USED_FOOD;

        // The MIME type of the {@link #CONTENT_URI} for a single food
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USED_FOOD;

        // Name of database table for used food
        public static final String USED_FOOD_TABLE_NAME = "used_food";

        // Unique ID number (needed only for database)
        // Type: INTEGER
        public static final String _ID = BaseColumns._ID;

        // Id of added food from product or dish tables dependent on food type
        // Type: INTEGER
        public static final String COLUMN_FOOD_ID = "food_id";

        // Type of food
        // Type: INTEGER
        public static final String COLUMN_FOOD_TYPE = "food_type";

        // Id of meal to which food is added
        // Type: INTEGER
        public static final String COLUMN_MEAL_ID = "meal_id";

        // Date of addition
        // Type: INTEGER NOT NULL
        public static final String COLUMN_DATE = "add_date";

        // Weight of added food
        // Type: INTEGER
        public static final String COLUMN_WEIGHT = "add_weight";

        public static Uri buildUsedFoodUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /**
     * Inner class that defines constant values for the dishes created by users.
     * Each entry in the table represents a single dish created with specific name and date.
     */
    public static final class DishesEntry implements BaseColumns {

        @Retention(RetentionPolicy.SOURCE)
        @IntDef({DISH_SHOWN, DISH_HIDDEN})
        public @interface DishDisplayed {
        }

        public static final int DISH_SHOWN = 0;
        public static final int DISH_HIDDEN = 1;

        // The content URI to access the dish data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DISHES);

        public static final Uri CONTENT_URI_WITH_PRODUCTS =
                Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DISH_WITH_PRODUCTS);

        // The MIME type of the {@link #CONTENT_URI} for a list of dishes
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DISHES;

        // The MIME type of the {@link #CONTENT_URI} for a single dish
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DISHES;

        // Name of database table for dishes
        public static final String DISHES_TABLE_NAME = "dishes";

        // Unique ID number
        // Type: INTEGER
        public static final String _ID = BaseColumns._ID;

        // Unique ID number with table name prefix
        // Type: INTEGER
        public static final String FULL_ID = DISHES_TABLE_NAME + "." + BaseColumns._ID;

        // Dish name
        // Type: TEXT
        public static final String COLUMN_NAME = "dish_name";

        // Date of creation
        // Type: INTEGER NOT NULL
        public static final String COLUMN_DATE = "date";

        // Dish visibility
        // Type: INTEGER NOT NULL
        // Possible values DISH_SHOWN, DISH_HIDDEN
        public static final String COLUMN_DISPLAYED = "dish_displayed";

        // Dish type
        // Type: INTEGER
        public static final String COLUMN_DISH_TYPE = "dish_type";

        public static Uri buildDishesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /**
     * Inner class that defines constant values for the products used to create dish.
     * Each entry in the table represents a single product connected with dish from dishes table.
     */
    public static final class DishProductsEntry implements BaseColumns {

        // The content URI to access the dish data in the provider
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DISH_PRODUCTS);


        // The MIME type of the {@link #CONTENT_URI} for a list of products connected with dish
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/" + PATH_DISH_PRODUCTS;

        // The MIME type of the {@link #CONTENT_URI} for a single product
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/" + PATH_DISH_PRODUCTS;

        // Name of database table for dishes
        public static final String DISH_PRODUCTS_TABLE_NAME = "dish_products";

        // Unique ID number
        // Type: INTEGER
        public static final String _ID = BaseColumns._ID;

        // Unique ID number with table name prefix
        // Type: INTEGER
        public static final String FULL_ID = DISH_PRODUCTS_TABLE_NAME + "." + BaseColumns._ID;

        // Id of dish from dishes table
        // Type: INTEGER
        public static final String COLUMN_DISH_ID = "dish_id";

        // Id of product from products table
        // Type: INTEGER
        public static final String COLUMN_PRODUCT_ID = "product_id";

        // Weight of added product
        // Type: INTEGER
        public static final String COLUMN_WEIGHT = "weight";

        public static Uri buildDishProductUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    /**
     * Inner class that defines constant values for the External Content FTS4 Table
     */
    public static final class ProductsFTS {
        // Name of the extended table content
        public static final String FTS_VIRTUAL_TABLE = "fts";

        // The content URI to access the products data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS_FTS);

        // Default id column for extended content fts table
        public static final String COLUMN_ID = "docid";

        public static Uri buildUriWithQueryString(String query) {
            return Uri.parse(CONTENT_URI + "/" + query);
        }
    }

}

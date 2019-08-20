package szklimek.diettracker.data.local.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import szklimek.diettracker.data.model.Food;

/**
 * {@link #ContentProvider} for DietTracker App
 */

public class FoodProvider extends ContentProvider {

    // UriMatcher used by this ContentProvider
    private static final UriMatcher mUriMatcher = buildUriMatcher();

    // Database helper object
    FoodDbHelper mFoodDbHelper;

    // URI matcher code for the content URI for list of rows and single row
    public static final int PRODUCTS = 0;
    public static final int PRODUCT_ID = 1;
    public static final int USED_FOODS = 2;
    public static final int USED_FOOD_ID = 3;
    public static final int DISHES = 4;
    public static final int DISH_ID = 5;
    public static final int PRODUCTS_FTS = 6;
    public static final int DISH_PRODUCTS = 7;
    public static final int DISH_PRODUCT_ID = 8;
    public static final int DISHES_WITH_PRODUCTS = 9;

    // Build UriMatcher object to match Uri patterns with corresponding code
    public static UriMatcher buildUriMatcher() {

        // UriMatcher object matches a content URI to corresponding code.
        final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FoodContract.CONTENT_AUTHORITY;

        mUriMatcher.addURI(authority, FoodContract.PATH_PRODUCTS, PRODUCTS);
        mUriMatcher.addURI(authority, FoodContract.PATH_PRODUCTS + "/#", PRODUCT_ID);
        mUriMatcher.addURI(authority, FoodContract.PATH_USED_FOOD, USED_FOODS);
        mUriMatcher.addURI(authority, FoodContract.PATH_USED_FOOD + "/#", USED_FOOD_ID);
        mUriMatcher.addURI(authority, FoodContract.PATH_DISHES, DISHES);
        mUriMatcher.addURI(authority, FoodContract.PATH_DISHES + "/#", DISH_ID);
        mUriMatcher.addURI(authority, FoodContract.PATH_PRODUCTS_FTS + "/*", PRODUCTS_FTS);
        mUriMatcher.addURI(authority, FoodContract.PATH_PRODUCTS_FTS + "/", PRODUCTS_FTS);
        mUriMatcher.addURI(authority, FoodContract.PATH_DISH_PRODUCTS, DISH_PRODUCTS);
        mUriMatcher.addURI(authority, FoodContract.PATH_DISH_PRODUCTS + "/#", DISH_PRODUCT_ID);
        mUriMatcher.addURI(authority, FoodContract.PATH_DISH_WITH_PRODUCTS, DISHES_WITH_PRODUCTS);


        return mUriMatcher;
    }

    @Override
    public boolean onCreate() {
        mFoodDbHelper = new FoodDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mFoodDbHelper.getReadableDatabase();

        Cursor cursor;

        // Checking if the UriMatcher can match provided Uri to corresponding code
        int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                cursor = database.query(
                        FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case PRODUCT_ID:
                selection = FoodContract.ProductsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(
                        FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case USED_FOODS:
                // SELECT * FROM used_food
                // LEFT OUTER JOIN dishes ON dishes._id = used_food.food_id
                // LEFT OUTER JOIN dish_products ON dish_products.dish_id = dishes._id
                // INNER JOIN products ON dish_products.product_id = products._id
                // WHERE food_type = 1
                SQLiteQueryBuilder usedFoodQueryBuilder = new SQLiteQueryBuilder();
                usedFoodQueryBuilder.setTables(
                        FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME
                                + " LEFT OUTER JOIN " + FoodContract.DishesEntry.DISHES_TABLE_NAME
                                + " ON " + FoodContract.DishesEntry.FULL_ID
                                + " = " + FoodContract.UsedFoodEntry.COLUMN_FOOD_ID
                                + " LEFT OUTER JOIN " + FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME
                                + " ON " + FoodContract.DishProductsEntry.COLUMN_DISH_ID
                                + " = " + FoodContract.DishesEntry.FULL_ID
                                + " INNER JOIN " + FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME
                                + " ON " + FoodContract.DishProductsEntry.COLUMN_PRODUCT_ID
                                + " = " + FoodContract.ProductsEntry.FULL_ID);

                usedFoodQueryBuilder.appendWhere(FoodContract.UsedFoodEntry.COLUMN_FOOD_TYPE +
                        "=" + Food.FOOD_DISH);

                String dishesUsedFoodQuery = usedFoodQueryBuilder.buildQuery(projection,
                        selection, null, null, null, null);


                // SELECT * FROM used_food
                // LEFT OUTER JOIN dish_products on  dish_products.product_id = used_food.food_id
                // LEFT OUTER JOIN dishes on dishes._id = dish_products.dish_id
                // INNER JOIN products on products._id = used_food.food_id
                // Where food_type = 0

                usedFoodQueryBuilder = new SQLiteQueryBuilder();
                usedFoodQueryBuilder.setTables(
                        FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME
                                + " LEFT OUTER JOIN " + FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME
                                + " ON " + FoodContract.DishProductsEntry.COLUMN_PRODUCT_ID
                                + " = " + FoodContract.UsedFoodEntry.COLUMN_FOOD_ID
                                + " LEFT OUTER JOIN " + FoodContract.DishesEntry.DISHES_TABLE_NAME
                                + " ON " + FoodContract.DishesEntry.FULL_ID
                                + " = " + FoodContract.DishProductsEntry.COLUMN_DISH_ID
                                + " INNER JOIN " + FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME
                                + " ON " + FoodContract.ProductsEntry.FULL_ID
                                + " = " + FoodContract.UsedFoodEntry.COLUMN_FOOD_ID);

                usedFoodQueryBuilder.appendWhere(
                        FoodContract.UsedFoodEntry.COLUMN_FOOD_TYPE
                                + "=" + Food.FOOD_PRODUCT);

                String productsUsedFoodQuery = usedFoodQueryBuilder.buildQuery(projection,
                        selection, null, null, null, null);

                String queryString = usedFoodQueryBuilder.buildUnionQuery(
                        new String[]{dishesUsedFoodQuery, productsUsedFoodQuery},
                        sortOrder, null);

                // Since union consists of 2 statements, we have to double selection args
                ArrayList<String> argsToDouble = new ArrayList<>();

                if (selection != null) {
                    argsToDouble.addAll(Arrays.asList(selectionArgs));
                    argsToDouble.addAll(Arrays.asList(selectionArgs));
                }

                String[] doubleSelectionArgs = argsToDouble.toArray(new String[argsToDouble.size()]);

                cursor = database.rawQuery(queryString, doubleSelectionArgs);

                break;

            case USED_FOOD_ID:
                selection = FoodContract.UsedFoodEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(
                        FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case DISHES:
                cursor = database.query(
                        FoodContract.DishesEntry.DISHES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case DISH_ID:
                selection = FoodContract.UsedFoodEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(
                        FoodContract.DishesEntry.DISHES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case PRODUCTS_FTS:
                //Returns all products which matches user's query
                // and all selection statements
                //
                // SELECT * FROM fts
                // INNER JOIN products ON
                // fts.rowid = products._id
                // WHERE selection
                cursor = database.rawQuery("SELECT * FROM "
                        + FoodContract.ProductsFTS.FTS_VIRTUAL_TABLE
                        + " INNER JOIN "
                        + FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME
                        + " ON " + FoodContract.ProductsFTS.FTS_VIRTUAL_TABLE + "."
                        + FoodContract.ProductsFTS.COLUMN_ID + "="
                        + FoodContract.ProductsEntry.FULL_ID + " "
                        + selection
                        + " ORDER BY "
                        + sortOrder, selectionArgs);
                break;

            case DISH_PRODUCTS:
                // SELECT * FROM dish_products INNER JOIN products
                // ON products._id = dish_products.product_id {selection}
                SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
                queryBuilder.setTables(FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME
                        + " INNER JOIN " + FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME
                        + " ON "
                        + FoodContract.ProductsEntry.FULL_ID
                        + " = " + FoodContract.DishProductsEntry.COLUMN_PRODUCT_ID);
                cursor = queryBuilder
                        .query(database, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case DISH_PRODUCT_ID:
                selection = FoodContract.DishProductsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(
                        FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case DISHES_WITH_PRODUCTS:
                // SELECT * FROM dishes INNER JOIN dish_products ON dish_products.dish_id = dishes._id
                // INNER JOIN products ON products._id = dish_products.product_id
                // ORDER BY dish_id
                SQLiteQueryBuilder dishQueryBuilder = new SQLiteQueryBuilder();
                dishQueryBuilder.setTables(FoodContract.DishesEntry.DISHES_TABLE_NAME
                        + " INNER JOIN " + FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME
                        + " ON " + FoodContract.DishProductsEntry.COLUMN_DISH_ID
                        + " = " + FoodContract.DishesEntry.FULL_ID
                        + " INNER JOIN " + FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME
                        + " ON "
                        + FoodContract.ProductsEntry.FULL_ID
                        + " = " + FoodContract.DishProductsEntry.COLUMN_PRODUCT_ID);
                sortOrder = FoodContract.DishProductsEntry.COLUMN_DISH_ID;
                cursor = dishQueryBuilder
                        .query(database, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor. If the data at this URI changes, the Cursor needs
        // to be updated
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return cursor
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return FoodContract.ProductsEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return FoodContract.ProductsEntry.CONTENT_ITEM_TYPE;
            case USED_FOODS:
                return FoodContract.UsedFoodEntry.CONTENT_LIST_TYPE;
            case USED_FOOD_ID:
                return FoodContract.UsedFoodEntry.CONTENT_ITEM_TYPE;
            case DISHES:
                return FoodContract.DishesEntry.CONTENT_LIST_TYPE;
            case DISH_ID:
                return FoodContract.DishesEntry.CONTENT_ITEM_TYPE;
            case DISH_PRODUCTS:
                return FoodContract.DishProductsEntry.CONTENT_LIST_TYPE;
            case DISH_PRODUCT_ID:
                return FoodContract.DishProductsEntry.CONTENT_ITEM_TYPE;
            case DISHES_WITH_PRODUCTS:
                return FoodContract.DishesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase database = mFoodDbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        long insertedRowId;
        Uri returnUri;
        switch (match) {
            case PRODUCTS:

                // Insert new row in database
                insertedRowId = database.insert(FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME, null,
                        values);

                // Check if new row was inserted
                if (insertedRowId > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    returnUri = FoodContract.ProductsEntry.buildProductsUri(insertedRowId);
                } else {
                    throw new android.database.SQLException("Failed to add new row into " + uri);
                }
                break;

            case USED_FOODS:

                // Insert new row in database
                insertedRowId = database.insert(FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME, null,
                        values);

                // Check if new row was inserted
                if (insertedRowId > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    returnUri = FoodContract.UsedFoodEntry.buildUsedFoodUri(insertedRowId);
                } else {
                    throw new android.database.SQLException("Failed to add new row into " + uri);
                }
                break;

            case DISHES:

                // Insert new row in database
                insertedRowId = database.insert(FoodContract.DishesEntry.DISHES_TABLE_NAME, null,
                        values);

                // Check if new row was inserted
                if (insertedRowId > 0) {
                    getContext().getContentResolver().notifyChange(FoodContract.DishesEntry.CONTENT_URI_WITH_PRODUCTS, null);
                    returnUri = FoodContract.DishesEntry.buildDishesUri(insertedRowId);
                } else {
                    throw new android.database.SQLException("Failed to add new row into " + uri);
                }
                break;

            case DISH_PRODUCTS:

                // Insert new row in database
                insertedRowId = database.insert(
                        FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME, null,
                        values);

                // Check if new row was inserted
                if (insertedRowId > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    returnUri = FoodContract.DishProductsEntry.buildDishProductUri(insertedRowId);
                } else {
                    throw new android.database.SQLException("Failed to add new row into " + uri);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mFoodDbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        int deletedRows;
        switch (match) {
            case PRODUCTS:
                deletedRows = database.delete(FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME,
                        selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = FoodContract.ProductsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                deletedRows = database.delete(FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME, selection,
                        selectionArgs);
                break;
            case USED_FOODS:
                deletedRows = database.delete(FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME,
                        selection, selectionArgs);
                break;
            case USED_FOOD_ID:
                selection = FoodContract.UsedFoodEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                deletedRows = database.delete(FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME, selection,
                        selectionArgs);
                break;
            case DISHES:
                deletedRows = database.delete(FoodContract.DishesEntry.DISHES_TABLE_NAME,
                        selection, selectionArgs);
                break;

            case DISH_ID:
                selection = FoodContract.DishesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                deletedRows = database.delete(FoodContract.DishesEntry.DISHES_TABLE_NAME, selection,
                        selectionArgs);
                getContext().getContentResolver()
                        .notifyChange(FoodContract.DishesEntry.CONTENT_URI_WITH_PRODUCTS, null);
                break;

            case DISH_PRODUCTS:
                deletedRows = database.delete(
                        FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME,
                        selection, selectionArgs);
                getContext().getContentResolver()
                        .notifyChange(FoodContract.DishesEntry.CONTENT_URI_WITH_PRODUCTS, null);
                break;

            case DISH_PRODUCT_ID:
                selection = FoodContract.DishProductsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                deletedRows = database.delete(
                        FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME, selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (deletedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = mFoodDbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case PRODUCTS:
                rowsUpdated = db.update(FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;

            case PRODUCT_ID:
                selection = FoodContract.ProductsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;

            case USED_FOODS:
                rowsUpdated = db.update(FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;

            case USED_FOOD_ID:
                selection = FoodContract.UsedFoodEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;

            case DISHES:
                rowsUpdated = db.update(FoodContract.DishesEntry.DISHES_TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;

            case DISH_ID:
                selection = FoodContract.UsedFoodEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(FoodContract.DishesEntry.DISHES_TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;

            case DISH_PRODUCTS:
                rowsUpdated = db.update(FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;

            case DISH_PRODUCT_ID:
                selection = FoodContract.DishProductsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase database = mFoodDbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                database.beginTransaction();
                int insertedRows = 0;
                try {
                    for (ContentValues contentValues : values) {
                        long insertedRow = database.insert(FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME,
                                null, contentValues);
                        if (insertedRow <= 0) {
                            throw new SQLException("Failed to insert row into " + uri.toString());
                        }
                        insertedRows++;
                    }
                    database.setTransactionSuccessful();
                } finally {
                    getContext().getContentResolver().notifyChange(uri, null);
                    database.endTransaction();
                }
                return insertedRows;
            case DISH_PRODUCTS:
                database.beginTransaction();
                int insertedProducts = 0;
                try {
                    for (ContentValues contentValues : values) {
                        long insertedRow = database.insert(
                                FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME,
                                null,
                                contentValues
                        );

                        if (insertedRow <= 0) {
                            throw new SQLException("Failed to insert row into " + uri.toString());
                        }
                        insertedProducts++;
                    }
                    database.setTransactionSuccessful();
                } finally {
                    getContext().getContentResolver()
                            .notifyChange(FoodContract.DishesEntry.CONTENT_URI_WITH_PRODUCTS, null);
                    database.endTransaction();
                }
        }
        return 0;
    }

}

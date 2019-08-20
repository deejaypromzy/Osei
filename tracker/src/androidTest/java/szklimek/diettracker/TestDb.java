package szklimek.diettracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import szklimek.diettracker.data.local.database.FoodContract;
import szklimek.diettracker.data.local.database.FoodDbHelper;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Test of FoodDbHelper class - database creation
 */
@RunWith(AndroidJUnit4.class)
public class TestDb {

    Context appContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("szklimek.diettracker", appContext.getPackageName());
    }

    @Before
    public void setUp() {
        deleteTheDatabase();
    }

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        // Context of the app under test.
        appContext.deleteDatabase(FoodDbHelper.DATABASE_NAME);
    }

    @Test
    public void testCreateDb() {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        appContext = InstrumentationRegistry.getTargetContext();
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME);
        tableNameHashSet.add(FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME);
        tableNameHashSet.add(FoodContract.DishesEntry.DISHES_TABLE_NAME);
        tableNameHashSet.add(FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME);

        appContext.deleteDatabase(FoodDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new FoodDbHelper(
                this.appContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // verify that any tables have been created
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that all tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        assertTrue("Error: Database was created not completely. Some of the tables was not created.",
                tableNameHashSet.isEmpty());


        c.close();
        db.close();
    }

    @Test
    public void testProductsColumns(){

        SQLiteDatabase db = new FoodDbHelper(
                this.appContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // verify that all columns in products table are created correctly
        Cursor c = db.rawQuery("PRAGMA table_info(" + FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME + ")",
                null);
        assertTrue("Error: Unable to query the database for table information.",
                c.moveToFirst());

        // HashSet of all columns in products table
        final HashSet<String> columnHashSet = new HashSet<>();
        columnHashSet.add(FoodContract.ProductsEntry._ID);
        columnHashSet.add(FoodContract.ProductsEntry.COLUMN_NAME);
        columnHashSet.add(FoodContract.ProductsEntry.COLUMN_PRODUCT_TYPE);
        columnHashSet.add(FoodContract.ProductsEntry.COLUMN_ENERGY);
        columnHashSet.add(FoodContract.ProductsEntry.COLUMN_PROTEIN);
        columnHashSet.add(FoodContract.ProductsEntry.COLUMN_FAT);
        columnHashSet.add(FoodContract.ProductsEntry.COLUMN_CARBOHYDRATES);
        columnHashSet.add(FoodContract.ProductsEntry.COLUMN_SUGARS);
        columnHashSet.add(FoodContract.ProductsEntry.COLUMN_WATER);
        columnHashSet.add(FoodContract.ProductsEntry.COLUMN_FIBER);
        columnHashSet.add(FoodContract.ProductsEntry.COLUMN_CA);
        columnHashSet.add(FoodContract.ProductsEntry.COLUMN_FE);
        columnHashSet.add(FoodContract.ProductsEntry.COLUMN_MG);
        columnHashSet.add(FoodContract.ProductsEntry.COLUMN_P);
        columnHashSet.add(FoodContract.ProductsEntry.COLUMN_NA);
        columnHashSet.add(FoodContract.ProductsEntry.COLUMN_K);
        columnHashSet.add(FoodContract.ProductsEntry.COLUMN_ZN);


        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            columnHashSet.remove(columnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required products entry columns",
                columnHashSet.isEmpty());

        c.close();
        db.close();
    }

    @Test
    public void testUsedFoodColumns(){

        SQLiteDatabase db = new FoodDbHelper(
                this.appContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // verify that all columns in used food table are created correctly
        Cursor c = db.rawQuery("PRAGMA table_info(" + FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME + ")",
                null);
        assertTrue("Error: Unable to query the database for table information.",
                c.moveToFirst());

        // HashSet of all columns in used food table
        final HashSet<String> columnHashSet = new HashSet<>();
        columnHashSet.add(FoodContract.UsedFoodEntry._ID);
        columnHashSet.add(FoodContract.UsedFoodEntry.COLUMN_FOOD_ID);
        columnHashSet.add(FoodContract.UsedFoodEntry.COLUMN_FOOD_TYPE);
        columnHashSet.add(FoodContract.UsedFoodEntry.COLUMN_DATE);
        columnHashSet.add(FoodContract.UsedFoodEntry.COLUMN_WEIGHT);


        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            columnHashSet.remove(columnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required used food entry columns",
                columnHashSet.isEmpty());

        c.close();
        db.close();
    }

    @Test
    public void testDishesColumns(){

        SQLiteDatabase db = new FoodDbHelper(
                this.appContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // verify that all columns in dishes table are created correctly
        Cursor c = db.rawQuery("PRAGMA table_info(" + FoodContract.DishesEntry.DISHES_TABLE_NAME + ")",
                null);
        assertTrue("Error: Unable to query the database for table information.",
                c.moveToFirst());

        // HashSet of all columns in dishes table
        final HashSet<String> columnHashSet = new HashSet<>();
        columnHashSet.add(FoodContract.DishesEntry._ID);
        columnHashSet.add(FoodContract.DishesEntry.COLUMN_NAME);
        columnHashSet.add(FoodContract.DishesEntry.COLUMN_DISH_TYPE);
        columnHashSet.add(FoodContract.DishesEntry.COLUMN_DATE);


        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            columnHashSet.remove(columnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                columnHashSet.isEmpty());

        c.close();
        db.close();
    }

    @Test
    public void testDishProductsColumns(){

        SQLiteDatabase db = new FoodDbHelper(
                this.appContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // verify that all columns in dish products table are created correctly
        Cursor c = db.rawQuery("PRAGMA table_info("
                        + FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME + ")",
                null);
        assertTrue("Error: Unable to query the database for table information.",
                c.moveToFirst());

        // HashSet of all columns in dishes table
        final HashSet<String> columnHashSet = new HashSet<>();
        columnHashSet.add(FoodContract.DishProductsEntry._ID);
        columnHashSet.add(FoodContract.DishProductsEntry.COLUMN_DISH_ID);
        columnHashSet.add(FoodContract.DishProductsEntry.COLUMN_PRODUCT_ID);
        columnHashSet.add(FoodContract.DishProductsEntry.COLUMN_WEIGHT);


        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            columnHashSet.remove(columnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                columnHashSet.isEmpty());

        c.close();
        db.close();
    }

    @Test
    public void insert() {
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        appContext = InstrumentationRegistry.getTargetContext();
        FoodDbHelper dbHelper = new FoodDbHelper(appContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME, null, null);
        db.delete(FoodContract.DishesEntry.DISHES_TABLE_NAME, null, null);
        db.delete(FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME, null, null);
        db.delete(FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME, null, null);

        // Create test values
        ContentValues testValues = TestUtilities.createProductValues();

        // Insert ContentValues into database and get a row ID back
        long insertedRowId;
        insertedRowId = db.insert(FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME, null, testValues);

        // Verify a row back.
        assertTrue(insertedRowId != -1);

        //Query the database and receive a Cursor back
        Cursor cursor = db.query(
                FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );
        // Move the cursor to a valid database row and check to see if it is any records back
        // from the query
        assertTrue( "Error: No Records returned from products query", cursor.moveToFirst() );

        // Validate data in resulting Cursor with the original ContentValues
        TestUtilities.validateCurrentRecord("Error: Products Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from products query",
                cursor.moveToNext() );

        // Test used food table insert
        testValues = TestUtilities.createUsedFoodValues(insertedRowId);
        insertedRowId = db.insert(FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME, null, testValues);

        assertTrue(insertedRowId != -1);
        cursor = db.query(
                FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );
        assertTrue( "Error: No Records returned from used food query", cursor.moveToFirst() );
        TestUtilities.validateCurrentRecord("Error: Used Food Query Validation Failed",
                cursor, testValues);
        assertFalse( "Error: More than one record returned from used food query",
                cursor.moveToNext() );

        // Test dishes table insert
        testValues = TestUtilities.createDishValues();
        insertedRowId = db.insert(FoodContract.DishesEntry.DISHES_TABLE_NAME, null, testValues);

        assertTrue(insertedRowId != -1);
        cursor = db.query(
                FoodContract.DishesEntry.DISHES_TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );
        assertTrue( "Error: No Records returned from dishes query", cursor.moveToFirst() );
        TestUtilities.validateCurrentRecord("Error: Dishes Query Validation Failed",
                cursor, testValues);
        assertFalse( "Error: More than one record returned from dishes query",
                cursor.moveToNext() );

        // Test dish products table insert
        testValues = TestUtilities.createDishProductValues();
        insertedRowId = db.insert(FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME,
                null, testValues);

        assertTrue(insertedRowId != -1);
        cursor = db.query(
                FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );
        assertTrue( "Error: No Records returned from dish products query", cursor.moveToFirst() );
        TestUtilities.validateCurrentRecord("Error: Dish Products Query Validation Failed",
                cursor, testValues);
        assertFalse( "Error: More than one record returned from dish products query",
                cursor.moveToNext() );

        cursor.close();
        db.close();
    }

}

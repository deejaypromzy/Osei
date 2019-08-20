package szklimek.diettracker;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import szklimek.diettracker.data.local.database.FoodContract;
import szklimek.diettracker.data.local.database.FoodDbHelper;
import szklimek.diettracker.data.local.database.FoodProvider;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Set of tests of  DietTracker ContentProvider.
 * Tests are taken from Udacity Advanced Android Course: https://github.com/udacity/Advanced_Android_Development
 * and recreated for DietTracker App.
 */
@RunWith(AndroidJUnit4.class)
public class TestProvider extends ProviderTestCase2<FoodProvider> {

    public TestProvider() {
        super(FoodProvider.class, FoodContract.CONTENT_AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecordsFromProvider();
    }

    private Context appContext = InstrumentationRegistry.getTargetContext();

    @Before
    public void deleteAllRecordsFromProvider() {
        appContext.getContentResolver().delete(
                FoodContract.ProductsEntry.CONTENT_URI,
                null,
                null
        );
        appContext.getContentResolver().delete(
                FoodContract.UsedFoodEntry.CONTENT_URI,
                null,
                null
        );

        appContext.getContentResolver().delete(
                FoodContract.DishesEntry.CONTENT_URI,
                null,
                null
        );

        appContext.getContentResolver().delete(
                FoodContract.DishProductsEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = appContext.getContentResolver().query(
                FoodContract.ProductsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Products table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = appContext.getContentResolver().query(
                FoodContract.UsedFoodEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Used Foods table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = appContext.getContentResolver().query(
                FoodContract.DishesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Dishes table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = appContext.getContentResolver().query(
                FoodContract.DishProductsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Dish Products table during delete", 0, cursor.getCount());
        cursor.close();

    }

    /*
        This test checks to make sure that the content provider is registered correctly.
        Copied from Udacity Advanced Android Course: https://github.com/udacity/Advanced_Android_Development
     */
    @Test
    public void testProviderRegistry() {
        PackageManager pm = appContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // FoodProvider class.
        ComponentName componentName = new ComponentName(appContext.getPackageName(),
                FoodProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: FoodProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + FoodContract.CONTENT_AUTHORITY,
                    providerInfo.authority, FoodContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: FoodProvider not registered at " + appContext.getPackageName(),
                    false);
        }
    }

    @Test
    public void testGetType() {
        // content://szklimek.diettracker.provider/products
        String productsListType = appContext.getContentResolver().getType(FoodContract.ProductsEntry.CONTENT_URI);
        // vnd.android.cursor.dir/szklimek.diettracker.provider/products
        assertEquals("Error: the ProductsEntry CONTENT_URI should return ProductsEntry.CONTENT_LIST_TYPE",
                FoodContract.ProductsEntry.CONTENT_LIST_TYPE, productsListType);

        // content://szklimek.diettracker.provider/used_food
        String usedFoodListType = appContext.getContentResolver().getType(FoodContract.UsedFoodEntry.CONTENT_URI);
        // vnd.android.cursor.dir/szklimek.diettracker.provider/used_food
        assertEquals("Error: the UsedFoodEntry CONTENT_URI should return UsedFoodEntry.CONTENT_LIST_TYPE",
                FoodContract.UsedFoodEntry.CONTENT_LIST_TYPE, usedFoodListType);

        // content://szklimek.diettracker.provider/dishes
        String dishesListType = appContext.getContentResolver().getType(FoodContract.DishesEntry.CONTENT_URI);
        // vnd.android.cursor.dir/szklimek.diettracker.provider/dishes
        assertEquals("Error: the DishesEntry CONTENT_URI should return DishesEntry.CONTENT_LIST_TYPE",
                FoodContract.DishesEntry.CONTENT_LIST_TYPE, dishesListType);

        // content://szklimek.diettracker.provider/dish_products
        String dishProductsListType = appContext.getContentResolver().getType(FoodContract.DishProductsEntry.CONTENT_URI);
        // vnd.android.cursor.dir/szklimek.diettracker.provider/dish_products
        assertEquals("Error: the DishProductsEntry CONTENT_URI should return DishProductsEntry.CONTENT_LIST_TYPE",
                FoodContract.DishProductsEntry.CONTENT_LIST_TYPE, dishProductsListType);

        long id = 1;
        // content://szklimek.diettracker.provider/products/1
        String productsItemType = appContext.getContentResolver().getType(FoodContract.ProductsEntry.buildProductsUri(id));
        // vnd.android.cursor.dir/szklimek.diettracker.provider/products/1
        assertEquals("Error: the ProductsEntry CONTENT_URI with id should return ProductsEntry.CONTENT_ITEM_TYPE",
                FoodContract.ProductsEntry.CONTENT_ITEM_TYPE, productsItemType);

        // content://szklimek.diettracker.provider/used_food/1
        String usedFoodItemType = appContext.getContentResolver().getType(FoodContract.UsedFoodEntry.buildUsedFoodUri(id));
        // vnd.android.cursor.dir/szklimek.diettracker.provider/used_food/1
        assertEquals("Error: the UsedFoodEntry CONTENT_URI with id should return UsedFoodEntry.CONTENT_ITEM_TYPE",
                FoodContract.UsedFoodEntry.CONTENT_ITEM_TYPE, usedFoodItemType);

        // content://szklimek.diettracker.provider/dishes/1
        String dishesItemType = appContext.getContentResolver().getType(FoodContract.DishesEntry.buildDishesUri(id));
        // vnd.android.cursor.dir/szklimek.diettracker.provider/dishes/1
        assertEquals("Error: the DishesEntry CONTENT_URI with id should return DishesEntry.CONTENT_ITEM_TYPE",
                FoodContract.DishesEntry.CONTENT_ITEM_TYPE, dishesItemType);

        // content://szklimek.diettracker.provider/dish_products/1
        String dishProductsItemType = appContext.getContentResolver()
                .getType(FoodContract.DishProductsEntry.buildDishProductUri(id));
        // vnd.android.cursor.dir/szklimek.diettracker.provider/dish_products/1
        assertEquals("Error: the DishProductsEntry CONTENT_URI with id should return DishProductsEntry.CONTENT_ITEM_TYPE",
                FoodContract.DishProductsEntry.CONTENT_ITEM_TYPE, dishProductsItemType);
    }

    @Test
    public void testBasicProductsQuery() {
        // insert our test records into the database
        FoodDbHelper dbHelper = new FoodDbHelper(appContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createProductValues();
        long productRowId = db.insert(FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME, null,
                testValues);

        assertTrue("Unable to Insert ProductsEntry into the Database", productRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor productCursor = appContext.getContentResolver().query(
                FoodContract.ProductsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicProductsQuery", productCursor, testValues);
    }

    @Test
    public void testBasicUsedFoodQuery() {
        // insert our test records into the database
        FoodDbHelper dbHelper = new FoodDbHelper(appContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long idOfProductFromProductsTable = 1;
        ContentValues testValues = TestUtilities.createUsedFoodValues(idOfProductFromProductsTable);
        long usedFoodRowId = db.insert(FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME, null,
                testValues);

        assertTrue("Unable to Insert UsedFoodEntry into the Database", usedFoodRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor usedFoodCursor = appContext.getContentResolver().query(
                FoodContract.UsedFoodEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicUsedFoodQuery", usedFoodCursor, testValues);
    }

    @Test
    public void testBasicDishesQuery() {
        // insert our test records into the database
        FoodDbHelper dbHelper = new FoodDbHelper(appContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createDishValues();
        long dishRowId = db.insert(FoodContract.DishesEntry.DISHES_TABLE_NAME, null,
                testValues);

        assertTrue("Unable to Insert DishesEntry into the Database", dishRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor dishesCursor = appContext.getContentResolver().query(
                FoodContract.DishesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicDishesQuery", dishesCursor, testValues);
    }

    @Test
    public void testBasicDishProductsQuery() {
        // insert our test records into the database
        FoodDbHelper dbHelper = new FoodDbHelper(appContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // insert test product to products table
        ContentValues productTestValues = TestUtilities.createProductValues();
        long productRowId = db.insert(FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME, null,productTestValues);

        ContentValues testValues = TestUtilities.createDishProductValues();
        testValues.put(FoodContract.DishProductsEntry.COLUMN_PRODUCT_ID, productRowId);
        long dishProductRowId = db.insert(FoodContract.DishProductsEntry.DISH_PRODUCTS_TABLE_NAME, null,
                testValues);

        assertTrue("Unable to Insert DishProductsEntry into the Database", dishProductRowId != -1);
        Log.e("testBasicDishProduct", "inserted rowID " + dishProductRowId);

        db.close();

        // Test the basic content provider query
        Cursor dishProductCursor = appContext.getContentResolver().query(
                FoodContract.DishProductsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        Log.e("testBasicDishProduct", "count " + dishProductCursor.getCount());

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicDishProductQuery", dishProductCursor, testValues);
    }

    @Test
    public void testInsertQueryDeleteProduct() {

        // Create test values
        ContentValues testValues = TestUtilities.createProductValues();

        // Get test content observer
        TestUtilities.TestContentObserver contentObserver = TestUtilities.getTestContentObserver();
        appContext.getContentResolver().registerContentObserver(FoodContract.ProductsEntry.CONTENT_URI,
                true, contentObserver);

        // Insert into products test values
        Uri insertedRowUri = appContext.getContentResolver().insert(FoodContract.ProductsEntry.CONTENT_URI,
                testValues);

        long insertedRowId = ContentUris.parseId(insertedRowUri);

        // Check if row was inserted
        assertTrue(insertedRowId != -1);

        // Check if content observer notified change
        assertEquals("Content observer din not notified any change.",
                true, contentObserver.waitForNotificationOrFail());

        appContext.getContentResolver().unregisterContentObserver(contentObserver);

        // Test the basic content provider query
        Cursor productCursor = appContext.getContentResolver().query(
                FoodContract.ProductsEntry.buildProductsUri(insertedRowId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );


        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testInsertAndQueryProduct", productCursor, testValues);


        int deletedRows = appContext.getContentResolver()
                .delete(FoodContract.ProductsEntry.buildProductsUri(insertedRowId), null, null);

        // Check if row was deleted
        assertTrue(deletedRows == 1);

    }

    @Test
    public void testInsertQueryDeleteUsedFood() {

        // Create test values
        long idOfProductFromProductTable = 1;
        ContentValues testValues = TestUtilities.createUsedFoodValues(idOfProductFromProductTable);

        // Get test content observer
        TestUtilities.TestContentObserver contentObserver = TestUtilities.getTestContentObserver();
        appContext.getContentResolver().registerContentObserver(FoodContract.UsedFoodEntry.CONTENT_URI,
                true, contentObserver);

        // Insert into used food test values
        Uri insertedRowUri = appContext.getContentResolver().insert(FoodContract.UsedFoodEntry.CONTENT_URI,
                testValues);

        long insertedRowId = ContentUris.parseId(insertedRowUri);

        // Check if row was inserted
        assertTrue(insertedRowId != -1);

        // Check if content observer notified change
        assertEquals("Content observer din not notified any change.",
                true, contentObserver.waitForNotificationOrFail());

        appContext.getContentResolver().unregisterContentObserver(contentObserver);

        // Test the basic content provider query
        Cursor usedFoodCursor = appContext.getContentResolver().query(
                FoodContract.UsedFoodEntry.buildUsedFoodUri(insertedRowId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testInsertAndUsedQueryFood", usedFoodCursor, testValues);

        int deletedRows = appContext.getContentResolver()
                .delete(FoodContract.UsedFoodEntry.buildUsedFoodUri(insertedRowId), null, null);

        // Check if row was deleted
        assertTrue(deletedRows == 1);
    }

    @Test
    public void testInsertQueryDeleteDish() {

        // Create test values
        ContentValues testValues = TestUtilities.createDishValues();

        // Get test content observer
        TestUtilities.TestContentObserver contentObserver = TestUtilities.getTestContentObserver();
        appContext.getContentResolver().registerContentObserver(FoodContract.DishesEntry.CONTENT_URI,
                true, contentObserver);

        // Insert into products test values
        Uri insertedRowUri = appContext.getContentResolver().insert(FoodContract.DishesEntry.CONTENT_URI,
                testValues);

        long insertedRowId = ContentUris.parseId(insertedRowUri);

        // Check if row was inserted
        assertTrue(insertedRowId != -1);

        // Check if content observer notified change
        assertEquals("Content observer din not notified any change.",
                true, contentObserver.waitForNotificationOrFail());

        appContext.getContentResolver().unregisterContentObserver(contentObserver);

        // Test the basic content provider query
        Cursor dishCursor = appContext.getContentResolver().query(
                FoodContract.DishesEntry.buildDishesUri(insertedRowId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testInsertAndDishProduct", dishCursor, testValues);

        int deletedRows = appContext.getContentResolver()
                .delete(FoodContract.DishesEntry.buildDishesUri(insertedRowId), null, null);

        // Check if row was deleted
        assertTrue(deletedRows == 1);

    }

    @Test
    public void testInsertQueryDeleteDishProduct() {

        // Create test values
        ContentValues testValues = TestUtilities.createDishProductValues();

        // Get test content observer
        TestUtilities.TestContentObserver contentObserver = TestUtilities.getTestContentObserver();
        appContext.getContentResolver().registerContentObserver(FoodContract.DishProductsEntry.CONTENT_URI,
                true, contentObserver);

        // Insert into products test values
        Uri insertedRowUri = appContext.getContentResolver().insert(FoodContract.DishProductsEntry.CONTENT_URI,
                testValues);

        long insertedRowId = ContentUris.parseId(insertedRowUri);

        // Check if row was inserted
        assertTrue(insertedRowId != -1);

        // Check if content observer notified change
        assertEquals("Content observer din not notified any change.",
                true, contentObserver.waitForNotificationOrFail());

        appContext.getContentResolver().unregisterContentObserver(contentObserver);

        // Test the basic content provider query
        Cursor dishProuctCursor = appContext.getContentResolver().query(
                FoodContract.DishProductsEntry.buildDishProductUri(insertedRowId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testInsertAndDishProduct", dishProuctCursor, testValues);

        int deletedRows = appContext.getContentResolver()
                .delete(FoodContract.DishProductsEntry.buildDishProductUri(insertedRowId), null, null);

        // Check if row was deleted
        assertTrue(deletedRows == 1);

    }

    @Test
    public void testUpdateProduct() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createProductValues();

        Uri productUri = appContext.getContentResolver().
                insert(FoodContract.ProductsEntry.CONTENT_URI, values);
        long productRowId = ContentUris.parseId(productUri);

        // Verify we got a row back.
        assertTrue(productRowId != -1);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(FoodContract.ProductsEntry._ID, productRowId);
        updatedValues.put(FoodContract.ProductsEntry.COLUMN_NAME, "Acid");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor productsCursor = appContext.getContentResolver()
                .query(FoodContract.ProductsEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver contentObserver = TestUtilities.getTestContentObserver();
        productsCursor.registerContentObserver(contentObserver);

        int count = appContext.getContentResolver().update(
                FoodContract.ProductsEntry.CONTENT_URI, updatedValues, FoodContract.ProductsEntry._ID + "= ?",
                new String[]{Long.toString(productRowId)});
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        contentObserver.waitForNotificationOrFail();

        productsCursor.unregisterContentObserver(contentObserver);
        productsCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = appContext.getContentResolver().query(
                FoodContract.ProductsEntry.CONTENT_URI,
                null,   // projection
                FoodContract.ProductsEntry._ID + " = " + productRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateProduct.  Error validating product entry update.",
                cursor, updatedValues);

        cursor.close();
    }

    @Test
    public void testUpdateUsedProduct() {
        // Create a new map of values, where column names are the keys
        long idOfProductFromProductsTable = 1;
        ContentValues values = TestUtilities.createUsedFoodValues(idOfProductFromProductsTable);

        Uri usedFoodUri = appContext.getContentResolver().
                insert(FoodContract.UsedFoodEntry.CONTENT_URI, values);
        long usedFoodRowId = ContentUris.parseId(usedFoodUri);

        // Verify we got a row back.
        assertTrue(usedFoodRowId != -1);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(FoodContract.UsedFoodEntry._ID, usedFoodRowId);
        updatedValues.put(FoodContract.UsedFoodEntry.COLUMN_WEIGHT, 150);

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor usedFoodCursor = appContext.getContentResolver()
                .query(FoodContract.UsedFoodEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver contentObserver = TestUtilities.getTestContentObserver();
        usedFoodCursor.registerContentObserver(contentObserver);

        int count = appContext.getContentResolver().update(
                FoodContract.UsedFoodEntry.CONTENT_URI, updatedValues, FoodContract.UsedFoodEntry._ID + "= ?",
                new String[]{Long.toString(usedFoodRowId)});
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        contentObserver.waitForNotificationOrFail();

        usedFoodCursor.unregisterContentObserver(contentObserver);
        usedFoodCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = appContext.getContentResolver().query(
                FoodContract.UsedFoodEntry.CONTENT_URI,
                null,   // projection
                FoodContract.UsedFoodEntry._ID + " = " + usedFoodRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateUsedFood.  Error validating used food entry update.",
                cursor, updatedValues);

        cursor.close();
    }

    @Test
    public void testUpdateDish() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createDishValues();

        Uri productUri = appContext.getContentResolver().
                insert(FoodContract.DishesEntry.CONTENT_URI, values);
        long dishRowId = ContentUris.parseId(productUri);

        // Verify we got a row back.
        assertTrue(dishRowId != -1);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(FoodContract.DishesEntry._ID, dishRowId);
        updatedValues.put(FoodContract.DishesEntry.COLUMN_NAME, "Another name");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor dishCursor = appContext.getContentResolver()
                .query(FoodContract.DishesEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver contentObserver = TestUtilities.getTestContentObserver();
        dishCursor.registerContentObserver(contentObserver);

        int count = appContext.getContentResolver().update(
                FoodContract.DishesEntry.CONTENT_URI, updatedValues, FoodContract.DishesEntry._ID + "= ?",
                new String[]{Long.toString(dishRowId)});
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        contentObserver.waitForNotificationOrFail();

        dishCursor.unregisterContentObserver(contentObserver);
        dishCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = appContext.getContentResolver().query(
                FoodContract.DishesEntry.CONTENT_URI,
                null,   // projection
                FoodContract.DishesEntry._ID + " = " + dishRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateDish.  Error validating dish entry update.",
                cursor, updatedValues);

        cursor.close();
    }

    @Test
    public void testUpdateDishProduct() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createDishProductValues();

        Uri productUri = appContext.getContentResolver().
                insert(FoodContract.DishProductsEntry.CONTENT_URI, values);
        long dishProductRowId = ContentUris.parseId(productUri);

        // Verify we got a row back.
        assertTrue(dishProductRowId != -1);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(FoodContract.DishProductsEntry._ID, dishProductRowId);
        updatedValues.put(FoodContract.DishProductsEntry.COLUMN_WEIGHT, 13.8);

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor dishCursor = appContext.getContentResolver()
                .query(FoodContract.DishProductsEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver contentObserver = TestUtilities.getTestContentObserver();
        dishCursor.registerContentObserver(contentObserver);

        int count = appContext.getContentResolver().update(
                FoodContract.DishProductsEntry.CONTENT_URI,
                updatedValues,
                FoodContract.DishProductsEntry._ID + "= ?",
                new String[]{Long.toString(dishProductRowId)});
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        contentObserver.waitForNotificationOrFail();

        dishCursor.unregisterContentObserver(contentObserver);
        dishCursor.close();

        Cursor cursor = appContext.getContentResolver().query(
                FoodContract.DishProductsEntry.buildDishProductUri(dishProductRowId),
                null,   // projection
                null,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities
                .validateCursor("testUpdateDishProduct.  Error validating dish product entry update.",
                cursor, updatedValues);

        cursor.close();
    }

    @Test
    public void testBulkInsert() {
        ContentValues[] valuesToInsert = TestUtilities.createProductsBulkInsertValues();

        // Get test content observer
        TestUtilities.TestContentObserver contentObserver = TestUtilities.getTestContentObserver();
        appContext.getContentResolver().registerContentObserver(FoodContract.ProductsEntry.CONTENT_URI,
                true, contentObserver);

        int insertCount = appContext.getContentResolver().bulkInsert(FoodContract.ProductsEntry.CONTENT_URI,
                valuesToInsert);

        // Check if content observer notified change
        assertEquals("Content observer din not notified any change.",
                true, contentObserver.waitForNotificationOrFail());

        appContext.getContentResolver().unregisterContentObserver(contentObserver);

        // Test the basic content provider query
        Cursor productCursor = appContext.getContentResolver().query(
                FoodContract.ProductsEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        assertEquals(insertCount, valuesToInsert.length);

        assertEquals(productCursor.getCount(), valuesToInsert.length);

        productCursor.close();
    }

}


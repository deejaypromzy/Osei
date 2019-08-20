package szklimek.diettracker;

import android.content.UriMatcher;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import szklimek.diettracker.data.local.database.FoodContract;
import szklimek.diettracker.data.local.database.FoodProvider;

import static junit.framework.Assert.assertEquals;

/**
 * Test to check if UriMatcher returns correct values dependent on passed Uri.
 */
@RunWith(AndroidJUnit4.class)
public class TestUriMatcher {

    private static final long ITEM_ID = 1;


    private static final Uri TEST_PRODUCTS_DIR = FoodContract.ProductsEntry.CONTENT_URI;
    private static final Uri TEST_USED_FOOD_DIR = FoodContract.UsedFoodEntry.CONTENT_URI;
    private static final Uri TEST_DISHES_DIR = FoodContract.DishesEntry.CONTENT_URI;
    private static final Uri TEST_DISH_PRODUCTS_DIR = FoodContract.DishProductsEntry.CONTENT_URI;

    private static final Uri TEST_PRODUCT_WITH_ID = FoodContract.ProductsEntry.buildProductsUri(ITEM_ID);
    private static final Uri TEST_USED_FOOD_WITH_ID = FoodContract.UsedFoodEntry.buildUsedFoodUri(ITEM_ID);
    private static final Uri TEST_DISH_WITH_ID = FoodContract.DishesEntry.buildDishesUri(ITEM_ID);
    private static final Uri TEST_DISH_PRODUCT_WITH_ID = FoodContract.DishProductsEntry.buildDishProductUri(ITEM_ID);

    private static final Uri TEST_PRODUCTS_FTS = FoodContract.ProductsFTS.buildUriWithQueryString("query");

    @Test
    public void testUriMatcher() {

        UriMatcher testMatcher = FoodProvider.buildUriMatcher();

        // List mType content uris
        assertEquals("Error: The PRODUCTS URI was matched incorrectly.",
                testMatcher.match(TEST_PRODUCTS_DIR), FoodProvider.PRODUCTS);
        assertEquals("Error: The USED FOOD URI was matched incorrectly.",
                testMatcher.match(TEST_USED_FOOD_DIR), FoodProvider.USED_FOODS);
        assertEquals("Error: The DISHES URI was matched incorrectly.",
                testMatcher.match(TEST_DISHES_DIR), FoodProvider.DISHES);
        assertEquals("Error: The DISH PRODUCTS URI was matched incorrectly.",
                testMatcher.match(TEST_DISH_PRODUCTS_DIR), FoodProvider.DISH_PRODUCTS);

        // Item mType content uris
        assertEquals("Error: The PRODUCT WITH ID URI was matched incorrectly.",
                testMatcher.match(TEST_PRODUCT_WITH_ID), FoodProvider.PRODUCT_ID);
        assertEquals("Error: The USED FOOD WITH ID URI was matched incorrectly.",
                testMatcher.match(TEST_USED_FOOD_WITH_ID), FoodProvider.USED_FOOD_ID);
        assertEquals("Error: The DISH WITH ID URI was matched incorrectly.",
                testMatcher.match(TEST_DISH_WITH_ID), FoodProvider.DISH_ID);
        assertEquals("Error: The DISH PRODUCT WITH ID URI was matched incorrectly.",
                testMatcher.match(TEST_DISH_PRODUCT_WITH_ID), FoodProvider.DISH_PRODUCT_ID);

        assertEquals("Error: The PRODUCTS_FTS URI was matched incorrectly.",
                testMatcher.match(TEST_PRODUCTS_FTS), FoodProvider.PRODUCTS_FTS);
    }
}

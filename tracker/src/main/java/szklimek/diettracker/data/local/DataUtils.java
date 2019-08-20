package szklimek.diettracker.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import szklimek.diettracker.R;
import szklimek.diettracker.data.local.database.FoodContract;
import szklimek.diettracker.data.local.database.FoodDbHelper;
import szklimek.diettracker.data.model.Product;

/**
 * Class contains methods used with database
 */

public class DataUtils {

    /**
     * Method which delete all products in products database and add products from file
     * (res/raw/products)
     */
    public static void restoreProductDatabase(final Context context){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                FoodDbHelper dbHelper = new FoodDbHelper(context);
                // Convert ArrayList object to Object[] array
                ContentValues[] contentValues =
                        getProductsFromFile(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete(FoodContract.ProductsFTS.FTS_VIRTUAL_TABLE, null, null);
                db.delete(FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME, null, null);
                loadProductsTable(db, contentValues);
                context.getContentResolver().notifyChange(FoodContract.ProductsEntry.CONTENT_URI, null);
                context.getContentResolver().notifyChange(FoodContract.ProductsFTS.CONTENT_URI, null);
            }
        });
        thread.start();
    }

    /**
     * Method which get products from products text file (in raw directory) and returns array of
     * ContentValues
     */
    public static ContentValues[] getProductsFromFile(Context context){
        final String separator = ":";
        InputStream inputStream = context.getResources().openRawResource(R.raw.products);

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            ArrayList<ContentValues> contentValuesArrayList = new ArrayList<>();
            while((line = bufferedReader.readLine()) != null){
                String[] parts = line.split(separator);
                ContentValues values = new ContentValues();
                values.put(FoodContract.ProductsEntry.COLUMN_NAME, parts[0]);
                values.put(FoodContract.ProductsEntry.COLUMN_PRODUCT_TYPE, Product.PRODUCT_1);
                values.put(FoodContract.ProductsEntry.COLUMN_NDBNO, 12345);
                values.put(FoodContract.ProductsEntry.COLUMN_FIBER, parts[1]);
                values.put(FoodContract.ProductsEntry.COLUMN_WATER, parts[2]);
                values.put(FoodContract.ProductsEntry.COLUMN_CA, parts[4]);
                values.put(FoodContract.ProductsEntry.COLUMN_PROTEIN, parts[5]);
                values.put(FoodContract.ProductsEntry.COLUMN_SUGARS, parts[6]);
                values.put(FoodContract.ProductsEntry.COLUMN_FAT, parts[7]);
                values.put(FoodContract.ProductsEntry.COLUMN_FE, parts[8]);
                values.put(FoodContract.ProductsEntry.COLUMN_CARBOHYDRATES, parts[9]);
                values.put(FoodContract.ProductsEntry.COLUMN_MG, parts[10]);
                values.put(FoodContract.ProductsEntry.COLUMN_P, parts[11]);
                values.put(FoodContract.ProductsEntry.COLUMN_K, parts[12]);
                values.put(FoodContract.ProductsEntry.COLUMN_ENERGY, parts[13]);
                values.put(FoodContract.ProductsEntry.COLUMN_NA, parts[14]);
                values.put(FoodContract.ProductsEntry.COLUMN_ZN, parts[15]);

                contentValuesArrayList.add(values);
            }
            // Convert ArrayList object to Object[] array
            ContentValues[] contentValues =
                    contentValuesArrayList.toArray(new ContentValues[contentValuesArrayList.size()]);

            return contentValues;

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  Method which insert products into products table
     */
    public static void loadProductsTable(SQLiteDatabase db, ContentValues[] valuesArray){
        db.beginTransaction();
        try {
            for (ContentValues  values: valuesArray) {
                db.insert(FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

}

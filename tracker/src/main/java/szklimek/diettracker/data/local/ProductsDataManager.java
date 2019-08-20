package szklimek.diettracker.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import szklimek.diettracker.R;
import szklimek.diettracker.data.local.database.FoodContract;
import szklimek.diettracker.data.local.database.FoodDbHelper;

/**
 * Implementation of ProductsDataSource
 */

public class ProductsDataManager implements ProductsDataSource {


    private static ProductsDataManager INSTANCE = null;

    private WeakReference<Context> mContext;

    // Singleton
    private ProductsDataManager(@NonNull Context context) {
        mContext = new WeakReference<>(context);
    }

    public static ProductsDataManager getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ProductsDataManager(context);
        }
        return INSTANCE;
    }

    @Override
    public void insertProductsDataToDatabase() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                FoodDbHelper dbHelper = new FoodDbHelper(mContext.get());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues[] valuesArray = getProductsFromFile();
                db.beginTransaction();
                try {
                    if(valuesArray != null){
                        for (ContentValues values : valuesArray) {
                            db.insert(FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME, null, values);
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }
        });
        thread.start();
    }

    @Override
    public void restoreProductsData() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                FoodDbHelper dbHelper = new FoodDbHelper(mContext.get());

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                db.delete(FoodContract.ProductsFTS.FTS_VIRTUAL_TABLE, null, null);
                db.delete(FoodContract.ProductsEntry.PRODUCTS_TABLE_NAME, null, null);

                insertProductsDataToDatabase();

                mContext.get().getContentResolver().notifyChange(FoodContract.ProductsEntry.CONTENT_URI, null);
                mContext.get().getContentResolver().notifyChange(FoodContract.ProductsFTS.CONTENT_URI, null);
            }
        });
        thread.start();
    }

    /**
     * Method which get products from products text file (in raw directory) and returns array of
     * ContentValues
     */
    private ContentValues[] getProductsFromFile() {
        final String separator = ":";
        InputStream inputStream = mContext.get().getResources().openRawResource(R.raw.products);

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            ArrayList<ContentValues> contentValuesArrayList = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(separator);
                ContentValues values = new ContentValues();
                values.put(FoodContract.ProductsEntry.COLUMN_NAME, parts[0]);
                values.put(FoodContract.ProductsEntry.COLUMN_PRODUCT_TYPE, parts[1]);
                values.put(FoodContract.ProductsEntry.COLUMN_NDBNO, parts[2]);
                values.put(FoodContract.ProductsEntry.COLUMN_MEASURE_NAME, parts[3]);
                values.put(FoodContract.ProductsEntry.COLUMN_MEASURE_VALUE, parts[4]);
                values.put(FoodContract.ProductsEntry.COLUMN_FIBER, parts[5]);
                values.put(FoodContract.ProductsEntry.COLUMN_WATER, parts[6]);
                values.put(FoodContract.ProductsEntry.COLUMN_CA, parts[7]);
                values.put(FoodContract.ProductsEntry.COLUMN_PROTEIN, parts[8]);
                values.put(FoodContract.ProductsEntry.COLUMN_SUGARS, parts[9]);
                values.put(FoodContract.ProductsEntry.COLUMN_FAT, parts[10]);
                values.put(FoodContract.ProductsEntry.COLUMN_FE, parts[11]);
                values.put(FoodContract.ProductsEntry.COLUMN_CARBOHYDRATES, parts[12]);
                values.put(FoodContract.ProductsEntry.COLUMN_MG, parts[13]);
                values.put(FoodContract.ProductsEntry.COLUMN_P, parts[14]);
                values.put(FoodContract.ProductsEntry.COLUMN_K, parts[15]);
                values.put(FoodContract.ProductsEntry.COLUMN_ENERGY, parts[16]);
                values.put(FoodContract.ProductsEntry.COLUMN_NA, parts[17]);
                values.put(FoodContract.ProductsEntry.COLUMN_ZN, parts[18]);
                contentValuesArrayList.add(values);
            }
            // Convert ArrayList object to Object[] array and return
            return contentValuesArrayList.toArray(new ContentValues[contentValuesArrayList.size()]);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

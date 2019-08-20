package szklimek.diettracker.data.model;

import android.support.annotation.IntDef;
import android.util.SparseArray;
import android.util.SparseIntArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import szklimek.diettracker.data.local.database.FoodContract;

/**
 * Class representing nutrient
 */

public class Nutrient {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ENERGY, WATER, PROTEIN, CARBOHYDRATE,
            FAT, FIBER, CALCIUM, MAGNESIUM,
            PHOSPHORUS, POTASSIUM, ZINC, SODIUM, IRON})
    public @interface NutrientType {
    }

    public static final int ENERGY = 0;
    public static final int WATER = 1;
    public static final int PROTEIN = 2;
    public static final int CARBOHYDRATE = 3;
    public static final int FAT = 4;
    public static final int FIBER = 5;
    public static final int CALCIUM = 6;
    public static final int MAGNESIUM = 7;
    public static final int PHOSPHORUS = 8;
    public static final int POTASSIUM = 9;
    public static final int ZINC = 10;
    public static final int SODIUM = 11;
    public static final int IRON = 12;

    public static final int[] NUTRIENTS_IDS = new int[]{
            ENERGY, PROTEIN, CARBOHYDRATE, FAT, WATER, FIBER,
            CALCIUM, MAGNESIUM, PHOSPHORUS, POTASSIUM, ZINC, SODIUM, IRON
    };

    public static final SparseArray<String> NUTRIENT_COLUMNS = new SparseArray<>(NUTRIENTS_IDS.length);

    static {

        NUTRIENT_COLUMNS.put(ENERGY, FoodContract.ProductsEntry.COLUMN_ENERGY);
        NUTRIENT_COLUMNS.put(WATER, FoodContract.ProductsEntry.COLUMN_WATER);
        NUTRIENT_COLUMNS.put(PROTEIN, FoodContract.ProductsEntry.COLUMN_PROTEIN);
        NUTRIENT_COLUMNS.put(CARBOHYDRATE, FoodContract.ProductsEntry.COLUMN_CARBOHYDRATES);
        NUTRIENT_COLUMNS.put(FAT, FoodContract.ProductsEntry.COLUMN_FAT);
        NUTRIENT_COLUMNS.put(FIBER, FoodContract.ProductsEntry.COLUMN_FIBER);
        NUTRIENT_COLUMNS.put(CALCIUM, FoodContract.ProductsEntry.COLUMN_CA);
        NUTRIENT_COLUMNS.put(MAGNESIUM, FoodContract.ProductsEntry.COLUMN_MG);
        NUTRIENT_COLUMNS.put(PHOSPHORUS, FoodContract.ProductsEntry.COLUMN_P);
        NUTRIENT_COLUMNS.put(POTASSIUM, FoodContract.ProductsEntry.COLUMN_K);
        NUTRIENT_COLUMNS.put(ZINC, FoodContract.ProductsEntry.COLUMN_ZN);
        NUTRIENT_COLUMNS.put(SODIUM, FoodContract.ProductsEntry.COLUMN_NA);
        NUTRIENT_COLUMNS.put(IRON, FoodContract.ProductsEntry.COLUMN_FE);

    }

    private String name;

    public Nutrient(String name) {
        this.name = name;
    }

    // Default constructor
    public Nutrient(){

    }

    public String getName() {
        return name;
    }


}
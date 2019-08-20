package szklimek.diettracker.data.model;

import android.support.annotation.IntDef;
import android.util.SparseArray;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Class representing product added to dish
 */

public class Product extends Food {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PRODUCT_1, PRODUCT_2, PRODUCT_3, PRODUCT_4, PRODUCT_5,
            PRODUCT_6,PRODUCT_7,PRODUCT_8, PRODUCT_9, PRODUCT_10,
            PRODUCT_11, PRODUCT_12, PRODUCT_13, PRODUCT_14, PRODUCT_15,
            PRODUCT_16, PRODUCT_17})
    public @interface ProductType {
    }

    // Possible product type values
    public static final int PRODUCT_ALL = 0;
    public static final int PRODUCT_1 = 1;
    public static final int PRODUCT_2 = 2;
    public static final int PRODUCT_3 = 3;
    public static final int PRODUCT_4 = 4;
    public static final int PRODUCT_5 = 5;
    public static final int PRODUCT_6 = 6;
    public static final int PRODUCT_7 = 7;
    public static final int PRODUCT_8 = 8;
    public static final int PRODUCT_9 = 9;
    public static final int PRODUCT_10 = 10;
    public static final int PRODUCT_11 = 11;
    public static final int PRODUCT_12 = 12;
    public static final int PRODUCT_13 = 13;
    public static final int PRODUCT_14 = 14;
    public static final int PRODUCT_15 = 15;
    public static final int PRODUCT_16 = 16;
    public static final int PRODUCT_17 = 17;

    private long ndbnoIndex; // Index of product in USDA database
    private int type; // Type of product
    private SparseArray<Double> nutrientsValues = new SparseArray<>(); // Nutrients values

    private Product(ProductBuilder builder) {
        setId(builder.mId);
        setWeight(builder.mWeight);
        setName(builder.mName);
        setDescription(builder.mDescription);
        ndbnoIndex = builder.mNdbnoIndex;
        type = builder.mType;
        nutrientsValues = builder.mNutrientsValues;
        setFoodType(FOOD_PRODUCT);
    }

    // Builder class to help object instantiation
    public static class ProductBuilder {
        private long mId;
        private long mNdbnoIndex;
        private int mType;
        private int mWeight;
        private String mName;
        private String mDescription;

        // Value of nutrients per 100g
        private SparseArray<Double> mNutrientsValues = new SparseArray<>();

        public ProductBuilder() {
        }

        public ProductBuilder id(long id) {
            this.mId = id;
            return this;
        }

        public ProductBuilder ndbnoIndex(long ndbnoIndex) {
            this.mNdbnoIndex = ndbnoIndex;
            return this;
        }

        public ProductBuilder type(int type) {
            this.mType = type;
            return this;
        }

        public ProductBuilder weight(int weight){
            this.mWeight = weight;
            return this;
        }

        public ProductBuilder name(String name){
            this.mName = name;
            return this;
        }

        public ProductBuilder description(String description){
            this.mDescription = description;
            return this;
        }

        public ProductBuilder nutrient(int nutrientId, double nutrientValue){
            this.mNutrientsValues.put(nutrientId, nutrientValue);
            return this;
        }

        public Product build() {
            return new Product(this);
        }

    }

    public long getNdbnoIndex() {
        return ndbnoIndex;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public double getNutrientPer100g(@Nutrient.NutrientType int nutrientId) {
        return nutrientsValues.get(nutrientId);
    }

    @Override
    public double getNutrientPerWeight(@Nutrient.NutrientType int nutrientId, int weight) {
        return ((weight * getNutrientPer100g(nutrientId))/100);
    }



}

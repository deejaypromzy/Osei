package szklimek.diettracker.browse;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import java.util.List;

import szklimek.diettracker.browse.detail.BrowseDetailContract;
import szklimek.diettracker.reusablefragments.products.ProductsContract;
import szklimek.diettracker.browse.menu.BrowseMenuContract;
import szklimek.diettracker.data.local.FoodDataSource;
import szklimek.diettracker.data.model.Nutrient;
import szklimek.diettracker.data.model.NutrientCategory;
import szklimek.diettracker.data.model.Product;

/**
 * Model class which controls data flow in BrowseActivity, get data from FoodRepository
 * and store cached data
 */

public class BrowseModel implements
        BrowseContract.Model,
        BrowseMenuContract.Model,
        ProductsContract.Model,
        BrowseDetailContract.Model {

    private static BrowseModel INSTANCE = null;

    private FoodDataSource mFoodDataSource;

    private SparseArray<NutrientCategory> mMenuNutrients = new SparseArray<>();

    private int mCategory;

    private String mQuery;

    private Product mClickedProduct;

    private BrowseModel(@NonNull FoodDataSource foodDataSource) {

        mFoodDataSource = foodDataSource;
        loadNutrientList();

    }

    static BrowseModel getInstance(@NonNull FoodDataSource foodDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new BrowseModel(foodDataSource);
        }
        return INSTANCE;
    }

    private void loadNutrientList() {
        mFoodDataSource.getNutrients(new FoodDataSource.GetNutrientsCallback() {
            @Override
            public void onNutrientsLoaded(SparseArray<Nutrient> nutrients) {

                // Convert Nutrient object to NutrientCategory by iterating of each entry in HashMap
                int nutrientsSize = nutrients.size();
                for (int index = 0; index < nutrientsSize; index++) {
                    mMenuNutrients.put(
                            index,
                            new NutrientCategory(nutrients.get(index)));
                }
            }
        });
    }

    @Override
    public void getNutrients(GetNutrientsCallback callback) {
        callback.onNutrientsLoaded(mMenuNutrients);
    }

    @Override
    public void setCategory(int category) {
        mCategory = category;
    }

    @Override
    public void setQuery(String query) {
        mQuery = query;
    }

    @Override
    public void setNutrientMinValue(int position, double value) {
        mMenuNutrients.get(position).setMinValue(value);
    }

    @Override
    public void setNutrientMinState(int position, boolean isChecked) {
        mMenuNutrients.get(position).setMinimumValueChecked(isChecked);
    }

    @Override
    public void setNutrientMaxValue(int position, double value) {
        mMenuNutrients.get(position).setMaxValue(value);
    }

    @Override
    public void setNutrientMaxState(int position, boolean isChecked) {
        mMenuNutrients.get(position).setMaximumValueChecked(isChecked);
    }

    @Override
    public void setNutrientState(int position, boolean isChecked) {
        mMenuNutrients.get(position).setChecked(isChecked);
    }

    @Override
    public void getProducts(final GetProductsCallback callback) {

        FoodDataSource.GetProductsCallback productsCallback = new FoodDataSource.GetProductsCallback() {
            @Override
            public void onProductsLoaded(List<Product> productsList) {
                callback.onProductsLoaded(productsList);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        };
        mFoodDataSource.getProducts(
                productsCallback,
                mQuery,
                mCategory,
                mMenuNutrients);
    }

    @Override
    public void getQuery(GetQueryCallback callback) {
        callback.onQueryLoaded(mQuery);
    }

    @Override
    public void setChosenProduct(Product product) {
        mClickedProduct = product;
    }

    @Override
    public void getChosenProduct(GetClickedProductCallback callback) {
        callback.onProductLoaded(mClickedProduct);
    }

}

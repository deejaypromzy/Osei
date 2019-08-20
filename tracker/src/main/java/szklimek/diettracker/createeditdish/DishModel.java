package szklimek.diettracker.createeditdish;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import szklimek.diettracker.reusablefragments.adddialog.AddFoodDialogContract;
import szklimek.diettracker.createeditdish.dishcreator.DishCreatorContract;
import szklimek.diettracker.data.local.SharedPreferencesSource;
import szklimek.diettracker.data.local.FoodDataSource;
import szklimek.diettracker.data.model.Dish;
import szklimek.diettracker.data.model.Product;
import szklimek.diettracker.reusablefragments.products.ProductsContract;

import static szklimek.diettracker.createeditdish.DishActivity.NO_DISH;

/**
 * Model class which controls data flow in DishActivity, get data from FoodRepository
 * and store cached data
 */

public class DishModel implements
        DishContract.Model,
        DishCreatorContract.Model,
        ProductsContract.Model,
        AddFoodDialogContract.Model {

    private static final int PRODUCT_NOT_FOUND = -1;

    private static DishModel INSTANCE = null;

    private FoodDataSource mFoodDataSource;

    private SharedPreferencesSource mSharedPreferencesManager;

    private OnDishProductsChangeListener onDishProductsChangeListener;

    private boolean isAfterConfigurationChanged;

    private boolean isRefreshNeeded;

    // Id of dish to edit
    private long mEditedDishId;

    // Mode in which activity was started (add or edit)
    private boolean isActivityInEditMode;

    // Cache of edited/created dish
    private Dish mEditedDishCache;

    // Product to add from products list
    private Product mNewProductCache;

    // Shown AddDialog mode
    private boolean isAddDialogInEditMode;

    // Max possible weight of product to set in add/edit dialog
    private int mMaxFoodWeight;

    // Cache of edited product
    private Product mDialogProductCache;

    // Position of edited product in dialog
    private int mDialogEditedProductPosition;

    // Clicked product weight
    private int mDialogProductWeight;

    // Query in SearchView in ProductsFragment which helps to choose product
    private String mQuery;

    private DishModel(@NonNull FoodDataSource foodDataSource,
                      @NonNull SharedPreferencesSource manager) {

        mFoodDataSource = foodDataSource;
        mSharedPreferencesManager = manager;

    }

    static DishModel getInstance(@NonNull FoodDataSource foodDataSource,
                                 @NonNull SharedPreferencesSource manager) {
        if (INSTANCE == null) {
            INSTANCE = new DishModel(foodDataSource, manager);
        }
        return INSTANCE;
    }

    @Override
    public void addDishProductsListener(OnDishProductsChangeListener listener) {
        onDishProductsChangeListener = listener;
    }

    @Override
    public void removeDishProductsListener() {
        onDishProductsChangeListener = null;
    }

    @Override
    public void notifyDishProductsChange() {
        onDishProductsChangeListener.onDishProductsChange();
    }

    @Override
    public void setConfigurationChanged(boolean isConfigurationChanged) {
        isAfterConfigurationChanged = isConfigurationChanged;
    }

    @Override
    public void getStartingDish(final GetEditedDishCallback callback) {
        // Load cache after configuration changes
        if (isAfterConfigurationChanged) {
            callback.onDishLoaded(mEditedDishCache);
            isAfterConfigurationChanged = false;
        } else {
            if (isRefreshNeeded) {
                if (isActivityInEditMode) {
                    FoodDataSource.GetDishCallback dishCallback = new FoodDataSource.GetDishCallback() {
                        @Override
                        public void onDishLoaded(Dish dish) {
                            mEditedDishCache = dish;
                            callback.onDishLoaded(mEditedDishCache);
                        }

                        @Override
                        public void onDataNotAvailable() {
                            Log.e("DishModel", "Data not available.");
                        }
                    };
                    mFoodDataSource.getDish(dishCallback, mEditedDishId);
                } else {
                    mEditedDishCache = new Dish();
                    callback.onDishLoaded(mEditedDishCache);
                }
                isRefreshNeeded = false;
            } else {
                callback.onDishLoaded(mEditedDishCache);
            }
        }
    }

    @Override
    public void getEditedDish(final GetEditedDishCallback callback) {
        callback.onDishLoaded(mEditedDishCache);
    }

    @Override
    public void setDishName(String name) {
        mEditedDishCache.setName(name);
    }

    @Override
    public void setDishType(int type) {
        mEditedDishCache.setType(type);
    }

    @Override
    public void setDishDescription(String description) {
        mEditedDishCache.setDescription(description);
    }

    @Override
    public void addDishProduct(Product product) {
        mEditedDishCache.addProduct(product);
    }

    @Override
    public void updateProduct(int position, Product product) {
        mEditedDishCache.updateProduct(position, product);
    }

    @Override
    public void removeProduct(int position) {
        mEditedDishCache.removeProduct(position);
    }

    @Override
    public void checkDishInput(CheckDishInputCallback callback) {
        if (mEditedDishCache.getName().isEmpty()){
            callback.onNameError();
            return;
        }

        if(mEditedDishCache.getDishProductsList().size() < 2){
            callback.onProductsError();
            return;
        }

        callback.onInputOk();
    }

    @Override
    public void saveDish(final SaveDishCallback callback) {
        if (isActivityInEditMode)
            mFoodDataSource.updateDish(new FoodDataSource.UpdateDishCallback() {
                @Override
                public void onDishUpdated() {
                    callback.onDishSaved(mEditedDishCache.getName());
                }
            }, mEditedDishCache);
        else mFoodDataSource.addDish(new FoodDataSource.AddDishCallback() {
            @Override
            public void onDishAdded() {
                callback.onDishSaved(mEditedDishCache.getName());
            }
        }, mEditedDishCache);
    }

    @Override
    public void setEditedDishId(long dishId) {
        if (dishId == NO_DISH) {
            isActivityInEditMode = false;
            isRefreshNeeded = true;
        } else {
            mEditedDishId = dishId;
            isActivityInEditMode = true;
            isRefreshNeeded = true;
        }
    }

    @Override
    public void getProducts(final GetProductsCallback callback) {
        FoodDataSource.GetProductsCallback productsCallback =
                new FoodDataSource.GetProductsCallback() {
                    @Override
                    public void onProductsLoaded(List<Product> productsList) {
                        callback.onProductsLoaded(productsList);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                };
        mFoodDataSource.getProducts(productsCallback, mQuery, 0, null);
    }

    @Override
    public void getQuery(GetQueryCallback callback) {
        callback.onQueryLoaded(mQuery);
    }

    @Override
    public void setQuery(String query) {
        mQuery = query;
    }

    @Override
    public void setChosenProduct(Product product) {
        mNewProductCache = product;
    }

    /*
    Implementation of methods from AddFoodDialog model
     */
    @Override
    public void setDialogProductCache(Product productCache) {
        mDialogProductCache = productCache;
        mDialogProductWeight = mDialogProductCache.getWeight();
    }

    @Override
    public void setEditProductPosition(int position) {
        mDialogEditedProductPosition = position;
    }

    @Override
    public void getDialogFoodCache(GetDialogFoodCallback callback) {
        getPreferenceMaxWeight();
        if (!isAddDialogInEditMode) {
            mDialogProductWeight = mMaxFoodWeight / 10;
        }
        int progress = (mDialogProductWeight * 100) / mMaxFoodWeight;
        callback.onFoodLoaded(mDialogProductCache, progress, isAddDialogInEditMode);
    }

    @Override
    public void getUpdatedWeightValuesPerProgress(GetUpdatedWeightValuesCallback callback, int progress) {
        getPreferenceMaxWeight();
        mDialogProductWeight = (progress * mMaxFoodWeight) / 100;
        callback.onValuesUpdatedCounted(mDialogProductCache, mDialogProductWeight);
    }

    @Override
    public void setDialogEditMode(boolean isInEditMode) {
        isAddDialogInEditMode = isInEditMode;
    }

    @Override
    public void saveDialogCacheFood(DialogSavedCallback callback) {
        mDialogProductCache.setWeight(mDialogProductWeight);
        if (isAddDialogInEditMode) updateProduct(mDialogEditedProductPosition, mDialogProductCache);
        else {
            int indexOfDuplicatedProduct = findDuplicatedProductIndex();

            // Update weight value when one product is added more times
            if (indexOfDuplicatedProduct == PRODUCT_NOT_FOUND) addDishProduct(mDialogProductCache);
            else {
                int currentProductWeight = mEditedDishCache.getDishProductsList().get(indexOfDuplicatedProduct).getWeight();
                mDialogProductCache.setWeight(currentProductWeight + mDialogProductWeight);
                updateProduct(indexOfDuplicatedProduct, mDialogProductCache);
            }
        }
        notifyDishProductsChange();
        callback.onDialogSaved();
    }

    @Override
    public void deleteDialogCacheFood() {
        removeProduct(mDialogEditedProductPosition);
        notifyDishProductsChange();
    }

    /**
     * Check if added product is currently in the dish
     *
     * @return index of product or -1 if product is not in list
     */
    private int findDuplicatedProductIndex() {
        int currentIndex = 0;
        for (Product product : mEditedDishCache.getDishProductsList()) {
            if (product.getId() == mDialogProductCache.getId()) {
                return currentIndex;
            }
            currentIndex++;
        }
        return PRODUCT_NOT_FOUND;
    }

    /**
     * Retrieve max food weight from preferences
     */
    private void getPreferenceMaxWeight() {
        if (mMaxFoodWeight == 0)
            mMaxFoodWeight = mSharedPreferencesManager.getMaxFoodWeight();
    }

}

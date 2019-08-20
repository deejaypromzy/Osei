package szklimek.diettracker.reusablefragments.products;

import android.support.annotation.NonNull;

import java.util.List;

import szklimek.diettracker.data.model.Product;

/**
 * Implementation of ProductsFragment presenter
 */

public class ProductsPresenter implements ProductsContract.Presenter {

    @NonNull
    private ProductsContract.Model mActivityModel;

    @NonNull
    private ProductsContract.View mFoodView;

    ProductsPresenter(@NonNull ProductsContract.Model model,
                      @NonNull ProductsContract.View view) {
        mFoodView = view;
        mActivityModel = model;

        mFoodView.setPresenter(this);
    }

    @Override
    public void start() {
        getQuery();
        getFood();
    }

    @Override
    public void getFood() {
        mActivityModel.getProducts(new ProductsContract.Model.GetProductsCallback() {
            @Override
            public void onProductsLoaded(List<Product> products) {
                mFoodView.updateProducts(products);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void getQuery() {
        mActivityModel.getQuery(new ProductsContract.Model.GetQueryCallback() {
            @Override
            public void onQueryLoaded(String query) {
                mFoodView.updateQuery(query);
            }
        });
    }

    @Override
    public void onQueryChange(String query) {
        mActivityModel.setQuery(query);
        getFood();
    }

    @Override
    public void onProductClicked(int position, Product product) {
        mActivityModel.setChosenProduct(product);
        mFoodView.sendClickedProduct(product);
    }
}

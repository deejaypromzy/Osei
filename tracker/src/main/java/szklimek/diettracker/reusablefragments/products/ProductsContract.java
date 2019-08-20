package szklimek.diettracker.reusablefragments.products;

import java.util.List;

import szklimek.diettracker.BasePresenter;
import szklimek.diettracker.BaseView;
import szklimek.diettracker.data.model.Product;

/**
 * Contract between ProductsFragment View, Presenter and Model
 */

public interface ProductsContract {

    interface View extends BaseView<Presenter> {

        void sendClickedProduct(Product product);

        void updateProducts(List<Product> products);

        void updateQuery(String query);

        interface ProductsClickedCallback {
            void sendClickedProduct(Product product);
        }

    }

    interface Presenter extends BasePresenter {

        void getFood();

        void getQuery();

        void onQueryChange(String query);

        void onProductClicked(int position, Product product);

    }

    interface Model {

        void getProducts(GetProductsCallback callback);

        interface GetProductsCallback {
            void onProductsLoaded(List<Product> products);

            void onDataNotAvailable();
        }

        void getQuery(GetQueryCallback callback);

        void setQuery(String query);

        interface GetQueryCallback {
            void onQueryLoaded(String query);
        }

        void setChosenProduct(Product product);

    }


}

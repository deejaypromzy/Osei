package szklimek.diettracker.browse.detail;

import szklimek.diettracker.BasePresenter;
import szklimek.diettracker.BaseView;
import szklimek.diettracker.data.model.Product;

/**
 * Contract between BrowseDetailFragment View, Presenter and Model
 */

public interface BrowseDetailContract {

    interface View extends BaseView<Presenter> {

        void updateView(Product product);

        void showNutrientsCounter();

        void showInternetReport();

    }

    interface Presenter extends BasePresenter {

        void getClickedProduct();

        void onMenuCounterClick();

        void onInternetReportClick();

    }

    interface Model {

        interface GetClickedProductCallback {

            void onProductLoaded(Product product);

        }

        void getChosenProduct(GetClickedProductCallback callback);

    }

}

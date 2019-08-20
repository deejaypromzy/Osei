package szklimek.diettracker.browse;

import szklimek.diettracker.BaseNavigationActivityPresenter;
import szklimek.diettracker.BaseNavigationActivityView;
import szklimek.diettracker.BaseView;

/**
 * Contract between BrowseActivity View, Presenter and Model
 */

public interface BrowseContract {

    interface View extends BaseNavigationActivityView<Presenter> {

        void showMenu();

        void showProducts();

        void showProductDetails();

    }

    interface Presenter extends BaseNavigationActivityPresenter {

        void onProductClicked();

    }

    interface Model {



    }
}

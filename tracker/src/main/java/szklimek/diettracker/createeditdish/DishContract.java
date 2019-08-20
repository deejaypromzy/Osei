package szklimek.diettracker.createeditdish;

import szklimek.diettracker.BaseNavigationActivityPresenter;
import szklimek.diettracker.BasePresenter;
import szklimek.diettracker.BaseView;
import szklimek.diettracker.data.model.Product;

/**
 * Contract between DishActivity View, Presenter and Model
 */

public interface DishContract {

    interface View extends BaseView<Presenter> {

        void showCreator();

        void showProducts();

        void hideProducts();

        void showAddDialog();

    }

    interface Presenter extends BasePresenter {

        void onConfigurationChanged();

        void onProductClicked(Product product);

        void onDialogSaveDismissed();

        void onIntentLoaded(long dishId);

    }

    interface Model {

        void setEditedDishId(long dishId);

        void setDialogProductCache(Product productCache);

        void setDialogEditMode(boolean isInEditMode);

        void setConfigurationChanged(boolean isConfigurationChanged);

    }
}
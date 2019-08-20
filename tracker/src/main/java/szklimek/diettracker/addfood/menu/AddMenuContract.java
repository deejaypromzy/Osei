package szklimek.diettracker.addfood.menu;

import java.util.List;

import szklimek.diettracker.BasePresenter;
import szklimek.diettracker.BaseView;
import szklimek.diettracker.data.model.Food;

/**
 * Contract class between AddMenuFragment's View and Presenter
 */
public interface AddMenuContract {
    interface View extends BaseView<AddMenuContract.Presenter> {

        void showDishes();

        void showProducts();

        void showAddDialog();

        void updateFoods(List<Food> foods);

    }

    interface Presenter extends BasePresenter {

        void onDishesClicked();

        void onProductsClicked();

        void onFoodClicked(Food food);

    }

    interface Model {

        void getRecentlyAddedFood(GetRecentlyAddedFoodCallback callback);

        interface GetRecentlyAddedFoodCallback {
            void onFoodLoaded(List<Food> foods);
        }

        void setDialogFoodCache(Food food);

        void setDialogMode(boolean isInEditMode);

    }

}

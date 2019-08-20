package szklimek.diettracker.reusablefragments.dishes;

import java.util.ArrayList;
import java.util.List;

import szklimek.diettracker.BasePresenter;
import szklimek.diettracker.BaseView;
import szklimek.diettracker.data.model.Dish;
import szklimek.diettracker.data.model.Food;

/**
 * Contract between DishesFragment View and Presenter
 */

public interface DishesContract {
    interface View extends BaseView<DishesPresenter> {

        void showDishList(List<Dish> dishesList, ArrayList<Boolean> dishesExpandedArray);

        void showEmptyView();

        void hideEmptyView();

        void showUndoSnackBar();

        void restoreRemovedDish(int position, Dish dish, boolean isViewExpanded);

        void showEditDish(Dish dish);

        void showNewDish();

        void showAddFoodDialog();
    }

    interface Presenter extends BasePresenter {

        void loadDishes();

        void saveDishesViewCache(ArrayList<Boolean> dishesExpandedArray);

        void restoreRemovedDish();

        void onResumeView();

        void onPause();

        void onSnackBarUndoClicked();

        void onEditDishClicked(Dish dish);

        void onNewDishClicked();

        void onAddDishClicked(Dish dish);

        void onDeleteDishClicked(int position, Dish dish, boolean isViewExpanded);
    }

    interface Model {

        void setDialogFoodCache(Food food);

        void setDialogMode(boolean isInEditMode);

        void getDishes(GetDishesCallback callback);

        void saveDishesViewCache(ArrayList<Boolean> dishesExpandedViewArray);

        void deleteHiddenDishes();

        void setDishHidden(Dish dish);

        void setDishShown(Dish dish);

        void saveRestoreDishCache(int position, Dish dish, boolean isViewExpanded);

        void getRestoreDishCache(GetRestoredDishCallback callback);

        interface GetDishesCallback {

            void onDishesLoaded(List<Dish> dishes, ArrayList<Boolean> dishesViewExpandedArray);

            void onEmptyDishes();

            void onDataNotAvailable();

        }

        interface GetRestoredDishCallback {
            void onRestoredDishLoaded(int position, Dish dish, boolean isViewExpanded);
        }
    }
}

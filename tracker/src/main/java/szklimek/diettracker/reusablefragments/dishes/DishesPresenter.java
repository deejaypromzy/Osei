package szklimek.diettracker.reusablefragments.dishes;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import szklimek.diettracker.data.model.Dish;
import szklimek.diettracker.addfood.AddActivityModel;

/**
 * Implementation of AddDishesPresenter
 */

class DishesPresenter implements DishesContract.Presenter {

    @NonNull
    private DishesContract.Model mActivityModel;

    @NonNull
    private DishesContract.View mAddDishesView;

    DishesPresenter(@NonNull AddActivityModel model,
                    @NonNull DishesContract.View dishesView) {
        mAddDishesView = dishesView;
        mActivityModel = model;

        mAddDishesView.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void loadDishes() {
        mActivityModel.getDishes(new DishesContract.Model.GetDishesCallback() {
            @Override
            public void onDishesLoaded(List<Dish> dishesList, ArrayList<Boolean> dishesViewExpandedArray) {
                mAddDishesView.showDishList(dishesList, dishesViewExpandedArray);
                mAddDishesView.hideEmptyView();
            }

            @Override
            public void onEmptyDishes() {
                mAddDishesView.showEmptyView();
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("DishesData", "Data not available");
            }

        });

    }

    @Override
    public void saveDishesViewCache(ArrayList<Boolean> dishesExpandedArray) {
        mActivityModel.saveDishesViewCache(dishesExpandedArray);
    }

    @Override
    public void onResumeView() {
        loadDishes();
    }

    @Override
    public void restoreRemovedDish() {
        mActivityModel.getRestoreDishCache(new DishesContract.Model.GetRestoredDishCallback() {
            @Override
            public void onRestoredDishLoaded(int position, Dish dish, boolean isViewExpanded) {
                mAddDishesView.restoreRemovedDish(position, dish, isViewExpanded);
                mActivityModel.setDishShown(dish);
                mAddDishesView.hideEmptyView();
            }
        });

    }


    @Override
    public void onPause() {
        mActivityModel.deleteHiddenDishes();
    }

    @Override
    public void onSnackBarUndoClicked() {
        restoreRemovedDish();
    }

    @Override
    public void onEditDishClicked(Dish dish) {
        mAddDishesView.showEditDish(dish);
    }

    @Override
    public void onAddDishClicked(Dish dish) {
        mActivityModel.setDialogFoodCache(dish);
        mActivityModel.setDialogMode(false);
        mAddDishesView.showAddFoodDialog();
    }

    @Override
    public void onDeleteDishClicked(int position, Dish dish, boolean isViewExpanded) {
        mActivityModel.saveRestoreDishCache(position, dish, isViewExpanded);
        mActivityModel.setDishHidden(dish);
        mAddDishesView.showUndoSnackBar();
    }

    @Override
    public void onNewDishClicked() {
        mAddDishesView.showNewDish();
    }


}

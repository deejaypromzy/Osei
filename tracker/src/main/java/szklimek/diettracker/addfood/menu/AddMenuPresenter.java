package szklimek.diettracker.addfood.menu;

import android.support.annotation.NonNull;

import java.util.List;

import szklimek.diettracker.data.model.Food;

/**
 * Implementation of AddMenuFragment presenter
 */

class AddMenuPresenter implements AddMenuContract.Presenter {

    @NonNull
    private AddMenuContract.View mAddMenuView;

    @NonNull
    private AddMenuContract.Model mActivityModel;

    AddMenuPresenter(@NonNull AddMenuContract.Model model,
                     @NonNull AddMenuContract.View menuView) {
        mAddMenuView = menuView;
        mActivityModel = model;

        mAddMenuView.setPresenter(this);
    }

    @Override
    public void start() {
        mActivityModel.getRecentlyAddedFood(new AddMenuContract.Model.GetRecentlyAddedFoodCallback() {
            @Override
            public void onFoodLoaded(List<Food> foods) {
                mAddMenuView.updateFoods(foods);
            }
        });
    }

    @Override
    public void onDishesClicked() {
        mAddMenuView.showDishes();
    }

    @Override
    public void onProductsClicked() {
        mAddMenuView.showProducts();
    }

    @Override
    public void onFoodClicked(Food food) {
        mActivityModel.setDialogFoodCache(food);
        mActivityModel.setDialogMode(false);
        mAddMenuView.showAddDialog();
    }

}

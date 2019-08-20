package szklimek.diettracker.createeditdish.dishcreator;

import android.support.annotation.NonNull;
import android.util.Log;

import szklimek.diettracker.createeditdish.DishContract;
import szklimek.diettracker.createeditdish.DishModel;
import szklimek.diettracker.data.model.Dish;
import szklimek.diettracker.data.model.Product;

/**
 * Implementation of DishCreator presenter
 */

public class DishCreatorPresenter implements
        DishCreatorContract.Presenter,
        DishCreatorContract.Model.OnDishProductsChangeListener{

    @NonNull
    private DishModel mActivityModel;

    @NonNull
    private DishCreatorContract.View mDishCreatorView;

    DishCreatorPresenter(@NonNull DishModel model,
                          @NonNull DishCreatorContract.View view) {
        mDishCreatorView = view;
        mActivityModel = model;

        mDishCreatorView.setPresenter(this);
    }

    @Override
    public void start() {
        mActivityModel.addDishProductsListener(this);
        getStartingDish();
    }

    @Override
    public void onDestroy() {
        mActivityModel.removeDishProductsListener();
    }

    @Override
    public void getStartingDish() {
        mActivityModel.getStartingDish(new DishCreatorContract.Model.GetEditedDishCallback() {
            @Override
            public void onDishLoaded(Dish dish) {
                mDishCreatorView.updateEditDishView(dish);
                if(dish.getDishProductsList().size() == 0) mDishCreatorView.showEmptyProductsView();
                else mDishCreatorView.hideEmptyProductsView();
            }
        });
    }

    @Override
    public void getEditedDish() {
        mActivityModel.getEditedDish(new DishCreatorContract.Model.GetEditedDishCallback() {
            @Override
            public void onDishLoaded(Dish dish) {
                mDishCreatorView.updateEditDishView(dish);
                if(dish.getDishProductsList().size() == 0) mDishCreatorView.showEmptyProductsView();
                else mDishCreatorView.hideEmptyProductsView();
            }
        });
    }

    @Override
    public void onCancelClicked() {
        mDishCreatorView.cancel();
    }

    @Override
    public void onSaveClicked() {
        mActivityModel.checkDishInput(new DishCreatorContract.Model.CheckDishInputCallback() {
            @Override
            public void onNameError() {
                mDishCreatorView.showNameError();
            }

            @Override
            public void onProductsError() {
                mDishCreatorView.showProductsError();
            }

            @Override
            public void onInputOk() {
                mActivityModel.saveDish(new DishCreatorContract.Model.SaveDishCallback() {
                    @Override
                    public void onDishSaved(String dishName) {
                        mDishCreatorView.showSuccess(dishName);
                        mDishCreatorView.cancel();
                    }
                });
            }
        });
    }

    @Override
    public void onNewProductClicked() {
        mActivityModel.setConfigurationChanged(true);
        mDishCreatorView.showProducts();
    }

    @Override
    public void onProductClicked(int position, Product product) {
        mActivityModel.setDialogProductCache(product);
        mActivityModel.setEditProductPosition(position);
        mActivityModel.setDialogEditMode(true);
        mDishCreatorView.showAddDialog();
    }

    @Override
    public void onNameChanged(String name) {
        if(!name.isEmpty()) mDishCreatorView.hideNameError();
        mActivityModel.setDishName(name);
    }

    @Override
    public void onDescriptionChanged(String description) {
        mActivityModel.setDishDescription(description);
    }

    @Override
    public void onTypeChanged(int type) {
        mActivityModel.setDishType(type);
    }

    @Override
    public void onDishProductsChange() {
        getEditedDish();
    }

}

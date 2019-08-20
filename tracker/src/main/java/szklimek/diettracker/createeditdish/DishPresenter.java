package szklimek.diettracker.createeditdish;

import android.support.annotation.NonNull;

import szklimek.diettracker.data.model.Product;

/**
 * Implementation of DishActivity presenter
 */

public class DishPresenter implements DishContract.Presenter {
    @NonNull
    private DishContract.Model mActivityModel;

    @NonNull
    private DishContract.View mActivityView;

    DishPresenter(@NonNull DishContract.Model model,
                    @NonNull DishContract.View view) {
        mActivityView = view;
        mActivityModel = model;
        mActivityView.setPresenter(this);
    }

    @Override
    public void start() {
        mActivityView.showCreator();
    }

    @Override
    public void onConfigurationChanged() {
        mActivityModel.setConfigurationChanged(true);
    }

    @Override
    public void onProductClicked(Product product) {
        mActivityModel.setDialogProductCache(product);
        mActivityModel.setDialogEditMode(false);
        mActivityView.showAddDialog();
    }

    @Override
    public void onDialogSaveDismissed() {
        mActivityView.hideProducts();
    }

    @Override
    public void onIntentLoaded(long dishId) {
        mActivityModel.setEditedDishId(dishId);
    }

}

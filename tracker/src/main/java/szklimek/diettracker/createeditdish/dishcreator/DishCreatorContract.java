package szklimek.diettracker.createeditdish.dishcreator;

import java.util.List;

import szklimek.diettracker.BasePresenter;
import szklimek.diettracker.BaseView;
import szklimek.diettracker.data.model.Dish;
import szklimek.diettracker.data.model.Product;

/**
 * Contract between DishCreatorFragment View, Presenter and Model
 */

public interface DishCreatorContract {

    interface View extends BaseView<Presenter> {

        void cancel();

        void updateEditDishView(Dish dish);

        void showProducts();

        void showAddDialog();

        void showEmptyProductsView();

        void hideEmptyProductsView();

        void showNameError();

        void hideNameError();

        void showProductsError();

        void showSuccess(String dishName);

        void showBackAlert();

    }

    interface Presenter extends BasePresenter {

        void onDestroy();

        void getStartingDish();

        void getEditedDish();

        void onNewProductClicked();

        void onProductClicked(int position, Product product);

        void onNameChanged(String name);

        void onDescriptionChanged(String description);

        void onTypeChanged(int type);

        void onCancelClicked();

        void onSaveClicked();

    }

    interface Model {

        interface GetEditedDishCallback{

            void onDishLoaded(Dish dish);

        }

        void getStartingDish(GetEditedDishCallback callback);

        void getEditedDish(GetEditedDishCallback callback);

        void setDishName(String name);

        void setDishType(int type);

        void setDishDescription(String description);

        void setEditProductPosition(int position);

        void addDishProduct(Product product);

        void updateProduct(int position, Product product);

        void removeProduct(int position);

        void checkDishInput(CheckDishInputCallback callback);

        interface CheckDishInputCallback{
            void onNameError();

            void onProductsError();

            void onInputOk();
        }

        void saveDish(SaveDishCallback callback);

        interface SaveDishCallback{
            void onDishSaved(String dishName);
        }

        interface OnDishProductsChangeListener {
            void onDishProductsChange();
        }

        void addDishProductsListener(OnDishProductsChangeListener listener);

        void removeDishProductsListener();

        void notifyDishProductsChange();

    }
}

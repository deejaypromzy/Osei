package szklimek.diettracker.reusablefragments.adddialog;

import android.support.annotation.NonNull;

import szklimek.diettracker.data.model.Food;

/**
 * Implementation of AddFoodDialog presenter
 */

class AddFoodDialogPresenter implements AddFoodDialogContract.Presenter {

    @NonNull
    private AddFoodDialogContract.Model mAddActivityModel;

    @NonNull
    private AddFoodDialogContract.View mAddFoodDialogView;

    AddFoodDialogPresenter(@NonNull AddFoodDialogContract.Model addActivityModel,
                           @NonNull AddFoodDialogContract.View addFoodDialogView) {
        mAddFoodDialogView = addFoodDialogView;
        mAddActivityModel = addActivityModel;

        mAddFoodDialogView.setPresenter(this);
    }

    @Override
    public void start() {
        getFoodData();
    }

    @Override
    public void getFoodData() {
        mAddActivityModel.getDialogFoodCache(new AddFoodDialogContract.Model.GetDialogFoodCallback() {
            @Override
            public void onFoodLoaded(Food food, int seekbarProgress, boolean isInEditMode) {
                mAddFoodDialogView.updateFoodData(food, seekbarProgress);
                if (!isInEditMode) {
                    mAddFoodDialogView.setAddMode();
                }
            }
        });
    }

    @Override
    public void onSeekBarProgressChanged(int progress) {
        mAddActivityModel.getUpdatedWeightValuesPerProgress(new AddFoodDialogContract.Model.GetUpdatedWeightValuesCallback() {
            @Override
            public void onValuesUpdatedCounted(Food food, int weight) {
                mAddFoodDialogView.updateFoodValuesView(food, weight);
            }
        }, progress);
    }

    @Override
    public void onSaveClicked() {
        mAddActivityModel.saveDialogCacheFood(new AddFoodDialogContract.Model.DialogSavedCallback() {
            @Override
            public void onDialogSaved() {
                mAddFoodDialogView.dismissDialog();
                mAddFoodDialogView.notifySaveDismissed();
            }
        });
    }

    @Override
    public void onDeleteClicked() {
        mAddActivityModel.deleteDialogCacheFood();
        mAddFoodDialogView.dismissDialog();
    }

    @Override
    public void onCancelClicked() {
        mAddFoodDialogView.dismissDialog();
    }

}

package szklimek.diettracker.browse.menu;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.HashMap;

import szklimek.diettracker.browse.BrowseModel;
import szklimek.diettracker.data.model.NutrientCategory;

/**
 * Implementation of BrowseMenuFragment presenter
 */

public class BrowseMenuPresenter implements BrowseMenuContract.Presenter {

    @NonNull
    private BrowseModel mActivityModel;

    @NonNull
    private BrowseMenuContract.View mMenuView;

    BrowseMenuPresenter(@NonNull BrowseModel model,
                        @NonNull BrowseMenuContract.View view) {
        mMenuView = view;
        mActivityModel = model;

        mMenuView.setPresenter(this);
    }

    @Override
    public void start() {
        getNutrients();
    }

    @Override
    public void getNutrients() {
        mActivityModel.getNutrients(new BrowseMenuContract.Model.GetNutrientsCallback() {
            @Override
            public void onNutrientsLoaded(SparseArray<NutrientCategory> nutrients) {
                mMenuView.updateNutrients(nutrients);
            }
        });
    }

    @Override
    public void onSearchClick() {
        mMenuView.showFood();
    }

    @Override
    public void onCategoryChange(int category) {
        mActivityModel.setCategory(category);
    }

    @Override
    public void onNutrientMinValueChange(int nutrientPosition, double minValue) {
        mActivityModel.setNutrientMinValue(nutrientPosition, minValue);
    }

    @Override
    public void onNutrientMinValueStateChange(int nutrientPosition, boolean isChecked) {
        mActivityModel.setNutrientMinState(nutrientPosition, isChecked);
    }

    @Override
    public void onNutrientMaxValueChange(int nutrientPosition, double maxValue) {
        mActivityModel.setNutrientMaxValue(nutrientPosition, maxValue);
    }

    @Override
    public void onNutrientMaxValueStateChange(int nutrientPosition, boolean isChecked) {
        mActivityModel.setNutrientMaxState(nutrientPosition, isChecked);
    }

    @Override
    public void onNutrientStateChange(int nutrientPosition, boolean isChecked) {
        mActivityModel.setNutrientState(nutrientPosition, isChecked);
    }

    @Override
    public void onSearchQueryChange(String query) {
        mActivityModel.setQuery(query);
    }
}

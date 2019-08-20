package szklimek.diettracker.browse.menu;

import android.util.SparseArray;

import java.util.HashMap;
import java.util.TreeMap;

import szklimek.diettracker.BasePresenter;
import szklimek.diettracker.BaseView;
import szklimek.diettracker.browse.BrowseContract;
import szklimek.diettracker.data.local.FoodDataSource;
import szklimek.diettracker.data.model.Nutrient;
import szklimek.diettracker.data.model.NutrientCategory;

/**
 * Contract between BrowseMenuFragment View, Presenter and Model
 */

public interface BrowseMenuContract {

    interface View extends BaseView<Presenter> {

        void updateNutrients(SparseArray<NutrientCategory> nutrients);

        void showFood();

    }

    interface Presenter extends BasePresenter {

        void getNutrients();

        void onSearchClick();

        void onCategoryChange(int category);

        void onNutrientMinValueChange(int nutrientPosition, double minValue);

        void onNutrientMinValueStateChange(int nutrientPosition, boolean isChecked);

        void onNutrientMaxValueChange(int nutrientPosition, double maxValue);

        void onNutrientMaxValueStateChange(int nutrientPosition, boolean isChecked);

        void onNutrientStateChange(int nutrientPosition, boolean isChecked);

        void onSearchQueryChange(String query);

    }

    interface Model {

        void getNutrients(GetNutrientsCallback callback);

        interface GetNutrientsCallback {
            void onNutrientsLoaded(SparseArray<NutrientCategory> nutrients);
        }

        void setCategory(int category);

        void setQuery(String query);

        void setNutrientMinValue(int position, double value);

        void setNutrientMinState(int position, boolean isChecked);

        void setNutrientMaxValue(int position, double value);

        void setNutrientMaxState(int position, boolean isChecked);

        void setNutrientState(int position, boolean isChecked);

    }

}

package szklimek.diettracker.main.mealdetails;

import android.support.annotation.NonNull;

import szklimek.diettracker.data.model.Meal;
import szklimek.diettracker.main.MainModel;

/**
 * Implementation of MealDetailsFragment presenter
 */

class MealDetailsPresenter implements MealDetailsContract.Presenter{

    @NonNull
    private MainModel mActivityModel;

    @NonNull
    private MealDetailsContract.View mDetailsView;

    MealDetailsPresenter(@NonNull MainModel model,
                        @NonNull MealDetailsContract.View view) {
        mDetailsView = view;
        mActivityModel = model;

        mDetailsView.setPresenter(this);
    }

    @Override
    public void start() {
        getMeal();
    }

    @Override
    public void getMeal() {
        mActivityModel.getClickedMeal(new MealDetailsContract.Model.GetClickedMealCallback() {
            @Override
            public void onMealLoaded(Meal meal) {
                mDetailsView.showMealData(meal);
            }
        });
    }
}

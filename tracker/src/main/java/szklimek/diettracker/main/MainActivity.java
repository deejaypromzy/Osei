package szklimek.diettracker.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

import szklimek.diettracker.BaseNavigationActivity;
import szklimek.diettracker.BaseNavigationActivityPresenter;
import szklimek.diettracker.R;
import szklimek.diettracker.addfood.AddActivity;
import szklimek.diettracker.data.local.ProductsDataManager;
import szklimek.diettracker.data.local.ProductsDataSource;
import szklimek.diettracker.data.local.SharedPreferencesManager;
import szklimek.diettracker.data.local.FoodDataSource;
import szklimek.diettracker.data.local.FoodRepository;
import szklimek.diettracker.data.model.Diet;
import szklimek.diettracker.diet.DietActivity;
import szklimek.diettracker.main.dailyprogress.DailyProgressFragment;
import szklimek.diettracker.main.dailyprogresspage.DailyProgressPageFragment;
import szklimek.diettracker.main.mealdetails.MealDetailsFragment;

public class MainActivity extends BaseNavigationActivity implements
        MainContract.View {

    public static final String MEAL_NUMBER = "meal_number";

    public static final String FRAGMENT_DAILY_MEALS_TAG = "daily_meals";

    public static final String FRAGMENT_MEAL_DETAILS_TAG = "meal_details";

    MainContract.Presenter mPresenter;

    MainModel mModel;

    ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setupPresenter() {
        FoodDataSource mFoodDataSource = FoodRepository.getInstance(getApplicationContext());

        SharedPreferencesManager mSharedPreferencesManager = SharedPreferencesManager.getInstance(
                getSharedPreferences(
                        getString(R.string.sh_pref_user_preferences_file_key),
                        MODE_PRIVATE)
        );

        ProductsDataSource mProductsDataManager = ProductsDataManager.getInstance(
                getApplicationContext());

        mModel = MainModel.getInstance(
                mFoodDataSource,
                mSharedPreferencesManager,
                mProductsDataManager
        );

        mPresenter = new MainPresenter(
                mModel, // Model
                this); // View
    }

    @Override
    public BaseNavigationActivityPresenter getActivityPresenter() {
        return mPresenter;
    }

    public MainModel getActivityModel() {
        return mModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        for(Fragment fragment : mFragments){
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showNewDiet() {
        Intent launchNewDietIntent = new Intent(this, DietActivity.class);
        startActivity(launchNewDietIntent);
    }

    @Override
    public void showAddFood(int mealNumber) {
        Intent addActivityIntent = new Intent(this, AddActivity.class);
        addActivityIntent.putExtra(AddActivity.MEAL_ID_KEY, mealNumber);
        startActivity(addActivityIntent);
    }

    @Override
    public void showDailyMeals(Diet diet) {
        Fragment mDailyProgressFragment = new DailyProgressFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mDailyProgressFragment, FRAGMENT_DAILY_MEALS_TAG);
        fragmentTransaction.commit();
        mFragments.add(mDailyProgressFragment);
    }

    @Override
    public void showMealDetails() {

        Fragment mMealDetailsFragment = new MealDetailsFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mMealDetailsFragment, FRAGMENT_DAILY_MEALS_TAG);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
        mFragments.add(mMealDetailsFragment);
    }

}

package szklimek.diettracker.addfood;

import android.databinding.DataBindingUtil;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import szklimek.diettracker.BaseActivity;
import szklimek.diettracker.R;
import szklimek.diettracker.databinding.ActivityAddBinding;
import szklimek.diettracker.reusablefragments.adddialog.AddFoodDialogContract;
import szklimek.diettracker.data.local.SharedPreferencesManager;
import szklimek.diettracker.reusablefragments.adddialog.AddFoodDialogFragment;
import szklimek.diettracker.reusablefragments.dishes.DishesFragment;
import szklimek.diettracker.addfood.menu.AddMenuFragment;
import szklimek.diettracker.data.local.FoodDataSource;
import szklimek.diettracker.data.local.FoodRepository;
import szklimek.diettracker.data.model.Product;
import szklimek.diettracker.reusablefragments.products.ProductsContract;
import szklimek.diettracker.reusablefragments.products.ProductsFragment;

import static szklimek.diettracker.reusablefragments.adddialog.AddFoodDialogFragment.ADD_FOOD_DIALOG;

public class AddActivity extends BaseActivity implements
        AddActivityContract.View,
        AddFoodDialogContract.View.DialogSaveDismissedCallback,
        ProductsContract.View.ProductsClickedCallback {

    public static final String MEAL_ID_KEY = "meal_id";
    public static final int NO_MEAL_ID = -1;

    private final String FRAGMENT_MENU_TAG = "menu";
    private final String FRAGMENT_DISH_TAG = "dish";
    private final String FRAGMENT_PRODUCT_TAG = "product";
    private final String LAST_FRAGMENT_TAG = "last_fragment";

    private Fragment lastFragment;
    private DialogFragment mAddFoodDialog;

    AddActivityModel mModel;
    AddActivityContract.Presenter mPresenter;
    FoodDataSource mFoodRepository;
    SharedPreferencesManager mSharedPreferencesManager;

    @Override
    public void setPresenter(AddActivityContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setupPresenter() {
        mFoodRepository = FoodRepository.getInstance(getApplicationContext());
        mSharedPreferencesManager = SharedPreferencesManager.getInstance(
                getSharedPreferences(
                        getString(R.string.sh_pref_user_preferences_file_key),
                        MODE_PRIVATE)
        );
        mModel = AddActivityModel.getInstance(mFoodRepository, mSharedPreferencesManager);
        mPresenter = new AddActivityPresenter(this, mModel);
    }

    public AddActivityModel getActivityModel() {
        return mModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add);
        setSupportActionBar(binding.toolbarLayout.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupPresenter();

        int mealId = getIntent().getIntExtra(MEAL_ID_KEY, NO_MEAL_ID);
        mPresenter.onIntentLoaded(mealId);

        if (savedInstanceState != null) {
            lastFragment = getSupportFragmentManager().getFragment(savedInstanceState, LAST_FRAGMENT_TAG);

        } else {
            lastFragment = new AddMenuFragment();

        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,
                lastFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        lastFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        getSupportFragmentManager().putFragment(outState, LAST_FRAGMENT_TAG, lastFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter = null;
        mFoodRepository = null;
        mSharedPreferencesManager = null;
    }

    @Override
    public void showDishes() {
        DishesFragment dishesFragment = new DishesFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,
                dishesFragment, FRAGMENT_DISH_TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void showProducts() {
        ProductsFragment productsFragment = new ProductsFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,
                productsFragment, FRAGMENT_PRODUCT_TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void showAddFoodDialog() {
        mAddFoodDialog = new AddFoodDialogFragment();
        mAddFoodDialog.show(getSupportFragmentManager(), ADD_FOOD_DIALOG);
    }

    @Override
    public void onDialogSaveDismissed() {
        finish();
    }

    @Override
    public void sendClickedProduct(Product product) {
        mAddFoodDialog = new AddFoodDialogFragment();
        mAddFoodDialog.show(getSupportFragmentManager(), ADD_FOOD_DIALOG);
    }

}

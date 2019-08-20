package szklimek.diettracker.createeditdish;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

import szklimek.diettracker.BaseActivity;
import szklimek.diettracker.BaseNavigationActivity;
import szklimek.diettracker.BaseNavigationActivityPresenter;
import szklimek.diettracker.R;
import szklimek.diettracker.databinding.ActivityDishBinding;
import szklimek.diettracker.reusablefragments.adddialog.AddFoodDialogContract;
import szklimek.diettracker.reusablefragments.adddialog.AddFoodDialogFragment;
import szklimek.diettracker.createeditdish.dishcreator.DishCreatorFragment;
import szklimek.diettracker.data.local.SharedPreferencesManager;
import szklimek.diettracker.data.local.FoodDataSource;
import szklimek.diettracker.data.local.FoodRepository;
import szklimek.diettracker.data.model.Product;
import szklimek.diettracker.reusablefragments.products.ProductsContract;
import szklimek.diettracker.reusablefragments.products.ProductsFragment;

import static szklimek.diettracker.reusablefragments.adddialog.AddFoodDialogFragment.ADD_FOOD_DIALOG;

/**
 * Activity which enables user to create or edit dish. User can change name, type of dish,
 * add and remove products from which dish consists of.
 */

public class DishActivity extends BaseActivity implements
        DishContract.View,
        ProductsContract.View.ProductsClickedCallback,
        AddFoodDialogContract.View.DialogSaveDismissedCallback{

    public static final String DISH_ID_KEY = "dish_id";
    public static final long NO_DISH = -1;

    private static final String FRAGMENT_CREATOR_TAG = "creator";
    private static final String FRAGMENT_PRODUCTS_TAG = "products";

    DishContract.Presenter mPresenter;

    DishModel mModel;

    Fragment mDishCreatorFragment;
    Fragment mProductsFragment;
    ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    public void setPresenter(DishContract.Presenter presenter) {
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
        mModel = DishModel.getInstance(mFoodDataSource, mSharedPreferencesManager);
        mPresenter = new DishPresenter(mModel,this);

        long dishId = getIntent().getLongExtra(DISH_ID_KEY, NO_DISH);
        mPresenter.onIntentLoaded(dishId);

        mPresenter.start();
    }

    public DishModel getActivityModel() {
        return mModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupPresenter();
        ActivityDishBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_dish);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().beginTransaction().remove(mDishCreatorFragment).commit();
        for(Fragment fragment : mFragments){
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        super.onSaveInstanceState(outState);

        mPresenter.onConfigurationChanged();

    }

    @Override
    public void showCreator() {

        mDishCreatorFragment = new DishCreatorFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mDishCreatorFragment, FRAGMENT_CREATOR_TAG);
        fragmentTransaction.commit();
        mFragments.add(mDishCreatorFragment);
    }

    @Override
    public void showProducts() {

        mProductsFragment = new ProductsFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mProductsFragment, FRAGMENT_PRODUCTS_TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        mFragments.add(mProductsFragment);

    }

    @Override
    public void hideProducts() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showAddDialog() {
        DialogFragment mAddFoodDialog = new AddFoodDialogFragment();
        mAddFoodDialog.show(getSupportFragmentManager(), ADD_FOOD_DIALOG);
    }

    @Override
    public void sendClickedProduct(Product product) {
        mPresenter.onProductClicked(product);
    }

    @Override
    public void onDialogSaveDismissed() {
        mPresenter.onDialogSaveDismissed();
    }
}

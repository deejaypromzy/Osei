package szklimek.diettracker.browse;

import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import szklimek.diettracker.BaseNavigationActivity;
import szklimek.diettracker.BaseNavigationActivityPresenter;
import szklimek.diettracker.R;
import szklimek.diettracker.browse.detail.BrowseDetailFragment;
import szklimek.diettracker.data.model.Product;
import szklimek.diettracker.databinding.ActivityBrowseBinding;
import szklimek.diettracker.reusablefragments.products.ProductsContract;
import szklimek.diettracker.reusablefragments.products.ProductsFragment;
import szklimek.diettracker.browse.menu.BrowseMenuFragment;
import szklimek.diettracker.data.local.FoodDataSource;
import szklimek.diettracker.data.local.FoodRepository;

public class BrowseActivity extends BaseNavigationActivity implements BrowseContract.View,
        ProductsContract.View.ProductsClickedCallback {

    BrowseContract.Presenter mPresenter;
    BrowseModel mModel;
    FoodDataSource mFoodDataSource;

    @Override
    public void setPresenter(BrowseContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setupPresenter() {
        mFoodDataSource = FoodRepository.getInstance(getApplicationContext());
        mModel = BrowseModel.getInstance(mFoodDataSource);
        mPresenter = new BrowsePresenter(mModel,this);
        mPresenter.start();
    }

    @Override
    public BaseNavigationActivityPresenter getActivityPresenter() {
        return mPresenter;
    }

    public BrowseModel getActivityModel() {
        return mModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        ActivityBrowseBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_browse);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupPresenter();
    }

    @Override
    public void showMenu() {
        BrowseMenuFragment menuFragment = new BrowseMenuFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, menuFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void showProducts() {

        ProductsFragment foodFragment = new ProductsFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, foodFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void showProductDetails() {
        BrowseDetailFragment detailFragment = new BrowseDetailFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void sendClickedProduct(Product product) {
        mPresenter.onProductClicked();
    }

}

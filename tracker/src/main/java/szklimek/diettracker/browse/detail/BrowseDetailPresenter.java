package szklimek.diettracker.browse.detail;

import android.support.annotation.NonNull;

import szklimek.diettracker.browse.BrowseModel;

/**
 * Implementation of BrowseDetailFragment presenter
 */

public class BrowseDetailPresenter implements BrowseDetailContract.Presenter {

    @NonNull
    private BrowseModel mActivityModel;

    @NonNull
    private BrowseDetailContract.View mMenuView;

    BrowseDetailPresenter(@NonNull BrowseModel model,
                          @NonNull BrowseDetailContract.View view) {
        mMenuView = view;
        mActivityModel = model;

        mMenuView.setPresenter(this);
    }

    @Override
    public void start() {

    }


    @Override
    public void getClickedProduct() {

    }

    @Override
    public void onMenuCounterClick() {

    }

    @Override
    public void onInternetReportClick() {

    }
}

package szklimek.diettracker.browse.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import szklimek.diettracker.BaseFragment;
import szklimek.diettracker.R;
import szklimek.diettracker.Utilities;
import szklimek.diettracker.browse.BrowseActivity;
import szklimek.diettracker.data.model.Product;
import szklimek.diettracker.databinding.FragmentBrowseDetailsBinding;

/**
 * Fragment which displays detail of clicked food
 */

public class BrowseDetailFragment extends BaseFragment implements BrowseDetailContract.View {

    BrowseDetailContract.Presenter mPresenter;

    FragmentBrowseDetailsBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBrowseDetailsBinding.inflate(
                LayoutInflater.from(getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupPresenter();
    }

    @Override
    public void setupPresenter() {
        mPresenter = new BrowseDetailPresenter(
                ((BrowseActivity) getActivity()).getActivityModel(),
                this
        );
    }

    @Override
    public void setPresenter(BrowseDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateView(Product product) {
        binding.category.setText(Utilities.getStringProductType(getContext(), product.getType()));
        binding.name.setText(product.getName());
        binding.description.setText(product.getName());
    }

    @Override
    public void showNutrientsCounter() {
        //TODO
    }

    @Override
    public void showInternetReport() {
        //TODO
    }
}

package szklimek.diettracker.reusablefragments.products;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import szklimek.diettracker.BaseFragment;
import szklimek.diettracker.R;
import szklimek.diettracker.Utilities;
import szklimek.diettracker.addfood.AddActivity;
import szklimek.diettracker.browse.BrowseActivity;
import szklimek.diettracker.createeditdish.DishActivity;
import szklimek.diettracker.data.model.Nutrient;
import szklimek.diettracker.data.model.Product;
import szklimek.diettracker.databinding.FragmentProductsBinding;

/**
 * Fragment which displays food list which matches parameters set in menu
 */

public class ProductsFragment extends BaseFragment implements ProductsContract.View {

    ProductsContract.Presenter mPresenter;

    FragmentProductsBinding binding;

    ProductsAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductsBinding.inflate(
                LayoutInflater.from(getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ProductsAdapter();
        mLayoutManager = new LinearLayoutManager(getContext());
        binding.foodRecyclerView.setLayoutManager(mLayoutManager);
        binding.foodRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getContext(),
                mLayoutManager.getOrientation());
        binding.foodRecyclerView.addItemDecoration(mDividerItemDecoration);

        binding.nameSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPresenter.onQueryChange(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPresenter.onQueryChange(newText);
                return false;
            }
        });

        setupPresenter();
    }

    @Override
    public void setupPresenter() {
        if(getActivity() instanceof BrowseActivity){
            mPresenter = new ProductsPresenter(
                    ((BrowseActivity) getActivity()).getActivityModel(),
                    this
            );
        } else if (getActivity() instanceof DishActivity){
            mPresenter = new ProductsPresenter(
                    ((DishActivity) getActivity()).getActivityModel(),
                    this
            );
        } else if (getActivity() instanceof AddActivity) {
            mPresenter = new ProductsPresenter(
                    ((AddActivity) getActivity()).getActivityModel(),
                    this
            );
        }

        mPresenter.start();
    }

    @Override
    public void setPresenter(ProductsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void sendClickedProduct(Product product) {
        if(getActivity() instanceof ProductsClickedCallback){
            ((ProductsClickedCallback) getActivity()).sendClickedProduct(product);
        } else throw new RuntimeException("Activity using ProductsFragment must implement ProductsClickedCallback.");

    }

    @Override
    public void updateProducts(List<Product> products) {
        mAdapter.setData(products);
    }

    @Override
    public void updateQuery(String query) {
        binding.nameSearch.setQuery(query, false);
    }


    /**
     * Adapter which displays list of Products
     */
    private class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

        private List<Product> mProducts;

        /**
         * ViewHolder class
         */
        class ProductViewHolder extends RecyclerView.ViewHolder {
            private TextView mProductNameTextView;
            private TextView mProductDescriptionTextView;
            private TextView mProductCategoryTextView;
            private TextView mProductEnergyTextView;


            // ViewHolder constructor
            ProductViewHolder(View itemView) {
                super(itemView);
                mProductNameTextView = (TextView) itemView.findViewById(R.id.product_name);
                mProductEnergyTextView = (TextView) itemView.findViewById(R.id.product_energy);
                mProductCategoryTextView = (TextView) itemView.findViewById(R.id.product_category);
                mProductDescriptionTextView = (TextView) itemView.findViewById(R.id.product_description);
            }

        }

        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layoutId = R.layout.item_browse_product;
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductViewHolder holder, int position) {
            final int adapterPosition = holder.getAdapterPosition();
            final Product product = mProducts.get(adapterPosition);

            holder.mProductNameTextView.setText(product.getName());
            holder.mProductDescriptionTextView.setText(product.getDescription());
            holder.mProductCategoryTextView.setText(
                    Utilities.getStringProductType(getContext(), product.getType())
            );
            holder.mProductEnergyTextView.setText(getString(R.string.unit_format_calories_per_100g,
                    product.getNutrientPer100g(Nutrient.ENERGY)));

            holder.mProductNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyItemChanged(adapterPosition);
                    mPresenter.onProductClicked(adapterPosition, product);
                }
            });

        }

        @Override
        public int getItemCount() {
            if(mProducts == null) return 0;
            return mProducts.size();
        }

        public void setData(List<Product> products){
            mProducts = products;
            notifyDataSetChanged();
        }

    }
}

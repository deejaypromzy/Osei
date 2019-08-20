package szklimek.diettracker.createeditdish.dishcreator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import szklimek.diettracker.BaseFragment;
import szklimek.diettracker.R;
import szklimek.diettracker.createeditdish.DishActivity;
import szklimek.diettracker.data.model.Dish;
import szklimek.diettracker.data.model.Nutrient;
import szklimek.diettracker.data.model.Product;
import szklimek.diettracker.databinding.FragmentDishCreatorBinding;

/**
 * Fragment which enables user to create or edit dish
 */
public class DishCreatorFragment extends BaseFragment implements DishCreatorContract.View {

    ProductAdapter mProductsAdapter;

    DishCreatorContract.Presenter mPresenter;

    FragmentDishCreatorBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentDishCreatorBinding.inflate(
                LayoutInflater.from(getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupPresenter();
        mProductsAdapter = new ProductAdapter();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getContext(),
                mLayoutManager.getOrientation());
        binding.productsRecyclerView.addItemDecoration(mDividerItemDecoration);
        binding.productsRecyclerView.setLayoutManager(mLayoutManager);
        binding.productsRecyclerView.setAdapter(mProductsAdapter);
        binding.menuCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onCancelClicked();

            }
        });
        binding.menuSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onSaveClicked();
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onNewProductClicked();
            }
        });

        binding.editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPresenter.onNameChanged(editable.toString());
            }
        });

        binding.spinnerDishType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mPresenter.onTypeChanged(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.editTextDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPresenter.onDescriptionChanged(editable.toString());
            }
        });

        mPresenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void setPresenter(DishCreatorContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setupPresenter() {
        mPresenter = new DishCreatorPresenter(
                ((DishActivity) getActivity()).getActivityModel(),
                this
        );
    }

    @Override
    public void cancel() {
        getActivity().finish();
    }

    @Override
    public void updateEditDishView(Dish dish) {
        binding.editTextName.setText(dish.getName());
        binding.editTextDescription.setText(dish.getDescription());
        binding.spinnerDishType.setSelection(dish.getType());
        binding.energyValue.setText(
                getString(R.string.unit_format_calories, dish.getTotalNutrientValue(Nutrient.ENERGY))
        );
        binding.weightValue.setText(
                getString(R.string.unit_format_weight_g, dish.getTotalDishProductsWeight())
        );
        mProductsAdapter.setData(dish.getDishProductsList());
    }

    @Override
    public void showProducts() {
        ((DishActivity) getActivity()).showProducts();
    }

    @Override
    public void showAddDialog() {
        ((DishActivity) getActivity()).showAddDialog();
    }

    @Override
    public void showEmptyProductsView() {
        binding.emptyText.setVisibility(View.VISIBLE);
        binding.emptyImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyProductsView() {
        binding.emptyText.setVisibility(View.INVISIBLE);
        binding.emptyImage.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNameError() {
        binding.nameInputLayout.setErrorEnabled(true);
        binding.nameInputLayout.setError(getString(R.string.new_dish_activity_error_name));
    }

    @Override
    public void hideNameError() {
        binding.nameInputLayout.setErrorEnabled(false);
    }

    @Override
    public void showProductsError() {
        Toast noProductsToast = Toast.makeText(
                getContext(),
                getString(R.string.new_dish_products_empty_toast),
                Toast.LENGTH_SHORT);
        noProductsToast.show();
    }

    @Override
    public void showSuccess(String dishName) {
        Toast successToast = Toast.makeText(
                getContext(),
                getString(R.string.new_dish_success_toast, dishName),
                Toast.LENGTH_SHORT);
        successToast.show();
    }

    @Override
    public void showBackAlert() {

    }

    /**
     * Adapter of Products in DishActivity
     */

   class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

        private List<Product> mProducts;

        /**
         * ViewHolder class
         */
        class ProductViewHolder extends RecyclerView.ViewHolder {
            private final TextView mProductNameTextView;
            private TextView mProductEnergyTextView;
            private TextView mProductWeightTextView;
            private TextView mProductDescriptionTextView;


            ProductViewHolder(View itemView) {
                super(itemView);
                mProductNameTextView = (TextView) itemView.findViewById(R.id.item_product_name);
                mProductEnergyTextView = (TextView) itemView.findViewById(R.id.item_product_energy);
                mProductWeightTextView = (TextView) itemView.findViewById(R.id.item_product_weight);
                mProductDescriptionTextView = (TextView) itemView.findViewById(R.id.item_product_description);
            }
        }

        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layoutId = R.layout.item_dish_product;
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductViewHolder holder, int position) {
            final int adapterPosition = holder.getAdapterPosition();
            final Product product = mProducts.get(adapterPosition);
            final int weight = product.getWeight();
            double energySum = product.getNutrientPerWeight(Nutrient.ENERGY, weight);

            holder.mProductNameTextView.setText(product.getName());
            holder.mProductDescriptionTextView.setText(product.getDescription());
            holder.mProductWeightTextView.setText(getString(R.string.unit_format_weight_g, weight));
            holder.mProductEnergyTextView.setText(getString(R.string.unit_format_calories, energySum));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.onProductClicked(adapterPosition, product);
                    notifyItemChanged(adapterPosition);
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

package szklimek.diettracker.reusablefragments.dishes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import szklimek.diettracker.createeditdish.DishActivity;
import szklimek.diettracker.data.model.Dish;
import szklimek.diettracker.data.model.Nutrient;
import szklimek.diettracker.data.model.Product;
import szklimek.diettracker.R;
import szklimek.diettracker.Utilities;
import szklimek.diettracker.addfood.AddActivity;
import szklimek.diettracker.databinding.FragmentAddDishBinding;

/**
 * Fragment which display user dished and enable to add, edit and delete user's dishes
 */

public class DishesFragment extends Fragment implements DishesContract.View {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BUTTON_DELETE, BUTTON_EDIT, BUTTON_ADD})
    public @interface AddDishesAdapterButton {
    }

    public static final int BUTTON_DELETE = 0;
    public static final int BUTTON_EDIT = 1;
    public static final int BUTTON_ADD = 2;

    public static final String DISH_EXPANDED_ARRAY_KEY = "expanded_array";
    public static final String SCROLL_POSITION_KEY = "scroll_position";


    private AddDishesAdapter mDishAdapter;
    private Snackbar mUndoSnackbar;

    int mScrollPosition;

    private DishesPresenter mPresenter;

    FragmentAddDishBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mScrollPosition = savedInstanceState.getInt(SCROLL_POSITION_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddDishBinding.inflate(
                LayoutInflater.from(getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setHasFixedSize(true);
        mDishAdapter = new AddDishesAdapter();
        binding.recyclerView.setAdapter(mDishAdapter);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch add new dish
                mPresenter.onNewDishClicked();
            }
        });

        mPresenter = new DishesPresenter(
                ((AddActivity) getActivity()).getActivityModel(), // Activity MVP Model
                this // Fragment as MVP View
        );

        mPresenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResumeView();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPresenter.saveDishesViewCache(mDishAdapter.getExpandedArray());
        outState.putInt(SCROLL_POSITION_KEY,
                ((LinearLayoutManager) binding.recyclerView.getLayoutManager()).findFirstVisibleItemPosition());

        super.onSaveInstanceState(outState);
    }

    public void onDishButtonsClick(final int position, int clickedButtonId, Dish dish, boolean isViewExpanded) {
        switch (clickedButtonId) {
            case BUTTON_DELETE:
                mPresenter.onDeleteDishClicked(position, dish, isViewExpanded);
                break;

            case BUTTON_EDIT:
                mPresenter.onEditDishClicked(dish);
                break;

            case BUTTON_ADD:
                mPresenter.onAddDishClicked(dish);
                break;
        }
    }

    @Override
    public void setPresenter(DishesPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showDishList(List<Dish> dishes, ArrayList<Boolean> dishesExpandedArray) {
        mDishAdapter.setDishList(dishes, dishesExpandedArray);
        if(mScrollPosition > 0){
            binding.recyclerView.getLayoutManager().scrollToPosition(mScrollPosition);
        }
    }

    @Override
    public void showEmptyView() {
        binding.emptyText.setVisibility(View.VISIBLE);
        binding.emptyImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        binding.emptyText.setVisibility(View.INVISIBLE);
        binding.emptyImage.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showUndoSnackBar() {
        mUndoSnackbar = Snackbar.make(getView(), R.string.snackbar_deleted,
                Snackbar.LENGTH_LONG);

        mUndoSnackbar.setAction(R.string.snackbar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onSnackBarUndoClicked();
            }
        });

        mUndoSnackbar.show();
    }

    @Override
    public void restoreRemovedDish(int position, Dish dish, boolean isViewExpanded) {
        mDishAdapter.getDishList().add(position, dish);
        mDishAdapter.getExpandedArray().add(position, isViewExpanded);
        mDishAdapter.notifyItemInserted(position);
        mDishAdapter.notifyItemChanged(position);
        mDishAdapter.notifyItemRangeChanged(position, mDishAdapter.getItemCount());
        binding.recyclerView.getLayoutManager().scrollToPosition(position);
        hideEmptyView();
    }

    @Override
    public void showEditDish(Dish dish) {
        Intent editDishIntent = new Intent(getContext(), DishActivity.class);
        editDishIntent.putExtra(DishActivity.DISH_ID_KEY, dish.getId());
        startActivity(editDishIntent);
    }

    @Override
    public void showAddFoodDialog() {
        ((AddActivity) getActivity()).showAddFoodDialog();
    }

    @Override
    public void showNewDish() {
        Intent newDishIntent = new Intent(getContext(), DishActivity.class);
        startActivity(newDishIntent);
    }


    // Inner classes

    /**
     * Adapter which display dishes created by user from database (using Cursor) in RecyclerView
     */

    class AddDishesAdapter extends RecyclerView.Adapter<AddDishesAdapter.DishViewHolder> {

        ArrayList<Boolean> mDishExpandedArray;
        List<Dish> mDishList;

        /**
         * ViewHolder class
         */
        class DishViewHolder extends RecyclerView.ViewHolder {
            TextView mDishNameTextView;
            TextView mDishEnergyTextView;
            TextView mDishDescriptionTextView;
            TextView mDishTypeTextView;
            TextView mDishProteinTextView;
            TextView mDishCarboTextView;
            TextView mDishFatTextView;
            ImageButton mDishExpandButton;
            View mDishExpandLayout;
            TableLayout mDishExpandProductsTableLayout;
            ImageButton mDishMenuButton;
            Button mDishAddButton;

            DishViewHolder(View itemView) {
                super(itemView);
                mDishNameTextView = (TextView) itemView.findViewById(R.id.item_name);
                mDishDescriptionTextView = (TextView) itemView.findViewById(R.id.description);
                mDishEnergyTextView = (TextView) itemView.findViewById(R.id.calories_per_100g);
                mDishTypeTextView = (TextView) itemView.findViewById(R.id.item_type);
                mDishProteinTextView = (TextView) itemView.findViewById(R.id.expand_protein_value);
                mDishCarboTextView = (TextView) itemView.findViewById(R.id.expand_carbo_value);
                mDishFatTextView = (TextView) itemView.findViewById(R.id.expand_fat_value);
                mDishExpandButton = (ImageButton) itemView.findViewById(R.id.expand_button);
                mDishExpandLayout = itemView.findViewById(R.id.products_expand_layout);
                mDishExpandProductsTableLayout =
                        (TableLayout) itemView.findViewById(R.id.products_expand_table);
                mDishMenuButton =
                        (ImageButton) itemView.findViewById(R.id.menu_button);
                mDishAddButton = (Button) itemView.findViewById(R.id.add_button);
            }
        }

        @Override
        public DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layoutId = R.layout.item_dish;
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new DishViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final DishViewHolder holder, int position) {
            final int adapterPosition = holder.getAdapterPosition();

            final Dish dish = mDishList.get(adapterPosition);

            holder.mDishNameTextView.setText(dish.getName());

            holder.mDishTypeTextView.setText(
                    Utilities.getStringDishType(getContext(), dish.getType()));

            holder.mDishDescriptionTextView.setText(dish.getDescription());

            holder.mDishEnergyTextView.setText(getString(
                    R.string.unit_format_calories_per_100g,
                    dish.getNutrientPer100g(Nutrient.ENERGY)
            ));

            final boolean isViewExpanded = mDishExpandedArray.get(adapterPosition);

            if (isViewExpanded) {
                holder.mDishExpandLayout.setVisibility(View.VISIBLE);
                holder.mDishExpandButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
            } else {
                holder.mDishExpandLayout.setVisibility(View.GONE);
                holder.mDishExpandButton.setImageResource(R.drawable.ic_expand_more_black_24dp);
            }

            if (dish.getDishProductsList() != null) {
                int childCount = holder.mDishExpandProductsTableLayout.getChildCount();
                // Remove all rows except the first one
                if (childCount > 1) {
                    holder.mDishExpandProductsTableLayout.removeViews(1, childCount - 1);
                }

                for (Product product : dish.getDishProductsList()) {

                    TableRow tableRow = new TableRow(getContext());
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    tableRow.setLayoutParams(lp);

                    TextView nameTextView = new TextView(getContext());
                    TextView energyTextView = new TextView(getContext());
                    TextView weightTextView = new TextView(getContext());

                    nameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, (float) 2));
                    energyTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, (float) 1));
                    weightTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, (float) 1));

                    nameTextView.setPadding(4, 4, 4, 4);
                    energyTextView.setPadding(4, 4, 4, 4);
                    weightTextView.setPadding(4, 4, 4, 4);

                    energyTextView.setGravity(Gravity.CENTER);
                    weightTextView.setGravity(Gravity.CENTER);

                    // Prepare values to display
                    String energyToDisplay = holder.itemView.getContext().getString(
                            R.string.unit_format_calories,
                            product.getNutrientPerWeight(Nutrient.ENERGY, product.getWeight())
                    );
                    String weightToDisplay = holder.itemView.getContext().getString(
                            R.string.unit_format_weight_g, product.getWeight()
                    );


                    nameTextView.setText(product.getName());
                    energyTextView.setText(energyToDisplay);
                    weightTextView.setText(weightToDisplay);

                    tableRow.addView(nameTextView);
                    tableRow.addView(weightTextView);
                    tableRow.addView(energyTextView);

                    holder.mDishExpandProductsTableLayout.addView(tableRow);

                }

                double proteinSum = dish.getNutrientPer100g(Nutrient.PROTEIN);
                double carbsSum = dish.getNutrientPer100g(Nutrient.CARBOHYDRATE);
                double fatSum = dish.getNutrientPer100g(Nutrient.FAT);


                String dishProteinToDisplay = holder.itemView.getContext().getString(
                        R.string.unit_format_weight_floating_g_per_100g, proteinSum
                );
                String dishCarbsToDisplay = holder.itemView.getContext().getString(
                        R.string.unit_format_weight_floating_g_per_100g, carbsSum
                );
                String dishFatToDisplay = holder.itemView.getContext().getString(
                        R.string.unit_format_weight_floating_g_per_100g, fatSum
                );

                holder.mDishProteinTextView.setText(dishProteinToDisplay);
                holder.mDishCarboTextView.setText(dishCarbsToDisplay);
                holder.mDishFatTextView.setText(dishFatToDisplay);

            }

            // Set up listener to menu button
            holder.mDishMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Display PopupMenu with Edit and Delete items
                    PopupMenu popup = new PopupMenu(getContext(), view);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.dish_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_edit:
                                    onDishButtonsClick(adapterPosition, BUTTON_EDIT, dish, isViewExpanded);
                                    break;

                                case R.id.menu_delete:
                                    mDishList.remove(adapterPosition);
                                    mDishExpandedArray.remove(adapterPosition);
                                    mDishAdapter.notifyItemRemoved(adapterPosition);
                                    mDishAdapter.notifyItemRangeChanged(adapterPosition, mDishAdapter.getItemCount());
                                    if (mDishList.isEmpty()) showEmptyView();
                                    onDishButtonsClick(adapterPosition, BUTTON_DELETE, dish, isViewExpanded);
                                    break;
                            }
                            return false;
                        }
                    });

                    popup.show();

                }
            });

            holder.mDishExpandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Check if view is already expanded
                    if (mDishExpandedArray.get(adapterPosition)) {

                        mDishExpandedArray.set(adapterPosition, false);
                        holder.mDishExpandLayout.setVisibility(View.GONE);
                        holder.mDishExpandButton.setImageResource(R.drawable.ic_expand_more_black_24dp);

                    } else {
                        mDishExpandedArray.set(adapterPosition, true);
                        holder.mDishExpandLayout.setVisibility(View.VISIBLE);
                        holder.mDishExpandButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
                    }
                }
            });

            holder.mDishAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDishButtonsClick(adapterPosition, BUTTON_ADD, dish, isViewExpanded);
                }
            });
        }

        void setDishList(List<Dish> dishList, ArrayList<Boolean> dishExpandedArray) {

            mDishList = dishList;
            mDishExpandedArray = dishExpandedArray;
            notifyDataSetChanged();
        }

        List<Dish> getDishList() {
            return mDishList;
        }

        ArrayList<Boolean> getExpandedArray() {
            return mDishExpandedArray;
        }

        @Override
        public int getItemCount() {
            if (null == mDishList) return 0;
            return mDishList.size();
        }

    }

}

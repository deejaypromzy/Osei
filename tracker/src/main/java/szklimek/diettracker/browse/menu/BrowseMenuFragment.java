package szklimek.diettracker.browse.menu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SearchView;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import szklimek.diettracker.BaseFragment;
import szklimek.diettracker.R;
import szklimek.diettracker.browse.BrowseActivity;
import szklimek.diettracker.data.model.NutrientCategory;
import szklimek.diettracker.databinding.FragmentBrowseMenuBinding;

/**
 * Fragment which displays menu to choose category and nutrients min an max values to search
 */

public class BrowseMenuFragment extends BaseFragment implements BrowseMenuContract.View {

    NutrientsAdapter mAdapter;

    BrowseMenuContract.Presenter mPresenter;

    FragmentBrowseMenuBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentBrowseMenuBinding.inflate(
                LayoutInflater.from(getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new NutrientsAdapter();
        binding.nutrientsListView.setAdapter(mAdapter);

        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selection, long l) {
                mPresenter.onCategoryChange(selection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onSearchClick();
            }
        });

        binding.nameSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPresenter.onSearchQueryChange(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPresenter.onSearchQueryChange(newText);
                return false;
            }
        });

        setupPresenter();
    }

    @Override
    public void setupPresenter() {
        mPresenter = new BrowseMenuPresenter(
                ((BrowseActivity) getActivity()).getActivityModel(),
                this
        );
        mPresenter.start();
    }

    @Override
    public void setPresenter(BrowseMenuContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void updateNutrients(SparseArray<NutrientCategory> nutrients) {
        mAdapter.setData(nutrients);
    }

    @Override
    public void showFood() {
        ((BrowseActivity) getActivity()).showProducts();
    }

    private class NutrientsAdapter extends BaseAdapter {

        SparseArray<NutrientCategory> mNutrients;

        @Override
        public int getCount() {
            if(mNutrients == null) return 0;
            return mNutrients.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_browse_nutrient, viewGroup, false);

            NutrientCategory nutrient = mNutrients.get(position);

            CheckBox nameCheckBox = (CheckBox) itemView.findViewById(R.id.nutrient_checkbox);
            CheckBox minimumValueCheckBox = (CheckBox) itemView.findViewById(R.id.min_checkbox);
            CheckBox maximumValueCheckBox = (CheckBox) itemView.findViewById(R.id.max_checkbox);

            EditText minimumValueEditText = (EditText) itemView.findViewById(R.id.min_edit_text);
            EditText maximumValueEditText = (EditText) itemView.findViewById(R.id.max_edit_text);

            nameCheckBox.setChecked(nutrient.isChecked());
            minimumValueCheckBox.setChecked(nutrient.isMinimumValueChecked());
            maximumValueCheckBox.setChecked(nutrient.isMaximumValueChecked());
            minimumValueEditText.setText(String.valueOf(nutrient.getMinValue()));
            maximumValueEditText.setText(String.valueOf(nutrient.getMaxValue()));

            nameCheckBox.setText(getString
                    (R.string.browse_menu_nutrient_per_100g, nutrient.getName()));

            nameCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    mPresenter.onNutrientStateChange(position, isChecked);
                }
            });

            minimumValueCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    mPresenter.onNutrientMinValueStateChange(position, isChecked);
                }
            });

            maximumValueCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    mPresenter.onNutrientMaxValueStateChange(position, isChecked);
                }
            });

            minimumValueEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
                    if(action == EditorInfo.IME_ACTION_DONE){
                        mPresenter.onNutrientMinValueChange(
                                position,
                                Double.parseDouble(textView.getText().toString()));
                    }
                    hideSoftInput(textView);
                    return false;

                }
            });

            maximumValueEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
                    if(action == EditorInfo.IME_ACTION_DONE){
                        mPresenter.onNutrientMaxValueChange(
                                position,
                                Double.parseDouble(textView.getText().toString()));
                    }
                    hideSoftInput(textView);
                    return false;
                }
            });

            return itemView;
        }

        private void hideSoftInput(View view) {
            InputMethodManager imm = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }

        public void setData(SparseArray<NutrientCategory> nutrients){
            mNutrients = nutrients;
            notifyDataSetChanged();
        }

    }

}

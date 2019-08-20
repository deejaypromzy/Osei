package szklimek.diettracker.diet.meals;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import szklimek.diettracker.data.model.BaseMeal;
import szklimek.diettracker.R;
import szklimek.diettracker.databinding.FragmentDietMealsBinding;
import szklimek.diettracker.diet.DietActivity;
import szklimek.diettracker.diet.DietContract;

/**
 * Fragment with meals setting such as name and daily calories ratio per meal
 */

public class MealsFragment extends Fragment implements MealsContract.View {

    MealsContract.Presenter mPresenter;

    FragmentDietMealsBinding binding;

    MealsAdapter mMealsAdapter;
    LinearLayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDietMealsBinding.inflate(
                LayoutInflater.from(getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new MealsPresenter(
                ((DietActivity) getActivity()).getModel(), // Activity Model
                this // View
        );

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onBackClicked();
            }
        });

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onNextClicked();
            }
        });

        mMealsAdapter = new MealsAdapter();
        mLayoutManager = new LinearLayoutManager(getContext());
        binding.mealsRecyclerView.setLayoutManager(mLayoutManager);
        binding.mealsRecyclerView.setHasFixedSize(true);
        binding.mealsRecyclerView.setAdapter(mMealsAdapter);

        mPresenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void setPresenter(MealsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateMealsView(ArrayList<BaseMeal> meals, int totalCalories, int caloriesLimit) {

        binding.caloriesValue.setText(getString(R.string.unit_format_calories_int,
                totalCalories));
        binding.caloriesLimit.setText(getString(R.string.unit_format_calories_int,
                caloriesLimit));

        mMealsAdapter.setData(meals, caloriesLimit);
    }

    @Override
    public void updateMealCalories(int position, int caloriesPercent, int mealCalories, int totalMealsCalories) {

        binding.caloriesValue.setText(getString(R.string.unit_format_calories_int,
                totalMealsCalories));

        MealsAdapter.MealViewHolder mealView =
                (MealsAdapter.MealViewHolder) binding.mealsRecyclerView.findViewHolderForAdapterPosition(position);

        mealView.caloriesSeekBar.setProgress(caloriesPercent);
        mealView.caloriesNumberTextView
                .setText(getString(R.string.unit_format_calories_int,
                        mealCalories));
        mealView.caloriesDailyPercentTextView
                .setText(getString(R.string.unit_format_percent,
                        caloriesPercent));

        mMealsAdapter.updateMealCalories(position, caloriesPercent);
    }

    @Override
    public void updateMealName(int position, int nameId, @Nullable String name) {
        mMealsAdapter.updateMealName(position, nameId, name);
    }

    @Override
    public void addMealToList(int position) {
        mMealsAdapter.notifyItemInserted(position);
    }

    @Override
    public void removeMealFromList(int position) {
        mMealsAdapter.notifyItemRemoved(position);
    }

    @Override
    public void showNextFragment() {
        ((DietContract.View) getActivity()).onNavButtonClicked(DietActivity.BUTTON_NEXT);
    }

    @Override
    public void showPreviousFragment() {
        ((DietContract.View) getActivity()).onNavButtonClicked(DietActivity.BUTTON_BACK);
    }

    /**
     * Adapter which create and populate Meals
     */
    private class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealViewHolder> {
        ArrayList<BaseMeal> mMeals;
        int mDailyCaloriesLimit;

        /**
         * ViewHolder class
         */
        class MealViewHolder extends RecyclerView.ViewHolder {
            TextView numberTextView;
            Spinner nameSpinner;
            TextInputLayout nameInputLayout;
            EditText nameEditText;
            SeekBar caloriesSeekBar;
            TextView caloriesNumberTextView;
            TextView caloriesDailyPercentTextView;
            ConstraintLayout constraintLayout;

            MealViewHolder(View itemView) {
                super(itemView);
                numberTextView = (TextView) itemView.findViewById(R.id.meal_number);
                nameSpinner = (Spinner) itemView.findViewById(R.id.name_spinner);
                nameEditText = (EditText) itemView.findViewById(R.id.edit_text);
                nameInputLayout = (TextInputLayout) itemView.findViewById(R.id.name_input_layout);
                caloriesSeekBar = (SeekBar) itemView.findViewById(R.id.calories_seekbar);
                caloriesNumberTextView = (TextView) itemView.findViewById(R.id.calories_value);
                caloriesDailyPercentTextView =
                        (TextView) itemView.findViewById(R.id.calories_percent);
                constraintLayout =
                        (ConstraintLayout) itemView.findViewById(R.id.constraint_layout);
            }
        }

        @Override
        public MealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layoutId = R.layout.item_meal_diet;
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new MealsAdapter.MealViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MealViewHolder holder, int position) {
            final int adapterPosition = holder.getAdapterPosition();
            final BaseMeal meal = mMeals.get(adapterPosition);

            holder.numberTextView.setText(String.valueOf(meal.getNumber()));
            holder.nameSpinner.setSelection(meal.getNameId());
            holder.caloriesSeekBar.setProgress(meal.getPercentDailyLimit());
            holder.caloriesNumberTextView
                    .setText(getString(R.string.unit_format_calories_int,
                            meal.getMealCaloriesLimit(mDailyCaloriesLimit)));
            holder.caloriesDailyPercentTextView
                    .setText(getString(R.string.unit_format_percent,
                            meal.getPercentDailyLimit()));

            holder.caloriesSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser) mPresenter.onMealCaloriesChanged(adapterPosition, progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            holder.nameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int selection, long l) {
                    mPresenter.onMealNameChanged(adapterPosition, selection);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            holder.nameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
                    if(action == EditorInfo.IME_ACTION_DONE){
                        mPresenter.onMealNameInputChanged(
                                adapterPosition,
                                textView.getText().toString());
                        hideSoftInput(textView);
                    }
                    return false;
                }
            });

            if(meal.getNameId() != BaseMeal.TYPE_OTHER) hideInputLayout(holder);
            else {
                showInputLayout(holder);
                if(meal.getName() != null) holder.nameEditText.setText(meal.getName());
            }
        }

        private void hideInputLayout(MealViewHolder holder){

            ViewGroup.LayoutParams layoutParams = holder.nameSpinner.getLayoutParams();
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(holder.constraintLayout);

            layoutParams.width = 0;
            holder.nameSpinner.setLayoutParams(layoutParams);
            constraintSet.connect(
                    R.id.name_spinner, ConstraintSet.LEFT,
                    R.id.calories_seekbar, ConstraintSet.LEFT);
            constraintSet.applyTo(holder.constraintLayout);
        }

        private void showInputLayout(MealViewHolder holder){

            ViewGroup.LayoutParams layoutParams = holder.nameSpinner.getLayoutParams();
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(holder.constraintLayout);

            constraintSet.clone(holder.constraintLayout);
            layoutParams.width = 110;
            holder.nameSpinner.setLayoutParams(layoutParams);
            constraintSet.clear(R.id.name_spinner, ConstraintSet.LEFT);
            constraintSet.applyTo(holder.constraintLayout);


        }

        private void hideSoftInput(View view) {
            InputMethodManager imm = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }

        @Override
        public int getItemCount() {
            if(mMeals == null) return 0;
            return mMeals.size();
        }

        void setData(ArrayList<BaseMeal> meals, int caloriesLimit) {
            mMeals = meals;
            mDailyCaloriesLimit = caloriesLimit;
            notifyDataSetChanged();
        }

        void updateMealName(int position, int nameId, @Nullable String name){
            mMeals.get(position).setNameId(nameId);
            mMeals.get(position).setName(name);
            notifyItemChanged(position);
        }

        void updateMealCalories(int position, int caloriesPercent){
            mMeals.get(position).setPercentDailyLimit(caloriesPercent);
        }

    }
}

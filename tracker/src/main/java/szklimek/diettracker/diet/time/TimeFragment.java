package szklimek.diettracker.diet.time;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import szklimek.diettracker.data.model.BaseMeal;
import szklimek.diettracker.R;
import szklimek.diettracker.Utilities;
import szklimek.diettracker.databinding.FragmentDietTimeBinding;
import szklimek.diettracker.diet.DietActivity;
import szklimek.diettracker.diet.DietContract;

/**
 * Fragment with meals time setting
 */

public class TimeFragment extends Fragment implements TimeContract.View {

    TimeContract.Presenter mPresenter;

    FragmentDietTimeBinding binding;

    MealsAdapter mMealsAdapter;
    LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDietTimeBinding.inflate(
                LayoutInflater.from(getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new TimePresenter(
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
    public void setPresenter(TimeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateMealsView(ArrayList<BaseMeal> meals) {
        mMealsAdapter.setData(meals);
    }

    @Override
    public void updateMeal(int position, BaseMeal meal) {
        mMealsAdapter.updateMeal(position, meal);
    }

    @Override
    public void showTimePicker(int position,
                               int currentHour, int currentMinutes,
                               int afterHour, int afterMinutes,
                               int beforeHour, int beforeMinutes) {

        ((DietContract.View) getActivity()).
                showTimePicker(
                        position, // BaseMeal position in list
                        currentHour, currentMinutes, // Current BaseMeal time
                        afterHour, afterMinutes, // Time of the previous meal (if there is)
                        beforeHour, beforeMinutes // Time of the next meal (if there is)
                );

    }

    @Override
    public void showPreviousFragment() {
        ((DietContract.View) getActivity()).onNavButtonClicked(DietActivity.BUTTON_BACK);
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

    /**
     * Adapter which populate list of meals with possibility to set time of meal
     */
    private class MealsAdapter
            extends RecyclerView.Adapter<MealsAdapter.MealTimeViewHolder> {

        private ArrayList<BaseMeal> mMeals;

        /**
         * ViewHolder class
         */
        class MealTimeViewHolder extends RecyclerView.ViewHolder {
            TextView mealNumberTextView;
            TextView mealNameTextView;
            TextInputEditText mealTimeEditText;


            MealTimeViewHolder(View itemView) {
                super(itemView);
                mealNumberTextView = (TextView) itemView.findViewById(R.id.meal_number);
                mealNameTextView = (TextView) itemView.findViewById(R.id.meal_name);
                mealTimeEditText = (TextInputEditText) itemView.findViewById(R.id.meal_time_edit);
            }
        }

        @Override
        public MealTimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layoutId = R.layout.item_meal_diet_time;
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new MealTimeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MealTimeViewHolder holder, int position) {
            BaseMeal meal = mMeals.get(holder.getAdapterPosition());
            final int adapterPosition = holder.getAdapterPosition();
            String name = meal.getNameId() == BaseMeal.TYPE_OTHER ?
                    meal.getName() : getString(Utilities.getMealNameByNameId(meal.getNameId()));
            holder.mealNameTextView.setText(name);
            holder.mealNumberTextView.setText(String.valueOf(meal.getNumber()));
            holder.mealTimeEditText.setText(
                    Utilities.getTimeToDisplay(
                            meal.getTimeHour(),
                            meal.getTimeMinutes()));

            holder.mealTimeEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.onMealTimeClicked(adapterPosition);
                }
            });

        }

        @Override
        public int getItemCount() {
            if (mMeals == null) return 0;
            return mMeals.size();
        }

        public void setData(ArrayList<BaseMeal> meals) {
            mMeals = meals;
            notifyDataSetChanged();
        }

        void updateMeal(int position, BaseMeal meal){
            mMeals.remove(position);
            mMeals.add(position, meal);
            notifyItemChanged(position);
        }
    }
}

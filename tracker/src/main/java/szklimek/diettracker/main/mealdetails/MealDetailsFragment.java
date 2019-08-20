package szklimek.diettracker.main.mealdetails;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import szklimek.diettracker.BaseFragment;
import szklimek.diettracker.R;
import szklimek.diettracker.Utilities;
import szklimek.diettracker.data.model.BaseMeal;
import szklimek.diettracker.data.model.Food;
import szklimek.diettracker.data.model.Meal;
import szklimek.diettracker.data.model.Nutrient;
import szklimek.diettracker.data.model.Product;
import szklimek.diettracker.databinding.FragmentMainMealsDetailsBinding;
import szklimek.diettracker.databinding.ItemMainDetailsFoodBinding;
import szklimek.diettracker.databinding.ItemMainDetailsNutrientBinding;
import szklimek.diettracker.main.MainActivity;

/**
 * Fragment which display nutrient summary about whole food added to specified meal
 */

public class MealDetailsFragment extends BaseFragment implements MealDetailsContract.View {

    MealDetailsContract.Presenter mPresenter;

    FragmentMainMealsDetailsBinding binding;

    FoodAdapter mFoodAdapter;

    NutrientsAdapter mNutrientsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainMealsDetailsBinding.inflate(
                LayoutInflater.from(getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void setPresenter(MealDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setupPresenter() {
        mPresenter = new MealDetailsPresenter(
                ((MainActivity) getActivity()).getActivityModel(),
                this
        );

        mPresenter.start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFoodAdapter = new FoodAdapter();
        mNutrientsAdapter = new NutrientsAdapter();

        binding.foodRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.foodRecyclerView.setHasFixedSize(true);
        binding.foodRecyclerView.setNestedScrollingEnabled(false);
        binding.foodRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        binding.foodRecyclerView.setAdapter(mFoodAdapter);

        binding.nutrientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.nutrientsRecyclerView.setHasFixedSize(true);
        binding.nutrientsRecyclerView.setNestedScrollingEnabled(false);
        binding.nutrientsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        binding.nutrientsRecyclerView.setAdapter(mNutrientsAdapter);

        setupPresenter();
    }

    @Override
    public void showMealData(Meal meal) {
        if(meal.getNameId() == BaseMeal.TYPE_OTHER) binding.mealName.setText(meal.getName());
        else binding.mealName.setText(Utilities.getMealNameByNameId(meal.getNameId()));

        binding.mealCaloriesDailyLimit.setText(getString(R.string.unit_format_calories_per_calories,
                        (int) meal.getTotalNutrientValue(Nutrient.ENERGY),
                        meal.getCaloriesLimit()));

        binding.mealCaloriesProgress.setProgress(meal.getCaloriesUsedPercent());
        binding.mealCaloriesPercentValue.setText(getString(
                R.string.unit_format_percent, meal.getCaloriesUsedPercent()));


        mFoodAdapter.setFoods(meal.getFoods());

        mNutrientsAdapter.setMeal(meal);

    }

    private class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

        List<Food> mFoods;

        class FoodViewHolder extends RecyclerView.ViewHolder {
            private ItemMainDetailsFoodBinding binding;

            FoodViewHolder(View itemView){
                super(itemView);
                binding = DataBindingUtil.bind(itemView);
            }

            public ItemMainDetailsFoodBinding getBinding() {
                return binding;
            }

        }

        @Override
        public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_main_details_food, parent, false);
            return new FoodViewHolder(v);
        }

        @Override
        public void onBindViewHolder(FoodViewHolder holder, int position) {
            final int adapterPosition = holder.getAdapterPosition();
            final Food food = mFoods.get(adapterPosition);
            holder.getBinding().foodName.setText(food.getName());

            holder.getBinding().foodType.setText(getString(
                    Utilities.getFoodTypeString(food.getFoodType())));

            holder.getBinding().foodEnergy.setText(getString(
                    R.string.unit_format_calories,
                    food.getNutrient(Nutrient.ENERGY)));

            holder.getBinding().foodWeight.setText(getString(
                    R.string.unit_format_weight_g, food.getWeight()));

            holder.getBinding().foodOptionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("MealsDetails", "clicked position " + adapterPosition + " food name " + food.getName());
                }
            });
        }

        @Override
        public int getItemCount() {
            if(mFoods == null) return 0;
            return mFoods.size();
        }

        public void setFoods(List<Food> foods){
            mFoods = foods;
        }
    }

    private class NutrientsAdapter extends RecyclerView.Adapter<NutrientsAdapter.NutrientViewHolder> {

        Meal mMeal;

        class NutrientViewHolder extends RecyclerView.ViewHolder {
            private ItemMainDetailsNutrientBinding binding;

            NutrientViewHolder(View itemView){
                super(itemView);
                binding = DataBindingUtil.bind(itemView);
            }

            public ItemMainDetailsNutrientBinding getBinding() {
                return binding;
            }

        }

        @Override
        public NutrientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_main_details_nutrient, parent, false);
            return new NutrientViewHolder(v);
        }

        @Override
        public void onBindViewHolder(NutrientViewHolder holder, int position) {
            final int adapterPosition = holder.getAdapterPosition();

            holder.getBinding().nutrientName.setText(
                            Utilities.getStringNutrientType(getContext(),
                            Nutrient.NUTRIENTS_IDS[adapterPosition]));

            holder.getBinding().nutrientWeight.setText(getString(R.string.unit_format_weight_floating_g,
                    mMeal.getTotalNutrientValue(Nutrient.NUTRIENTS_IDS[adapterPosition])));


        }

        @Override
        public int getItemCount() {
            if(mMeal == null) return 0;
            return Nutrient.NUTRIENT_COLUMNS.size();
        }

        public void setMeal(Meal meal){
            mMeal = meal;
        }
    }

}

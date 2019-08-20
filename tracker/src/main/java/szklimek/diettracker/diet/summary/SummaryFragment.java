package szklimek.diettracker.diet.summary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import szklimek.diettracker.data.model.BaseMeal;
import szklimek.diettracker.R;
import szklimek.diettracker.Utilities;
import szklimek.diettracker.data.model.Diet;
import szklimek.diettracker.databinding.FragmentDietSummaryBinding;
import szklimek.diettracker.diet.DietActivity;
import szklimek.diettracker.diet.DietContract;

/**
 * Fragment containing summary of diet plan created by user
 */

public class SummaryFragment extends Fragment implements SummaryContract.View {

    SummaryContract.Presenter mPresenter;

    FragmentDietSummaryBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDietSummaryBinding.inflate(
                LayoutInflater.from(getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new SummaryPresenter(
                ((DietActivity) getActivity()).getModel(), // Activity Model
                this // View
        );

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onBackClicked();
            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onSaveClicked();
            }
        });

        mPresenter.start();
    }

    @Override
    public void setPresenter(SummaryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateView(Diet diet) {

        binding.caloriesDailyLimitValue.setText(getString(R.string.unit_format_calories_int,
                diet.getDailyCaloriesLimit()));

        binding.proteinPercent.setText(getString(R.string.unit_format_percent, diet.getProteinRatio()));
        binding.proteinWeight.setText(getString(R.string.unit_format_weight_g, diet.getProteinWeight()));

        binding.carboPercent.setText(getString(R.string.unit_format_percent, diet.getCarboRatio()));
        binding.carboWeight.setText(getString(R.string.unit_format_weight_g, diet.getCarboWeight()));

        binding.fatPercent.setText(getString(R.string.unit_format_percent, diet.getFatRatio()));
        binding.fatWeight.setText(getString(R.string.unit_format_weight_g, diet.getFatWeight()));

        for(BaseMeal meal : diet.getMeals()){
            TableRow tableRow = new TableRow(getContext());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tableRow.setLayoutParams(lp);

            TextView nameTextView = new TextView(getContext());
            TextView caloriesTextView = new TextView(getContext());
            TextView timeTextView = new TextView(getContext());

            nameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, (float) 1));
            caloriesTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, (float) 1));
            timeTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, (float) 1));

            int defaultPadding = (int) getResources().getDimension(R.dimen.meals_table_default_padding);
            nameTextView.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
            caloriesTextView.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
            timeTextView.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);

            caloriesTextView.setGravity(Gravity.CENTER);
            timeTextView.setGravity(Gravity.CENTER);

            String mealName;
            if(meal.getNameId() == BaseMeal.TYPE_OTHER) mealName = meal.getName();
            else mealName = getString(Utilities.getMealNameByNameId(meal.getNameId()));

            nameTextView.setText(mealName);
            caloriesTextView.setText(getString(
                    R.string.unit_format_calories_int, meal.getCaloriesLimit()
            ));
            timeTextView.setText(
                    Utilities.getTimeToDisplay(meal.getTimeHour(), meal.getTimeMinutes())
            );

            tableRow.addView(timeTextView);
            tableRow.addView(nameTextView);
            tableRow.addView(caloriesTextView);

            binding.mealsTable.addView(tableRow);
        }
    }

    @Override
    public void showPreviousFragment() {
        ((DietContract.View) getActivity()).onNavButtonClicked(DietActivity.BUTTON_BACK);
    }

    @Override
    public void saveDiet() {
        ((DietContract.View) getActivity()).onNavButtonClicked(DietActivity.BUTTON_SAVE);
    }
}

package szklimek.diettracker.reusablefragments.adddialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import szklimek.diettracker.createeditdish.DishActivity;
import szklimek.diettracker.data.model.Food;
import szklimek.diettracker.R;
import szklimek.diettracker.Utilities;
import szklimek.diettracker.addfood.AddActivity;
import szklimek.diettracker.data.model.Nutrient;
import szklimek.diettracker.databinding.DialogAddFoodBinding;

/**
 * Dialog which is displayed after user clicked on Add button, enables to select weight of Food
 * to add either to used food or to dish
 */

public class AddFoodDialogFragment extends DialogFragment implements
        AddFoodDialogContract.View {

    public static final String ADD_FOOD_DIALOG = "add_dish_dialog";

    AddFoodDialogPresenter mPresenter;

    DialogAddFoodBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddFoodBinding.inflate(
                LayoutInflater.from(getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity() instanceof AddActivity){
            mPresenter = new AddFoodDialogPresenter(
                    ((AddActivity) getActivity()).getActivityModel(), // Activity Model
                    this // View
            );
        } else if (getActivity() instanceof DishActivity) {
            mPresenter = new AddFoodDialogPresenter(
                    ((DishActivity) getActivity()).getActivityModel(), // Activity Model
                    this // View
            );
        }


        binding.amountSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // Unable user to set progress to 0
                if (progress == 0) {
                    binding.amountSeekbar.setProgress(1);
                    return;
                }
                mPresenter.onSeekBarProgressChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing here
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing here
            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {mPresenter.onSaveClicked();}
        });
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {mPresenter.onCancelClicked();}
        });
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {mPresenter.onDeleteClicked();}
        });

        mPresenter.start();
    }

    @Override
    public void setPresenter(AddFoodDialogPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter = null;
    }

    @Override
    public void notifySaveDismissed() {
        if(getActivity() instanceof DialogSaveDismissedCallback){
            ((AddFoodDialogContract.View.DialogSaveDismissedCallback) getActivity()).onDialogSaveDismissed();
        } else throw new RuntimeException("Activity must implement DialogSaveDismissedCallback.");
    }

    @Override
    public void updateFoodValuesView(Food food, int foodWeight) {
        binding.amountValue.setText(getString
                (R.string.unit_format_weight_g,
                        foodWeight));
        binding.caloriesPerWeight.setText(getString(
                R.string.unit_format_calories,
                food.getNutrientPerWeight(Nutrient.ENERGY, foodWeight)));
    }

    @Override
    public void updateFoodData(Food food, int seekbarProgress) {
        binding.foodName.setText(food.getName());
        binding.foodDescription.setText(food.getDescription());
        if(food.getFoodType() == Food.FOOD_DISH)
            binding.foodType.setText(Utilities.getStringDishType(getContext(), food.getType()));
        else
            binding.foodType.setText(Utilities.getStringProductType(getContext(), food.getType()));

        binding.amountSeekbar.setProgress(seekbarProgress);
        binding.caloriesPer100g.setText(
                getString(R.string.unit_format_calories_per_100g,
                        food.getNutrientPer100g(Nutrient.ENERGY))
        );
    }

    @Override
    public void setAddMode() {
        binding.deleteButton.setVisibility(View.INVISIBLE);
        binding.saveButton.setText(R.string.add_dialog_button_add_label);
    }

    @Override
    public void dismissDialog() {
        dismiss();
    }
}
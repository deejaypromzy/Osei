package szklimek.diettracker.diet.general;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.NumberPicker;
import android.widget.TextView;

import szklimek.diettracker.R;
import szklimek.diettracker.databinding.FragmentDietGeneralBinding;
import szklimek.diettracker.diet.DietActivity;
import szklimek.diettracker.diet.DietContract;

/**
 * Fragment with calories limit and meals number setting
 */

public class GeneralFragment extends Fragment implements GeneralContract.View {

    GeneralContract.Presenter mPresenter;

    FragmentDietGeneralBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentDietGeneralBinding.inflate(
                LayoutInflater.from(getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new GeneralPresenter(
                ((DietActivity) getActivity()).getModel(), // Activity Model
                this // View
        );

        binding.caloriesEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    mPresenter.onCaloriesLimitChanged(textView.getText().toString());
                }
                return true;
            }
        });

        binding.nrPicker.setMinValue(1);
        binding.nrPicker.setMaxValue(9);
        binding.nrPicker.setWrapSelectorWheel(false);
        binding.nrPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                mPresenter.onMealsNumberChanged(newValue);
            }
        });

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onNextClicked();
            }
        });

        mPresenter.start();
    }

    @Override
    public void setPresenter(GeneralPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showStartValues(int caloriesLimit, int mealsNumber) {
        binding.caloriesEditText.setText(String.valueOf(caloriesLimit));
        binding.nrPicker.setValue(mealsNumber);
    }

    @Override
    public void showCaloriesError() {
        binding.caloriesInputLayout.setErrorEnabled(true);
        binding.caloriesInputLayout.setError(getString(R.string.fragment_general_error_calories));
    }

    @Override
    public void hideCaloriesError() {
        binding.caloriesInputLayout.setErrorEnabled(false);
    }

    @Override
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager)
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        getView().clearFocus();
    }

    @Override
    public void showNextFragment() {
        ((DietContract.View) getActivity()).onNavButtonClicked(DietActivity.BUTTON_NEXT);
    }

}

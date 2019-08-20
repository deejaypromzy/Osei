package szklimek.diettracker.diet.ratio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import szklimek.diettracker.data.model.Diet;
import szklimek.diettracker.R;
import szklimek.diettracker.databinding.FragmentDietRatioBinding;
import szklimek.diettracker.diet.DietActivity;
import szklimek.diettracker.diet.DietContract;

/**
 * Fragment with protein, carbohydrates and fat ratio per calories limit
 */

public class RatioFragment extends Fragment implements RatioContract.View {

    RatioContract.Presenter mPresenter;

    FragmentDietRatioBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDietRatioBinding.inflate(
                LayoutInflater.from(getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new RatioPresenter(
                ((DietActivity) getActivity()).getModel(), // Activity Model
                this // View
        );

        binding.proteinSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) mPresenter.onRatioChanged(Diet.PROTEIN_PERCENT, progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        binding.carboSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) mPresenter.onRatioChanged(Diet.CARBO_PERCENT, progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        binding.fatSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) mPresenter.onRatioChanged(Diet.FAT_PERCENT, progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

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


        mPresenter.start();
    }

    @Override
    public void setPresenter(RatioContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateView(int type, int caloriesLimit, int totalWeight, int percentSum, int progress, int calories, int weight) {

        binding.percentSumValue.setText(getString(R.string.unit_format_percent,
                percentSum));

        binding.weightValue.setText(getString(R.string.unit_format_weight_g,
                totalWeight));

        switch (type){
            case Diet.PROTEIN_PERCENT:
                binding.proteinSeekbar.setProgress(progress);
                binding.proteinPercent
                        .setText(getString(R.string.unit_format_percent,
                                progress));
                binding.proteinCalories
                        .setText(getString(R.string.fragment_ratio_calories_format,
                                calories,
                                caloriesLimit));
                binding.proteinWeight
                        .setText(getString(R.string.unit_format_weight_g,
                                weight));
                break;

            case Diet.CARBO_PERCENT:
                binding.carboSeekbar.setProgress(progress);
                binding.carboPercent
                        .setText(getString(R.string.unit_format_percent,
                                progress));
                binding.carboCalories
                        .setText(getString(R.string.fragment_ratio_calories_format,
                                calories,
                                caloriesLimit));
                binding.carboWeight
                        .setText(getString(R.string.unit_format_weight_g,
                                weight));
                break;

            case Diet.FAT_PERCENT:
                binding.fatSeekbar.setProgress(progress);
                binding.fatPercent
                        .setText(getString(R.string.unit_format_percent,
                                progress));
                binding.fatCalories
                        .setText(getString(R.string.fragment_ratio_calories_format,
                                calories,
                                caloriesLimit));
                binding.fatWeight
                        .setText(getString(R.string.unit_format_weight_g,
                                weight
                        ));
                break;
        }
    }

    @Override
    public void showPreviousFragment() {
        ((DietContract.View) getActivity()).onNavButtonClicked(DietActivity.BUTTON_BACK);
    }

    @Override
    public void showNextFragment() {
        ((DietContract.View) getActivity()).onNavButtonClicked(DietActivity.BUTTON_NEXT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}

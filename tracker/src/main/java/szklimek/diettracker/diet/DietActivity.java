package szklimek.diettracker.diet;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import szklimek.diettracker.R;
import szklimek.diettracker.data.local.SharedPreferencesManager;
import szklimek.diettracker.data.local.SharedPreferencesSource;
import szklimek.diettracker.databinding.ActivityDietBinding;
import szklimek.diettracker.diet.general.GeneralFragment;
import szklimek.diettracker.diet.meals.MealsFragment;
import szklimek.diettracker.diet.ratio.RatioFragment;
import szklimek.diettracker.diet.summary.SummaryFragment;
import szklimek.diettracker.reusablefragments.TimePickerFragment;
import szklimek.diettracker.diet.time.TimeFragment;

/**
 * Activity which enables user to create or edit his/her diet plan
 */

public class DietActivity extends AppCompatActivity implements
        DietContract.View,
        TimePickerFragment.OnTimeSetFromAdapterListener {

    public static final int BUTTON_BACK = 0;
    public static final int BUTTON_NEXT = 1;
    public static final int BUTTON_SAVE = 2;

    public static final int FRAGMENT_GENERAL = 0;
    public static final int FRAGMENT_RATIO = 1;
    public static final int FRAGMENT_MEALS = 2;
    public static final int FRAGMENT_TIME = 3;

    public static final String FRAGMENT_SUMMARY_TAG = "summary";

    public static final String TIME_PICKER_DIALOG_KEY = "time_dialog";

    SharedPreferencesSource mSharedPreferencesManager;
    DietModel mModel;
    DietContract.Presenter mPresenter;

    ActivityDietBinding binding;
    PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diet);

        // Set up presenter
        mSharedPreferencesManager = SharedPreferencesManager.getInstance(
                getSharedPreferences(
                        getString(R.string.sh_pref_user_diet_file_key),
                        MODE_PRIVATE)
        );

        mModel = DietModel.getInstance(mSharedPreferencesManager);

        mPresenter = new DietPresenter(mModel, this);

        // Setup adapter
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(mPagerAdapter);
        binding.viewPager.setAdapter(mPagerAdapter);
        binding.viewPager.setOffscreenPageLimit(4);

        // Setup pager transformer
        ViewPager.PageTransformer mPagerTransformer = new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                final float MIN_SCALE = 0.85f;
                final float MIN_ALPHA = 0.5f;

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0);
                    view.setRotation(0);

                } else if (position <= 1) { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));

                    // Scale the page down (between MIN_SCALE and 1)
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                    // Fade the page relative to its size.
                    view.setAlpha(MIN_ALPHA +
                            (scaleFactor - MIN_SCALE) /
                                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);
                }
            }
        };

        binding.viewPager.setPageTransformer(true, mPagerTransformer);

        mPresenter.start();
    }

    @Override
    public void setPresenter(DietContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showPreviousFragment() {
        binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() - 1, true);
    }

    @Override
    public void showNextFragment() {
        binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1, true);
    }

    @Override
    public void showSummary() {
        Fragment summaryFragment = new SummaryFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, summaryFragment, FRAGMENT_SUMMARY_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void hideSummary() {
        Fragment summaryFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_SUMMARY_TAG);
        getSupportFragmentManager().beginTransaction()
                .hide(summaryFragment)
                .commit();
    }

    @Override
    public void showRatioError() {
        Toast toast = Toast.makeText(
                getApplicationContext(),
                getString(R.string.diet_error_zero_calories_percent),
                Toast.LENGTH_SHORT);
        toast.show();
        binding.viewPager.setCurrentItem(FRAGMENT_RATIO);

    }

    @Override
    public void showMealCaloriesSumError() {
        Toast toast = Toast.makeText(
                getApplicationContext(),
                getString(R.string.diet_error_daily_calories_not_full),
                Toast.LENGTH_SHORT);
        toast.show();
        binding.viewPager.setCurrentItem(FRAGMENT_MEALS);
    }

    @Override
    public void showMealCaloriesError() {
        Toast toast = Toast.makeText(
                getApplicationContext(),
                getString(R.string.diet_error_zero_calories_percent),
                Toast.LENGTH_SHORT);
        toast.show();
        binding.viewPager.setCurrentItem(FRAGMENT_MEALS);
    }

    @Override
    public void showMealNameError() {
        Toast toast = Toast.makeText(
                getApplicationContext(),
                getString(R.string.diet_error_no_custom_name),
                Toast.LENGTH_SHORT);
        toast.show();
        binding.viewPager.setCurrentItem(FRAGMENT_MEALS);
    }

    @Override
    public void hideViewPager() {
        binding.viewPager.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showViewPager() {
        binding.viewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSuccessAndFinish() {
        Toast toast = Toast.makeText(
                getApplicationContext(),
                getString(R.string.diet_plan_saved_toast),
                Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }

    @Override
    public void onNavButtonClicked(int buttonType) {
        switch (buttonType) {
            case BUTTON_BACK:
                mPresenter.onBackClicked();
                return;
            case BUTTON_NEXT:
                mPresenter.onNextClicked(binding.viewPager.getCurrentItem());
                return;
            case BUTTON_SAVE:
                mPresenter.onSaveClicked();
        }
    }

    @Override
    public void showTimePicker(int position,
                               int currentHour, int currentMinutes,
                               int afterHour, int afterMinutes,
                               int beforeHour, int beforeMinutes) {
        Bundle args = new Bundle();
        args.putInt(TimePickerFragment.HOUR_KEY, currentHour);
        args.putInt(TimePickerFragment.MINUTES_KEY, currentMinutes);
        args.putInt(TimePickerFragment.POSITION_KEY, position);

        // If it is not first meal unable user to set meal time earlier than previous meal

        args.putInt(TimePickerFragment.AFTER_HOUR, afterHour);
        args.putInt(TimePickerFragment.AFTER_MINUTES, afterMinutes);


        // If it is not the last meal unable user to set meal time later than the next meal

        args.putInt(TimePickerFragment.BEFORE_HOUR, beforeHour);
        args.putInt(TimePickerFragment.BEFORE_MINUTES, beforeMinutes);

        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(args);
        timePickerFragment.show(getSupportFragmentManager(), TIME_PICKER_DIALOG_KEY);
    }

    @Override
    public void onTimeSetFromAdapter(TimePicker timePicker, int hour, int minutes, int adapterPosition) {
        mModel.updateMealTime(adapterPosition, hour, minutes);
    }

    public DietModel getModel() {
        return mModel;
    }

    /**
     * A simple pager adapter that represents fragments in create object activity, in
     * sequence.
     */
    private class PagerAdapter extends FragmentPagerAdapter {

        private int[] imageResId = {
                R.drawable.ic_tab_1,
                R.drawable.ic_tab_2,
                R.drawable.ic_tab_3,
                R.drawable.ic_tab_4
        };

        private CharSequence[] tabTitles = {
                getString(R.string.diet_general_tab_label),
                getString(R.string.diet_ratio_tab_label),
                getString(R.string.diet_meals_tab_label),
                getString(R.string.diet_time_tab_label)
        };

        @Override
        public CharSequence getPageTitle(int position) {

            // Add icon in tabItem text
            Drawable icon =
                    ContextCompat.getDrawable(getApplicationContext(), imageResId[position]);
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            // Replace blank spaces with image icon
            SpannableString sb = new SpannableString("   \n" + tabTitles[position]);
            ImageSpan imageSpan = new ImageSpan(getApplicationContext(), imageResId[position]);
            sb.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new GeneralFragment();
                case 1:
                    return new RatioFragment();
                case 2:
                    return new MealsFragment();
                case 3:
                    return new TimeFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}

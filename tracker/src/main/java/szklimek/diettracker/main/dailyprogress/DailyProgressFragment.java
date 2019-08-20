package szklimek.diettracker.main.dailyprogress;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.renderer.PieChartRenderer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import szklimek.diettracker.BaseFragment;
import szklimek.diettracker.R;
import szklimek.diettracker.Utilities;
import szklimek.diettracker.data.model.BaseMeal;
import szklimek.diettracker.data.model.Diet;
import szklimek.diettracker.data.model.DietProgress;
import szklimek.diettracker.data.model.Meal;
import szklimek.diettracker.data.model.Nutrient;
import szklimek.diettracker.databinding.FragmentMainDailyBinding;
import szklimek.diettracker.databinding.FragmentMainDailyPageBinding;
import szklimek.diettracker.databinding.FragmentMainDailyPageBinding;
import szklimek.diettracker.databinding.ItemMainMealBinding;
import szklimek.diettracker.main.MainActivity;
import szklimek.diettracker.main.MainContract;
import szklimek.diettracker.main.dailyprogresspage.DailyProgressPageFragment;

/**
 * Fragment which presents food added to meals and basic nutrients statistic in connection
 * current diet plan on single day
 */

public class DailyProgressFragment extends BaseFragment implements DailyProgressContract.View {

    private DailyProgressContract.Presenter mPresenter;

    private FragmentMainDailyBinding mBinding;

    PagerAdapter mPagerAdapter;

    @Override
    public void setupPresenter() {
        mPresenter = new DailyProgressPresenter(
                ((MainActivity) getActivity()).getActivityModel(),
                this
        );
        mPresenter.start();
    }

    @Override
    public void setPresenter(DailyProgressContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMainDailyBinding.inflate(
                LayoutInflater.from(getContext()), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupPresenter();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        mPagerAdapter = new PagerAdapter(getChildFragmentManager(), calendar);
        mBinding.viewPager.setAdapter(mPagerAdapter);
        mBinding.viewPager.setAdapter(mPagerAdapter);
        mBinding.viewPager.setOffscreenPageLimit(4);
    }

    @Override
    public void showDayOfWeek(int dayOfWeek) {
        mBinding.viewPager.setCurrentItem(dayOfWeek, true);
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        Calendar mDate;

        @Override
        public CharSequence getPageTitle(int position) {
            // Page titles are based on days in week
            mDate.set(Calendar.DAY_OF_WEEK,
                    mDate.getFirstDayOfWeek() + position);
            return Utilities.getFormattedDay(getContext(), mDate);
        }

        PagerAdapter(FragmentManager fm, Calendar calendar) {
            super(fm);
            mDate = calendar;
        }

        @Override
        public Fragment getItem(int position) {

            // Set date based on positions
            // First page is always first day of week (Monday or Sunday)
            mDate.set(Calendar.DAY_OF_WEEK,
                    mDate.getFirstDayOfWeek() + position);

            // Send date via bundle object
            Bundle bundle = new Bundle();
            bundle.putLong(DailyProgressPageFragment.DATE_KEY, mDate.getTimeInMillis());

            Fragment fragment = new DailyProgressPageFragment();
            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getCount() {
            return 7;
        }
    }

}

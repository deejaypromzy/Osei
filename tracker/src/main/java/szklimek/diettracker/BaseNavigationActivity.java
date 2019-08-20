package szklimek.diettracker;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import szklimek.diettracker.browse.BrowseActivity;
import szklimek.diettracker.databinding.ActivityWithNavBinding;
import szklimek.diettracker.diet.DietActivity;

/**
 * Template for activities with navigation drawer
 */

public abstract class BaseNavigationActivity extends AppCompatActivity {

    ActivityWithNavBinding binding;

    ActionBarDrawerToggle mDrawerToggle;

    public abstract void setupPresenter();

    public abstract BaseNavigationActivityPresenter getActivityPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_with_nav);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupNavigationDrawer();
    }

    private void setupNavigationDrawer(){

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                R.string.nav_drawer_open_message,
                R.string.nav_drawer_close_message
        );
        binding.drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        final NavigationView navView = (NavigationView) binding.navDrawer.findViewById(R.id.nav_drawer_menu_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                item.setChecked(true);
                switch (item.getItemId()){
                    case R.id.menu_daily_plan:
                        getActivityPresenter().onDailyPlanMenuClicked();
                        break;

                    case R.id.menu_reports:
                        getActivityPresenter().onReportsMenuClicked();
                        break;

                    case R.id.menu_counter:
                        getActivityPresenter().onCounterMenuClicked();
                        break;

                    case R.id.menu_browse_food:
                        getActivityPresenter().onFoodBrowseMenuClicked();
                        break;

                    case R.id.menu_change_plan:
                        getActivityPresenter().onDietPlanMenuClicked();
                        break;

                    case R.id.menu_settings:
                        getActivityPresenter().onSettingsMenuClicked();
                        break;

                    case R.id.menu_help:
                        getActivityPresenter().onHelpMenuClicked();
                        break;

                    case R.id.menu_feedback:
                        getActivityPresenter().onFeedbackMenuClicked();
                        break;
                }
                binding.drawerLayout.closeDrawers();
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerToggle.onOptionsItemSelected(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDailyPlan() {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        Toast toast = Toast.makeText(getApplicationContext(), "daily plan ", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showReports(){
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        Toast toast = Toast.makeText(getApplicationContext(), "reports ", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showCounter(){
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        Toast toast = Toast.makeText(getApplicationContext(), "counter ", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showFoodBrowse() {
        Intent intent = new Intent(this, BrowseActivity.class);
        startActivity(intent);
        Toast toast = Toast.makeText(getApplicationContext(), "browse ", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showDietPlan(){
        Intent intent = new Intent(this, DietActivity.class);
        startActivity(intent);
    }

    public void showSettings() {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        Toast toast = Toast.makeText(getApplicationContext(), "settings ", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showHelp() {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        Toast toast = Toast.makeText(getApplicationContext(), "help ", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showFeedback(){
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        Toast toast = Toast.makeText(getApplicationContext(), "feedback ", Toast.LENGTH_SHORT);
        toast.show();
    }

}

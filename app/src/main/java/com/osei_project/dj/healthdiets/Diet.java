package com.osei_project.dj.healthdiets;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;


public class Diet extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<Child> mSportsData;
    private childAdapter mAdapter;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

// [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Healthy Diet");

        loadBackdrop();




        //Initialize the RecyclerView
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);

        //Set the Layout Manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Initialize the ArrayList that will contain the data
        mSportsData = new ArrayList<>();

        //Initialize the adapter and set it ot the RecyclerView
        mAdapter = new childAdapter(this, mSportsData);
        mRecyclerView.setAdapter(mAdapter);

        //Get the data
        initializeData();

        //Helper class for creating swipe to dismiss and drag and drop functionality
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback
                (ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN
                        | ItemTouchHelper.UP, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {

                //Get the from and to position
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();

                //Swap the items and notify the adapter
                Collections.swap(mSportsData, from, to);
                mAdapter.notifyItemMoved(from, to);
                return true;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                //Remove the item from the dataset
                mSportsData.remove(viewHolder.getAdapterPosition());

                //Notify the adapter
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });

        //Attach the helper to the RecyclerView
        helper.attachToRecyclerView(mRecyclerView);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    /**
     * Method for initializing the sports data from resources.
     */
    private void initializeData() {
        //Get the resources from the XML file
        String[]childList = getResources().getStringArray(R.array.ChildTitles);
        String[] childInfo = getResources().getStringArray(R.array.ChildInfo);
        String[] childDetail = getResources().getStringArray(R.array.ChildDetail);
        String[] childNews = getResources().getStringArray(R.array.ChildNews);
        TypedArray childImageResources = getResources().obtainTypedArray(R.array.ChildCareImage);
        //Clear the existing data (to avoid duplication)
        mSportsData.clear();


        //Create the ArrayList of Sports objects with the titles, images
        // and information about each sport
        for(int i=0; i<childList.length; i++){
            mSportsData.add(new Child(childList[i], childInfo[i], childDetail[i],childNews[i],
                    childImageResources.getResourceId(i,0)));
        }

        //Recycle the typed array
        childImageResources.recycle();

        //Notify the adapter of the change
        mAdapter.notifyDataSetChanged();
    }


    public void resetChildCareList(View view) {
        initializeData();
    }

    private void loadBackdrop() {
        final ImageView imageView = findViewById(R.id.backdrop);
        Glide.with(this).load(DropBack.getRandomChildDrawable()).centerCrop().into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            Toast.makeText(this, "Home Selected", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Healthy Diet Tips");
            alertDialog.setIcon(R.drawable.lock);
            // Setting Dialog Message
            alertDialog.setMessage("Are you sure you want to log out?");


            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    // Write your code here to invoke YES event
                    signOut();

                }
            });
            alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alertDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        if (id == R.id.home) {
            Toast.makeText(this, "Home Selected", Toast.LENGTH_SHORT).show();
            // Handle the camera action
        } else if (id == R.id.game) {
            Intent launchIntent = getApplicationContext().getPackageManager().getLaunchIntentForPackage("promzy.com.dietgame");
            startActivity(launchIntent);
            finish();
        } else if (id == R.id.setup) {
            Intent launchIntent = getApplicationContext().getPackageManager().getLaunchIntentForPackage("szklimek.diettracker");
            if (launchIntent != null) {
                launchIntent.putExtra("AppID", "MY-CHILD-APP1");
                launchIntent.putExtra("UserID", "MY-APP");
                launchIntent.putExtra("Password", "MY-PASSWORD");
                startActivity(launchIntent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), " launch Intent not available", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.logout) {
            Toast.makeText(this, "Home Selected", Toast.LENGTH_SHORT).show();

            alertDialog.setTitle("Healthy Diet Tips");
            alertDialog.setIcon(R.drawable.lock);
            // Setting Dialog Message
            alertDialog.setMessage("Are you sure you want to log out?");


            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    // Write your code here to invoke YES event
                    signOut();
                }
            });
            alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void signOut() {

//        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
//        // [END initialize_auth]*/
//        fireuser = mAuth.getCurrentUser();
//        mfirebaseDatabase = FirebaseDatabase.getInstance();
//        mref = mfirebaseDatabase.getReference();
//        userid = fireuser.getUid();
        mAuth.signOut();
        startActivity(new Intent(Diet.this,LoginActivity.class));
        super.finish();

    }
}

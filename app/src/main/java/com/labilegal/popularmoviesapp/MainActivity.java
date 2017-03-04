package com.labilegal.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.labilegal.popularmoviesapp.data.Movie;


public class MainActivity extends AppCompatActivity implements MainFragment.OnHeadMovieSelectedListener {

    private static final String STRING_FOR_ON_SAVE_MODE_TABLETTE = "mode_tablette";
    private boolean mModeTablette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (findViewById(R.id.fragment_detail) != null) {
            // le cas sur tablette
            mModeTablette = true;
        } else {
            // le cas sur tel
            mModeTablette = false;
        }

        if (findViewById(R.id.fragment_main) != null) {

            if (savedInstanceState != null) {
                mModeTablette = savedInstanceState.getBoolean(STRING_FOR_ON_SAVE_MODE_TABLETTE);
                return;
            }

            MainFragment mainFragment = new MainFragment();
            //mainFragment.setArguments(getIntent().getExtras());

            Bundle args = new Bundle();
            args.putBoolean("mModeTablette", mModeTablette);
            mainFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_main, mainFragment).commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STRING_FOR_ON_SAVE_MODE_TABLETTE, mModeTablette);
    }

    @Override
    public void onMovieSelected(Movie movie) {
        DetailFragment detailFragment = (DetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_detail);

        if (detailFragment != null) {

            if (mModeTablette == true) {
                detailFragment.updateContent(movie);
            } else {
            }

        } else {

            if (mModeTablette == true) {

                DetailFragment newFragment = new DetailFragment();
                Bundle args = new Bundle();
                args.putParcelable("movie", movie);
                newFragment.setArguments(args);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_detail, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            } else {
                intentForDetail(movie);
            }
        }
    }

    public void intentForDetail(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}

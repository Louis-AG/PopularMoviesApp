package com.labilegal.popularmoviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Log.v("DetailActivity ", "on est dans le onCreate");

        if (findViewById(R.id.detail_fragment) != null) {
            Log.v("DetailActivity ", "on est dans le (findViewById(R.id.detail_fragment) != null) ");

            if (savedInstanceState != null) {
                return;
            }

            DetailFragment newFragment = new DetailFragment();
            newFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment, newFragment).commit();

            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.detailmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                finish();

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}

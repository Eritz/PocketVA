package com.android.app.pocketva;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


/** The MainActivity that acts as a fragment container for the MainListFragment. Here it will
 *  instantiate a new MainListFragment and save/load the ActorList.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG_RETAINED_LIST = "MainListFragment";
    private static final String SAVED_LIST = "Saved List";
    private static final String SHAREDPREFERENCE_NAME = "com.pocketVA.savedList";
    private  MainListFragment mListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        loadSharedPrefsList(getApplication());
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FragmentManager fm = getFragmentManager();

        if (savedInstanceState == null) {
            mListFragment = new MainListFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragmentContainer, mListFragment).commit();
        } else {
            mListFragment = (MainListFragment) fm.findFragmentByTag(TAG_RETAINED_LIST);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_screen_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchView).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSharedPrefsList(getApplication());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSharedPrefsList(getApplication());
    }

    public void saveSharedPrefsList(Context context) {
        SharedPreferences appSharedPrefs = context.getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String jsonList = gson.toJson(ActorList.getActorListInstance().getActorSavedList());
        prefsEditor.putString(SAVED_LIST, jsonList);
        prefsEditor.commit();
    }

    public void loadSharedPrefsList(Context context) {
        SharedPreferences appSharedPrefs = context.getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(SAVED_LIST,null);
        Type type = new TypeToken<ArrayList<Actor>>() {}.getType();
        ArrayList<Actor> restoreList = gson.fromJson(json, type);
        ActorList.getActorListInstance().restoreSavedList(restoreList);

    }
}

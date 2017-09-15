package com.android.app.pocketva;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.google.gson.Gson;

/** From SearchActivity, when clicked on a result generated the SearchClickedActivity will be
 *  initialized. SearchClickedActivity will have the SearchClickedFragment inside of it.
 *  The intent passed down when clicking on an Actor in the search screen will be further passed
 *  to the SearchClickedFragment to generate all the information for display.
 *  Any time the application reaches onPause(), it will automatically save the current list.
 */
public class SearchClickedActivity extends AppCompatActivity{

    private static final String CURRENT_SEARCH_CLICKED = "SearchClickedFragment";
    private static final String SHAREDPREFERENCE_NAME = "com.pocketva.savedList";
    private static final String SAVED_LIST = "Saved List";

    private SearchClickedFragment mSearchClickedFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_clicked_screen);

        FragmentManager fm = getFragmentManager();

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle = getIntent().getExtras();
            mSearchClickedFragment = new SearchClickedFragment();
            mSearchClickedFragment.setArguments(bundle);

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.searchFragmentContainer, mSearchClickedFragment).commit();

        } else {
            mSearchClickedFragment = (SearchClickedFragment)
                    fm.findFragmentByTag(CURRENT_SEARCH_CLICKED);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search_clicked_menu,menu);
        return true;
    }

    public void saveSharedPrefsList(Context context) {
        SharedPreferences appSharedPrefs = context.getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String jsonList = gson.toJson(ActorList.getActorListInstance().getActorSavedList());
        prefsEditor.putString(SAVED_LIST, jsonList);
        prefsEditor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSharedPrefsList(getApplication());
    }
}

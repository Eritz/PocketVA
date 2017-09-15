package com.android.app.pocketva;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** SearchActivity will be called when performing a search by the SearchView created
 *  in MainActivity. It will generate an access token, and initiate a GET request to find
 *  all Actors in AniList's database that matches the query sent.
 */
public class SearchActivity extends AppCompatActivity {

    private SearchAccess searchAccess;
    private String currentQuery;

    private RecyclerView recyclerView;
    private TextView emptyView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);

        recyclerView = (RecyclerView) findViewById(R.id.search_recyclerView);
        emptyView = (TextView) findViewById(android.R.id.empty);
        emptyView.setVisibility(View.GONE);

        Intent intent = getIntent();
        handleIntent(intent);

        searchAccess = SearchAccess.getInstance();
        getToken();

    }

    /** Retrieve the query sent from the SearchView in MainActivity */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            currentQuery = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    /** Generate an access token using the RetrofitClient, and automatically call the
     *  doSearch() method.
     */
    public void getToken() {
        AniList apiService = RetrofitClient.getInstance(this).create(AniList.class);
        Call<SearchAccess> call = apiService.getAccessToken(AniList.GRANT_TYPE,
                AniList.CLIENT_ID, AniList.CLIENT_SECRET);
        call.enqueue(new Callback<SearchAccess>() {
            @Override
            public void onResponse(Call<SearchAccess> call, Response<SearchAccess> response) {
                String token = response.body().getToken();
                searchAccess.setToken(token);
                doSearch(token);
            }

            @Override
            public void onFailure(Call<SearchAccess> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Error: " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Use the query obtained in handleIntent(), to do a GET request for all actors
     *  in AniList's database that matches the query.
     * @param token - The token obtained from getToken()
     */
    public void doSearch(String token) {
        AniList apiService = RetrofitClient.getInstance(getApplication()).create(AniList.class);
        Call<List<SearchItem>> call = apiService.getActorList(currentQuery, token);
        call.enqueue(new Callback<List<SearchItem>>() {
            @Override
            public void onResponse(Call<List<SearchItem>> call, Response<List<SearchItem>> response) {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.search_progressBar);
                List<SearchItem> results  = response.body();
                Collections.sort(results);
                progressBar.setVisibility(View.GONE);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
                recyclerView.setAdapter(new SearchAdapter(results, getApplicationContext()));

            }

            @Override
            public void onFailure(Call<List<SearchItem>> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.search_progressBar);
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                emptyView.setVisibility(View.VISIBLE);
            }
        });

    }

}

package com.android.app.pocketva;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** This Activity displays saved Actor information in the list. It will be created when clicking
 *  on the MainListFragment's actor item that is displayed.
 */
public class ActorActivity extends AppCompatActivity {

    private static final String TAG = "ActorActivity";
    public static final String PASSED_POSITION =
            "com.example.eddie.pocketva.passed_position";
    public static final String EXTRA_CHANGED_FAVORITE =
            "com.example.eddie.pocketva.changed_favorite";
    public static final String PASSED_ID =
            "com.example.eddie.pocketva.passed_id";

    private int passedId;
    private int passedPosition;

    private static SearchAccess searchAccess;
    private RecyclerView recyclerView;
    private TextView showSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_actor);
        searchAccess = SearchAccess.getInstance();

        String passedName = getIntent().getStringExtra(MainListFragment.EXTRA_NAME);
        Boolean passedFavorite = getIntent().getBooleanExtra(MainListFragment.EXTRA_FAVORITE,false);
        passedPosition = getIntent().getIntExtra(MainListFragment.EXTRA_POSITION,-1);
        passedId = getIntent().getIntExtra(MainListFragment.EXTRA_ACTOR_ID,0);
        String passedUrl = getIntent().getStringExtra(MainListFragment.EXTRA_ACTOR_IMAGE);
        String passedInfo = getIntent().getStringExtra(MainListFragment.EXTRA_ACTOR_INFO);

        // Set the passed result
        ImageView image = (ImageView) findViewById(R.id.actor_face);
        Picasso.with(this).load(passedUrl).into(image);
        TextView name = (TextView) findViewById(R.id.actor_name);
        name.setText(passedName);
        TextView description = (TextView) findViewById(R.id.actor_info);
        description.setMovementMethod(new ScrollingMovementMethod());
        description.setText(passedInfo);


        CheckBox checkBox = (CheckBox) findViewById(R.id.actor_favorite);
        checkBox.setChecked(passedFavorite);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isFavorite) {
                Intent data = new Intent();
                data.putExtra(EXTRA_CHANGED_FAVORITE, isFavorite);
                data.putExtra(PASSED_POSITION, passedPosition);
                data.putExtra(PASSED_ID, passedId);
                setResult(RESULT_OK, data);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.character_recyclerView);
        LinearLayout seiyuuLayout = (LinearLayout) findViewById(R.id.actor_screen_layout);
        showSize = seiyuuLayout.findViewById(R.id.divider_show_text);

        getToken(getApplication());
    }

    /** Uses the RetrofitClient's private instance to get an OAuth2 token from AniList's API.
     *  If it's a success, it will automatically call getRoles(). Had to set an obtain access token
     *  method here because it's not guaranteed that there will already be an access token in use
     *  prior to starting the ActorActivity.
     */
    public void getToken(Context context) {
        AniList apiService = RetrofitClient.getInstance(context).create(AniList.class);
        Call<SearchAccess> call = apiService.getAccessToken(AniList.GRANT_TYPE,
                AniList.CLIENT_ID, AniList.CLIENT_SECRET);
        call.enqueue(new Callback<SearchAccess>() {
            @Override
            public void onResponse(Call<SearchAccess> call, Response<SearchAccess> response) {
                String token = response.body().getToken();
                searchAccess.setToken(token);
                getRoles();
            }

            @Override
            public void onFailure(Call<SearchAccess> call, Throwable t) {
                Toast.makeText(ActorActivity.this, "Error: " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** This method will use an access token generated to display information about all
     *  the roles performed. Since it uses an access token each time, it guarantees the most
     *  updated information.
     */
    public void getRoles() {

        AniList apiService = RetrofitClient.getInstance(this).create(AniList.class);
        String token = searchAccess.getToken();
        Call<SearchItem> call = apiService.getActorRoles(passedId,token);
        call.enqueue(new Callback<SearchItem>() {
            @Override
            public void onResponse(Call<SearchItem> call, Response<SearchItem> response) {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.saved_actor_progressBar);
                List<Shows> results = response.body().getShowsList();
                String listSize = getResources().getString(R.string.divider_show);
                showSize.setText(String.format(listSize, results.size()));
                if (results.size() == 0) {
                    showSize.setText(String.format(listSize, 0));
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplication(),"Not a Voice Actor", Toast.LENGTH_SHORT).show();
                    return;
                }
                Collections.sort(results);
                progressBar.setVisibility(View.GONE);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
                recyclerView.setAdapter(new CharacterAdapter(results, getApplication()));
            }

            @Override
            public void onFailure(Call<SearchItem> call, Throwable t) {
                Toast.makeText(ActorActivity.this, "Error: " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_seiyuu_from_list_menu, menu);
        menu.findItem(R.id.seiyuu_delete);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.seiyuu_delete:
                ActorList actorListInstance = ActorList.getActorListInstance();
                actorListInstance.getActorSavedList().remove(passedPosition);
                actorListInstance.getActorSavedList().trimToSize();
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

}

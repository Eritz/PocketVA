package com.android.app.pocketva;



import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Displays the clicked actor result from the search results here. SearchClickedFragment will
 *  retrieve the Bundle sent from the SearchAdapter->SearchClickedActivity. It will use that
 *  information to populate the display's information and to generate a search for the actor's roles.
 */
public class SearchClickedFragment extends Fragment {

    private static final String SAVED_LIST = "Saved List";
    private static final String SHAREDPREFERENCE_NAME = "com.pocketVA.savedList";

    private static SearchAccess searchAccess;
    private ActorList actorListInstance;
    private RecyclerView recyclerView;

    private String clickedName;
    private int actorId;
    private String actorInfo;
    private String actorImage;
    private TextView showSize;

    private int arrayIndex;

    private boolean clickedAdd = false;

    /** Displays the passed down information from SearchAdapter to SearchClickedActivity to here.
     *  After it retrieves and sets the information into its view, the AniList interface will be
     *  called to retrieve the character information. The recycler view will be populated by making
     *  use of the CharacterAdapter created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_clicked, parent, false);
        searchAccess = SearchAccess.getInstance();

        Bundle bundle = this.getArguments();
        clickedName = bundle.getString("Clicked Actor Full Name");
        actorId = bundle.getInt("Clicked Actor Id");
        actorInfo = bundle.getString("Clicked Actor Info");
        actorImage = bundle.getString("Clicked Actor Image");

        checkInList(actorId);

        recyclerView = view.findViewById(R.id.character_recyclerView);

        LinearLayout seiyuuLayout = view.findViewById(R.id.actor_screen_layout);
        showSize = seiyuuLayout.findViewById(R.id.divider_show_text);
        ImageView image = seiyuuLayout.findViewById(R.id.actor_face);
        Picasso.with(getActivity()).load(actorImage).into(image);
        TextView name = seiyuuLayout.findViewById(R.id.actor_name);
        name.setText(clickedName);
        TextView description = seiyuuLayout.findViewById(R.id.actor_info);
        description.setMovementMethod(new ScrollingMovementMethod());
        description.setText(actorInfo);
        CheckBox checkBox = seiyuuLayout.findViewById(R.id.actor_favorite);
        checkBox.setVisibility(View.GONE);

        getRoles();

        return view;
    }

    /** There will already be an access token generated because of SearchActivity.
     *  This method will display the number of shows performed by the actor into the
     *  String resource, and will populate the RecyclerView using the CharacterAdapter.
     */
    public void getRoles() {
        AniList apiService = RetrofitClient.getInstance(getActivity()).create(AniList.class);
        final String token = searchAccess.getToken();
        Call<SearchItem> call = apiService.getActorRoles(actorId,token);
        call.enqueue(new Callback<SearchItem>() {
            @Override
            public void onResponse(Call<SearchItem> call, Response<SearchItem> response) {
                ProgressBar progressBar = getActivity().findViewById(
                        R.id.search_clicked_progressBar);
                List<Shows> results = response.body().getShowsList();
                String listSize = getActivity().getResources().getString(R.string.divider_show);
                showSize.setText(String.format(listSize, results.size()));
                if (results.size() == 0) {
                    showSize.setText(String.format(listSize, 0));
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Not a Voice Actor", Toast.LENGTH_SHORT).show();
                    return;
                }
                Collections.sort(results);
                progressBar.setVisibility(View.GONE);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(new CharacterAdapter(results, getActivity()));
            }

            @Override
            public void onFailure(Call<SearchItem> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Check if the clicked actor is already in the currently saved list.
     *  If there is no saved list or if the saved list is empty, set the arrayIndex for this class
     *  as 0. Else, set the arrayIndex based on the data set's item position. In addition, set the
     *  clickedAdd boolean to true so that the Action Bar will force a delete option, rather than
     *  an add-to-list option. The arrayIndex is used in case the item needs to be deleted.
     * @param checkActorId - the unique actor ID retrieved from the Bundle
     */
    public void checkInList(int checkActorId) {
        ArrayList<Actor> currentList = ActorList.getActorListInstance().getActorSavedList();
        if (!currentList.isEmpty()) {
            for (Actor item : currentList) {
                if (item.getmActorId() == checkActorId) {
                    arrayIndex = currentList.indexOf(item);
                    clickedAdd = true;
                    getActivity().invalidateOptionsMenu();
                    break;
                }
            }
        } else {
            arrayIndex = 0;
        }
    }

    /** The add-to-list and delete-from-list option is set up in the Action Bar */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        actorListInstance = ActorList.getActorListInstance();
        ArrayList<Actor> currentSavedList = actorListInstance.getActorSavedList();

        switch(item.getItemId()) {
            case R.id.add_to_main:
                currentSavedList.add(new Actor(
                        clickedName,false, actorInfo,actorImage,actorId));
                if (currentSavedList.size() == 1) {
                    arrayIndex = 0;
                } else {
                    arrayIndex = currentSavedList.size() - 1;
                }
                clickedAdd = true;
                saveSharedPrefsList(getActivity());
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.delete_from_main:
                currentSavedList.remove(arrayIndex);
                clickedAdd = false;
                saveSharedPrefsList(getActivity());
                getActivity().invalidateOptionsMenu();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void saveSharedPrefsList(Context context) {
        SharedPreferences appSharedPrefs = context.getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String jsonList = gson.toJson(ActorList.getActorListInstance().getActorSavedList());
        prefsEditor.putString(SAVED_LIST, jsonList);
        prefsEditor.apply();
    }

    /** Use the clickedAdd boolean to check which menu item to display in the Action Bar.
     *  This helps prevent adding an item twice into the list or pressing delete and causing
     *  an OutOfBound exception.
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem addItem = menu.findItem(R.id.add_to_main);
        MenuItem deleteItem = menu.findItem(R.id.delete_from_main);

        if (clickedAdd) {
            addItem.setVisible(false);
            deleteItem.setVisible(true);
        } else {
            addItem.setVisible(true);
            deleteItem.setVisible(false);
        }

    }
}
